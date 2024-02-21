package it.polito.did.g2.shopdrop

import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesomeMosaic
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.MoveToInbox
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.polito.did.g2.shopdrop.data.Cart
import it.polito.did.g2.shopdrop.data.CollectionStep
import it.polito.did.g2.shopdrop.data.Fees
import it.polito.did.g2.shopdrop.data.Locker
import it.polito.did.g2.shopdrop.data.Order
import it.polito.did.g2.shopdrop.data.OrderStateName
import it.polito.did.g2.shopdrop.data.Store
import it.polito.did.g2.shopdrop.data.StoreItem
import it.polito.did.g2.shopdrop.data.users.AdmUser
import it.polito.did.g2.shopdrop.data.users.CrrUser
import it.polito.did.g2.shopdrop.data.users.CstUser
import it.polito.did.g2.shopdrop.data.users.User
import it.polito.did.g2.shopdrop.data.users.UserQuery
import it.polito.did.g2.shopdrop.data.users.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    fun login(userQuery: UserQuery){

        val targetUID = fbRepo.usersList.value?.find { it.email == userQuery.email }?.uid

        if(targetUID != null){
            userQuery.errType = null

            if(fbRepo.usersList.value?.find { it.uid == targetUID }?.password == userQuery.password){
                _currUser.value = fbRepo.usersList.value!!.find { it.uid == targetUID }
                userQuery.role = _currUser.value?.role

                saveCredentials(_currUser.value!!)

            }else{
                userQuery.errType = UserQuery.LOGIN_ERROR_TYPE.PASSWORD
            }
        }else{
            userQuery.errType = UserQuery.LOGIN_ERROR_TYPE.NOT_FOUND
        }
    }

    fun logout(){
        Log.i("LOGOUT", "Removing login data from shared preferences")
        loginSP.edit().clear().apply()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////        PRODUCTS

    val prodsList : LiveData<MutableList<StoreItem>> = fbRepo.prodsList

    val storesList : LiveData<MutableList<Store>> = fbRepo.storesList

    val ordersList : LiveData<MutableList<Order>> = fbRepo.ordersList

    val lockersList : LiveData<MutableList<Locker>> = fbRepo.lockersList

    val usersList : LiveData<MutableList<User>> = fbRepo.usersList

    ////////////////////////////////////////////////////////////////////////////////////////////////        SHOPPING AND ORDER DATA

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
        _currOrder.value = Order(
            storeID = fbRepo.assignStore(),
            carrierID = fbRepo.assignCarrier(),
            customerID = _currUser.value?.uid,
            lockerID = lockersList.value?.first { it.isWorking && !it.isFull }?.id
        )

        Log.i("ORD_CR", "\tOrdine creato vuoto:\n${_currOrder.value.toString()}")
        _currOrder.value!!.items = _cart.value!!.items.toMap()

        Log.i("ORD_CR", "\tAggiunti gli elementi del carrello:\n${_currOrder.value.toString()}")
    }

    fun setNewLocker(newID: String){
        _currOrder.value?.lockerID = newID
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

    fun getPendingOrders(): List<String>?{
        val currentUser = _currUser.value
        var orders : List<String>? = null

        if(currentUser is CstUser){
            orders = currentUser.orders?.map { it.key }
        }

        return orders
    }

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
        Log.i("MVM INIT", "####################")
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

    ////////////////////////////////////////////////////////////////////////////////////////////////        UTILITIES

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateString(date: LocalDateTime): String{
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    fun getOrderStateStringId(state: OrderStateName?): Int{
        return when(state){
            OrderStateName.CANCELLED -> R.string.order_state_cancelled
            OrderStateName.CREATED -> R.string.order_state_created
            OrderStateName.RECEIVED -> R.string.order_state_received
            OrderStateName.CARRIED -> R.string.order_state_carried
            OrderStateName.AVAILABLE -> R.string.order_state_available
            OrderStateName.COLLECTED -> R.string.order_state_collected
            OrderStateName.ERROR -> R.string.order_state_error
            else -> R.string.order_state_error
        }
    }

    fun getOrderStateIcon(state: OrderStateName?): ImageVector{
        return when(state){
            OrderStateName.CANCELLED -> Icons.Filled.Close
            OrderStateName.CREATED -> Icons.Filled.Add
            OrderStateName.RECEIVED -> Icons.Filled.MoveToInbox
            OrderStateName.CARRIED -> Icons.Filled.DeliveryDining
            OrderStateName.AVAILABLE -> Icons.Filled.AutoAwesomeMosaic
            OrderStateName.COLLECTED -> Icons.Filled.CheckCircle
            OrderStateName.ERROR -> Icons.Filled.Error
            else -> Icons.Filled.Error
        }
    }

    enum class db_reset{ ORDERS, LOCKERS }


    // DEBUG
    fun debugDBReset(target: db_reset){
        when(target){
            db_reset.ORDERS -> {
                Log.w("RESET", "RESETTING ORDERS DB")
                fbRepo.DebugResetOrders()
            }

            db_reset.LOCKERS -> {
                Log.w("RESET", "RESETTING LOCKERS DB")
                fbRepo.DebugResetLockers()
            }
        }
    }
}