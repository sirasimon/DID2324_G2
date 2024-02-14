package it.polito.did.g2.shopdrop

import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.did.g2.shopdrop.data.Cart
import it.polito.did.g2.shopdrop.data.CollectionStep
import it.polito.did.g2.shopdrop.data.Fees
import it.polito.did.g2.shopdrop.data.Order
import it.polito.did.g2.shopdrop.data.StoreItem
import it.polito.did.g2.shopdrop.data.users.AdmUser
import it.polito.did.g2.shopdrop.data.users.CrrUser
import it.polito.did.g2.shopdrop.data.users.CstUser
import it.polito.did.g2.shopdrop.data.users.User
import it.polito.did.g2.shopdrop.data.users.UserQuery
import it.polito.did.g2.shopdrop.data.users.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel() : ViewModel(){
    ////////////////////////////////////////////////////////////////////////////////////////////////        GLOBAL DATA
    private val fbRepo = FirebaseRepository()

    ////////////////////////////////////////////////////////////////////////////////////////////////        STARTING APP (SPLASH SCREEN)
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    ////////////////////////////////////////////////////////////////////////////////////////////////        LOGIN AND USER DATA

    private lateinit var loginSP : SharedPreferences

    private  val _currUser : MutableLiveData<User?> = MutableLiveData(null)
    val currUser : LiveData<User?> = _currUser

    fun setLoginSP(sp: SharedPreferences){
        loginSP = sp
    }

    fun loadCredentials(){
        Log.i("LOADING CR", "LOADING CREDENTIALS")

        if(loginSP.getString("uid", null) != null ){
            when(UserRole.valueOf(loginSP.getString("role", "NUL")!!)){
                UserRole.ADM -> {
                    _currUser.value = AdmUser(
                        loginSP.getString("uid", "ERR")!!,
                        loginSP.getString("email", "ERR")!!,
                        loginSP.getString("password", "ERR")!!,
                        loginSP.getString("name", "ERR")!!,
                        UserRole.valueOf(loginSP.getString("role", "")!!)
                    )
                }
                UserRole.CST -> {
                    _currUser.value = CstUser(
                        loginSP.getString("uid", "ERR")!!,
                        loginSP.getString("email", "ERR")!!,
                        loginSP.getString("password", "ERR")!!,
                        loginSP.getString("name", "ERR")!!,
                        UserRole.valueOf(loginSP.getString("role", "")!!)
                    )
                }
                UserRole.CRR -> {
                    _currUser.value = CrrUser(
                        loginSP.getString("uid", "ERR")!!,
                        loginSP.getString("email", "ERR")!!,
                        loginSP.getString("password", "ERR")!!,
                        loginSP.getString("name", "ERR")!!,
                        UserRole.valueOf(loginSP.getString("role", "")!!)
                    )
                }
                else -> _currUser.value = null
            }
        }else{
            Log.i("LOADING CR", "\tNo credentials saved yet")
        }
    }

    fun saveCredentials(newCredentials: User){
        Log.i("SAVE_CREDS", "SAVING CREDENTIALS INTO SHARED PREFERENCES")

        loginSP.edit().apply{
            putString("uid", newCredentials.uid)
            putString("email", newCredentials.email)
            putString("password", newCredentials.password)
            putString("role", newCredentials.role.toString())
            apply()
        }

        Log.i("SAVE_CREDS", "DONE!")
    }

    suspend fun login(userQuery: UserQuery){

        val targetUID = fbRepo.usersList.value?.find { it.email == userQuery.email }?.uid

        if(targetUID != null){
            userQuery.errType = null

            if(fbRepo.usersList.value?.find { it.uid == targetUID }?.password == userQuery.password){
                _currUser.value = fbRepo.usersList.value!!.find { it.uid == targetUID }

                saveCredentials(_currUser.value!!)

            }else{
                userQuery.errType = UserQuery.LOGIN_ERROR_TYPE.PASSWORD
            }
        }else{
            userQuery.errType = UserQuery.LOGIN_ERROR_TYPE.NOT_FOUND
        }

        /*
        users.orderByChild("email")
            .equalTo(userQuery.email)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(dataSnapshot.children.count()==1){
                            Log.i("LOGIN", "FOUND 1 OCCURRENCE FOR EMAIL ${userQuery.email}")
                            for (snapshot in dataSnapshot.children) {
                                // Trovato un nodo con l'attributo desiderato e il valore noto
                                _userID.value = snapshot.key

                                userQuery.role = enumValueOf<UserRole>(snapshot.child("role").getValue<String>()?:"")
                                Log.i("LOGIN", "FOUND ${_userID.value}")

                                users.child(snapshot.key?:"").child("password")
                                    .addValueEventListener(
                                        object : ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                                Log.i("LOGIN", "Stored password is ${dataSnapshot.value}")

                                                if(userQuery.password!=dataSnapshot.value){
                                                    userQuery.errType = UserQuery.LOGIN_ERROR_TYPE.PASSWORD

                                                    Log.e("LOGIN", "PASSWORD IS NOT VALID")
                                                }else{
                                                    userQuery.errType = null
                                                    Log.i("LOGIN", "PASSWORD IS VALID")
                                                }

                                            }

                                            override fun onCancelled(databaseError: DatabaseError) {
                                                userQuery.role = null
                                                userQuery.errType = UserQuery.LOGIN_ERROR_TYPE.UNKNOWN

                                                Log.e("LOGIN", "UNKNOWN ERROR")
                                            }
                                        }
                                    )
                            }
                        }else{
                            if(userQuery.errType == null){
                                Log.e("LOGIN", "FOR EMAIL ${userQuery.email} COUNTED ${dataSnapshot.children.count()} OCCURRENCES")
                                userQuery.role = null
                                userQuery.errType = UserQuery.LOGIN_ERROR_TYPE.NOT_FOUND
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        userQuery.role = null
                        userQuery.errType = UserQuery.LOGIN_ERROR_TYPE.UNKNOWN

                        Log.e("LOGIN", "UNKNOWN ERROR")
                    }
                }
            )

         */
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////        PRODUCTS

    val prodsList : LiveData<MutableList<StoreItem>> = fbRepo.prodsList

    ////////////////////////////////////////////////////////////////////////////////////////////////        SHOPPING DATA

    //CART DATA
    private val _cart : MutableLiveData<Cart?> = MutableLiveData(null)
    val cart : LiveData<Cart?> = _cart

    //CURRENT ORDER DATA
    private val _currOrder : MutableLiveData<Order?> = MutableLiveData(null)
    val currOrder : LiveData<Order?> = _currOrder

    /**
     * Modifica della quantità degli elementi del carrello
     */
    fun modifyCart(item: StoreItem?, quantity: Int){
        Log.i("MODIFY_CART", "Trying to modify the cart")

        if(_cart.value==null)
            _cart.value = Cart()

        if(item!=null){
            _cart.value?.modify(item, quantity)

            Log.i("MODIFY_CART", "\tUpdated quantity of \"${item.name}\" in map is ${_cart.value?.items?.get(item.name) ?:"[ERROR]"} (desired change was $quantity)")
        }else{
            Log.w("MODIFY_CART", "\tITEM IS NULL")
        }
    }

    /**
     * Restituisce il totale della spesa comprensivo delle spese aggiuntive (fees)
     */
    fun getCartTotal(): Float{
        return (_cart.value?.subtot?:0f) + Fees.total()
    }


    /**
     * Istanziamento di un nuovo ordine per poterlo scrivere sul database
     */
    fun createOrder(){
        Log.i("ORD_CR", "Creazione dell'ordine")
        _currOrder.value = Order()

        Log.i("ORD_CR", "\tOrdine creato vuoto:\n${_currOrder.value.toString()}")
        _currOrder.value!!.items = _cart.value!!.items.toMap()

        Log.i("ORD_CR", "\tAggiunti gli elementi del carrello:\n${_currOrder.value.toString()}")
    }

    /**
     * Registrazione sul database del nuovo ordine appena inviato
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun placeOrder(){
        Log.i("ORD_PLACING", "Posizionamento dell'ordine:\n${_currOrder.value.toString()}")

        _currOrder.value!!.updateState()
        Log.i("ORD_PLACING", "\tAggiornamento dello stato:\n${_currOrder.value.toString()}")

        fbRepo.addOrder(_currOrder.value, _currUser.value?.uid)

        // svuota il carrello
        _cart.value = null

        // Svuota l'ordine
        _currOrder.value = null
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////        ORDERS MANAGEMENT



    ////////////////////////////////////////////////////////////////////////////////////////////////        OPENING PROCEDURE

    private val _collectionStep : MutableLiveData<CollectionStep> = MutableLiveData(CollectionStep.NONE)
    val collectionStep : LiveData<CollectionStep> = _collectionStep


    ////////////////////////////////////////////////////////////////////////////////////////////////


    //ORDERS
    private val _hasPending : MutableLiveData<Boolean> = MutableLiveData(true)
    val hasPending : LiveData<Boolean> = _hasPending

    var targetOrderID : String? = null


    //TODO Adesso HARDCODED ma da fare su server
    /*
    @RequiresApi(Build.VERSION_CODES.O)
    private val _pendingOrdersList : MutableLiveData<MutableList<Order>> = MutableLiveData(
        mutableListOf(
            Order("0x11", "STO_01", "0x13", "0x14", "LTO_01",
                listOf(
                    OrderState(OrderStateName.CREATED, LocalDateTime.now().minusHours(3)),
                    OrderState(OrderStateName.RECEIVED, LocalDateTime.now().minusHours(3).plusMinutes(35)),
                    OrderState(OrderStateName.CARRIED, LocalDateTime.now().minusHours(2).plusMinutes(15)),
                    OrderState(OrderStateName.AVAILABLE, LocalDateTime.now().minusMinutes(47)),
                )
            ),
            Order("0x21", "STO_01", "0x23", "0x24", "LTO_01",
                listOf(
                    OrderState(OrderStateName.CREATED, LocalDateTime.now().minusHours(2)),
                    OrderState(OrderStateName.RECEIVED, LocalDateTime.now().minusHours(2).plusMinutes(35)),
                    OrderState(OrderStateName.CARRIED, LocalDateTime.now().minusHours(1).plusMinutes(15)),
                )
            ),
            Order("0x31", "STO_01", "0x33", "0x34", "LTO_01",
                listOf(
                    OrderState(OrderStateName.CREATED, LocalDateTime.now().minusHours(1)),
                )
            )
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    val pendingOrdersList : LiveData<MutableList<Order>> = _ordersList
    */

    /*
    class OrdersList(
        pending : MutableList<Order>? = null,
        archived : MutableList<Order>? = null,
        cancelled : MutableList<Order>? = null
    )


    @RequiresApi(Build.VERSION_CODES.O)
    private val _ordersList : MutableLiveData<OrdersList> = MutableLiveData<OrdersList>(OrdersList())

    @RequiresApi(Build.VERSION_CODES.O)
    val ordersList : LiveData<OrdersList> = _ordersList
    */


    //DEBUG

    /*
    private val _apriVal : MutableLiveData<String> = MutableLiveData("")
    val apriVal : LiveData<String> = _apriVal

    private val orders = dbRef.child("debug").addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("DBCON", "Listening to value in orders")
                //Looking for orders for UID

                _apriVal.value = snapshot.child("apri").getValue<String>()

                Log.i("DBCON", "\t${_apriVal.value} ++ ${apriVal.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO
                Log.i("DB CON", "CANCELLED")
            }
        }
    )
     */

    //LOGIN


    //DEBUG FUNS
    /*
    fun debugInit(){
        Log.i("DBCON", "Store value BEFORE init is ${apriVal.value}")
        dbRef.child("debug").child("apri").setValue("false")
        Log.i("DBCON", "Store value AFTER init is ${apriVal.value}")
    }

    fun debugOpen(){
        dbRef.child("debug").child("apri").setValue("true")
    }

    fun debugSetDefault(){
        dbRef.child("debug").child("apri").setValue("null")
    }
     */

    init{
        fbRepo.initUsers()
        fbRepo.initProducts()
        fbRepo.initOrders()
        fbRepo.initLockers()
        fbRepo.initStores()

        _isLoading.value = false
    }

    /*
    fun getLockers(){
        //TODO implementare distanze e mappa

        dbRefLockers.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
        )

        //Attraverso una transazione impegna un cassetto per locker
    }
    */



    private fun updateCollectionStep(isDone : Boolean? = null){
        if(isDone==null){
            when(_collectionStep.value){
                CollectionStep.NONE         -> _collectionStep.value = CollectionStep.READY
                CollectionStep.READY        -> _collectionStep.value = CollectionStep.OPEN_OK
                CollectionStep.OPEN_OK      -> _collectionStep.value = CollectionStep.OPEN_MID
                CollectionStep.OPEN_MID     -> _collectionStep.value = CollectionStep.OPEN_LAST
                CollectionStep.OPEN_LAST    -> _collectionStep.value = CollectionStep.TIMEOUT
                CollectionStep.TIMEOUT      -> _collectionStep.value = CollectionStep.DONE
                CollectionStep.DONE         -> _collectionStep.value = CollectionStep.NONE
                else -> _collectionStep.value = CollectionStep.NONE
            }
        }
    }
}