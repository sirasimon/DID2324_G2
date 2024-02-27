package it.polito.did.g2.shopdrop

import android.content.SharedPreferences
import android.os.Build
import android.os.CountDownTimer
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
import androidx.lifecycle.viewModelScope
import it.polito.did.g2.shopdrop.data.Cart
import it.polito.did.g2.shopdrop.data.CollectionStep
import it.polito.did.g2.shopdrop.data.Fees
import it.polito.did.g2.shopdrop.data.Locker
import it.polito.did.g2.shopdrop.data.Order
import it.polito.did.g2.shopdrop.data.OrderStateName
import it.polito.did.g2.shopdrop.data.Store
import it.polito.did.g2.shopdrop.data.StoreItem
import it.polito.did.g2.shopdrop.data.users.User
import it.polito.did.g2.shopdrop.data.users.UserQuery
import it.polito.did.g2.shopdrop.data.users.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel() : ViewModel(){
    val bldNum = "alpha-0008"

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

    private fun loadCurrentUser(email: String){
        Log.i("LOAD_USER", "LOADING CURRENT USER INFO")

        _currUser.value = fbRepo.getUserByEmail(email)

        //val user = fbRepo.getUser(email)
        /*
        val user : User? = if(loginSP.getString("uid", null) != null ){
            User(
                loginSP.getString("uid", null)!!,
                loginSP.getString("email", null)!!,
                loginSP.getString("password", null)!!,
                loginSP.getString("name", null)!!,
                UserRole.valueOf(loginSP.getString("role", null)!!)
            )
        }else{
            fbRepo.usersList.value?.find { it.email == email }
        }

         */
/*
        when(user?.role){
            UserRole.ADM -> {
                _currUser.value = AdmUser(
                    user.uid,
                    user.email,
                    user.password,
                    user.name,
                    user.role
                )
            }
            UserRole.CST -> {
                _currUser.value = CstUser(
                    user.uid,
                    user.email,
                    user.password,
                    user.name,
                    user.role,
                    null
                )
            }
            UserRole.CRR -> {
                _currUser.value = CrrUser(
                    user.uid,
                    user.email,
                    user.password,
                    user.name,
                    user.role,
                    (user as CrrUser).isFree
                )
            }
            else -> _currUser.value = null
        }
*/
        Log.i("LOAD_USER", "\tNow current user is: ${_currUser.value?.uid} (${currUser.value?.uid})")
    }

    fun loadCredentials(){
        Log.i("LOADING CR", "LOADING CREDENTIALS")

        if(loginSP.getString("uid", null) != null ){
            //loadCurrentUser(loginSP.getString("uid", null)!!)

            _currUser.value = User(
                loginSP.getString("uid", null)!!,
                loginSP.getString("email", null)!!,
                loginSP.getString("password", null)!!,
                loginSP.getString("name", null)!!,
                UserRole.valueOf(loginSP.getString("role", null)!!)
            )
            /*
            when(UserRole.valueOf(loginSP.getString("role", null)!!)){
                UserRole.CST -> {
                    _currUser.value = CstUser(
                        loginSP.getString("uid", null)!!,
                        loginSP.getString("email", null)!!,
                        loginSP.getString("password", null)!!,
                        loginSP.getString("name", null)!!,
                        UserRole.valueOf(loginSP.getString("role", null)!!)
                    )
                }
                UserRole.CRR -> {
                    _currUser.value = CrrUser(
                        loginSP.getString("uid", null)!!,
                        loginSP.getString("email", null)!!,
                        loginSP.getString("password", null)!!,
                        loginSP.getString("name", null)!!,
                        UserRole.valueOf(loginSP.getString("role", null)!!)
                    )
                }
                UserRole.ADM -> {
                    _currUser.value = AdmUser(
                        loginSP.getString("uid", null)!!,
                        loginSP.getString("email", null)!!,
                        loginSP.getString("password", null)!!,
                        loginSP.getString("name", null)!!,
                        UserRole.valueOf(loginSP.getString("role", null)!!)
                    )
                }
                UserRole.NUL -> TODO()
            }
            */
        }else{
            Log.i("LOADING CR", "\tNo credentials saved yet")
        }
    }

    fun validateCredentials(userQuery: UserQuery){
        Log.i("VALIDATE CR", "VALIDATING CREDENTIALS")

        userQuery.errType = null

        val targetUID = fbRepo.usersList.value?.find { it.email == userQuery.email }?.uid

        if(targetUID != null){
            if(fbRepo.usersList.value?.find { it.uid == targetUID }?.password != userQuery.password){
                userQuery.errType = UserQuery.LOGIN_ERROR_TYPE.PASSWORD
            }else{
                userQuery.role = fbRepo.usersList.value?.find { it.uid == targetUID }?.role
            }
        }else{
            userQuery.errType = UserQuery.LOGIN_ERROR_TYPE.NOT_FOUND
        }
    }

    private fun saveCredentials(){
        Log.i("SAVE_CREDS", "SAVING CREDENTIALS INTO SHARED PREFERENCES")

        val newCredentials = _currUser.value

        Log.i("SAVE_CREDS", "\tSaving into $loginSP data of _currUser (${_currUser.value?.uid})")

        loginSP.edit().apply{
            putString("uid", newCredentials?.uid)
            putString("email", newCredentials?.email)
            putString("password", newCredentials?.password)
            putString("name", newCredentials?.name)
            putString("role", newCredentials?.role.toString())
            apply()
        }

        Log.i("SAVE_CREDS", "DONE!")
        Log.i("SAVE_CREDS", "\tData saved is: " +
                "${loginSP.getString("uid", "[uid here]")} – " +
                "${loginSP.getString("email", "[email here]")} – " +
                "${loginSP.getString("password", "[password here]")} – " +
                "${loginSP.getString("role", "[role here]")}")
    }

    private fun deleteCredentials(){
        Log.i("DELETE CR", "DELETING CREDENTIALS")

        loginSP.edit().clear().apply()
    }

    fun login(email: String){
        Log.i("LOGIN", "LOGGING IN WITH EMAIL $email")
        loadCurrentUser(email)
        Log.i("LOGIN", "SAVING CREDENTIALS")
        saveCredentials()
    }

    fun logout(){
        Log.i("LOGOUT", "Removing login data from shared preferences")

        deleteCredentials()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////        PRODUCTS

    val prodsList : LiveData<MutableList<StoreItem>> = fbRepo.prodsList

    val storesList : LiveData<MutableList<Store>> = fbRepo.storesList

    val ordersList : LiveData<MutableList<Order>> = fbRepo.ordersList

    val lockersList : LiveData<MutableList<Locker>> = fbRepo.lockersList

    val usersList : LiveData<MutableList<User>> = fbRepo.usersList

    ////////////////////////////////////////////////////////////////////////////////////////////////        CART AND ORDER DATA

    // PENDING ORDERS
    //private val _userOrders : MutableLiveData<MutableList<Order>> = MutableLiveData(fbRepo.ordersList.value?.filter { it.customerID == _currUser.value?.uid }?.toMutableList())
    //val userOrders : LiveData<MutableList<Order>> = _userOrders

    //CART DATA
    private val _cart : MutableLiveData<Cart?> = MutableLiveData(null)
    val cart : LiveData<Cart?> = _cart

    //CURRENT ORDER DATA
    private val _currOrder : MutableLiveData<Order?> = MutableLiveData(null)
    val currOrder : LiveData<Order?> = _currOrder

    fun getPendingOrders() : List<Order>?{
        return ordersList.value?.filter { it.customerID == _currUser.value?.uid && it.isPending() }
    }

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

            if(_cart.value?.totalItems == 0)
                _cart.value = null
        }else{
            Log.w("MODIFY_CART", "\tITEM IS NULL")
        }
    }

    fun emptyCart(){
        _cart.value = null
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateOrderState(id: String){
        fbRepo.updateOrderState(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun cancelOrder(id: String){
        fbRepo.cancelOrder(id)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////        CST COLLECTION
    //TODO: unire con script dopo

    private val _timerValue = MutableLiveData<Long>().also { it.value=0 }
    val timerValue: LiveData<Long> = _timerValue

    private var _timer : CountDownTimer?= null

    private var _isTimeout = MutableLiveData<Boolean>().also{it.value=false}
    val isTimeout: LiveData<Boolean> = _isTimeout

    //var previousisOpenVal : Boolean? = null
    //private val _isOpen = MutableLiveData<Boolean?>(null)
    //val isOpen : LiveData<Boolean?> = _isOpen
    val is1Open : MutableStateFlow<Boolean?> = fbRepo.isComp1Open
    val is2Open : MutableStateFlow<Boolean?> = fbRepo.isComp2Open

    fun createTimer(timeout:Long){
        //fbRepo.startObserveCompartment()
        ///_isOpen.value = true

        _timer = object : CountDownTimer(timeout, 1000){

            override fun onTick(remainingTime: Long) {
                if(_isTimeout.value==true)
                    _isTimeout.value = false

                Log.d("onTick", "Remaining time is $remainingTime")

                _timerValue.value = remainingTime

                Log.d("onTick", "Updated timer value is ${_timerValue.value}")
            }

            override fun onFinish() {
                _isTimeout.value = true;
            }
        }.start()
    }

    fun getTimerValue() : Long{
        return _timerValue.value ?: 0;
    }

    fun cancelTimer(){
        _timer?.cancel()

        //_isOpen.value = null
    }



    fun cstStartsCollection(lockerCode : String, orderID: String) : Boolean{
        return fbRepo.cstStartsCollection(lockerCode, orderID)
    }

    fun checkClosed(orderID: String): Boolean{
        return lockersList.value
            ?.find { lk -> lk.id == ordersList.value?.find { or -> or.id == orderID }?.lockerID }
            ?.compartments
            ?.find { co -> co.orderID == orderID }
            ?.isOpen
            ?:false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun cstHasCollected(orderID: String){
        fbRepo.updateToCollected(orderID)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////        CRR COLLECTION + DEPOSIT

    @RequiresApi(Build.VERSION_CODES.O)
    fun crrHasCollected(orderID: String){
        fbRepo.updateToCarried(orderID)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun crrHasDeposited(orderID: String){
        fbRepo.updateToAvailable(orderID)
    }

    fun crrStartsDeposit(lockerCode: String, orderID: String) : Boolean{
        return fbRepo.crrOccupyLocker(lockerCode, orderID)
    }

    fun crrDepositing(orderID: String){
        fbRepo.crrDepositing(orderID)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////        OPENING PROCEDURE

    private val _collectionStep : MutableLiveData<CollectionStep> = MutableLiveData(CollectionStep.NONE)
    val collectionStep : LiveData<CollectionStep> = _collectionStep


    ////////////////////////////////////////////////////////////////////////////////////////////////


    //ORDERS
    private val _hasPending : MutableLiveData<Boolean> = MutableLiveData(true)
    val hasPending : LiveData<Boolean> = _hasPending

    var targetOrderID : String? = null


    init{
        Log.i("MVM INIT", "####################")
        viewModelScope.launch {
            fbRepo.initUsers()
            fbRepo.initProducts()
            fbRepo.initOrders()
            fbRepo.initLockers()
            fbRepo.initStores()
        }

        _isLoading.value = false

        /*
        viewModelScope.launch {
            fbRepo.startObserveCompartment()?.collect(){newVal ->
                previousisOpenVal?.let{oldVal ->
                    if(oldVal && !newVal){
                        cancelTimer()
                    }
                }

                previousisOpenVal = newVal
                _isOpenFieldState.value = newVal

            }

        }
        */

        Log.i("MVM INIT", "END ####################")
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
    fun getDateString(date: LocalDateTime?): String{
        return date?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))?:"ERR"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeString(date: LocalDateTime): String{
        return date.format(DateTimeFormatter.ofPattern("HH:mm"))
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
                fbRepo.debugResetOrders()
            }

            db_reset.LOCKERS -> {
                Log.w("RESET", "RESETTING LOCKERS DB")
                fbRepo.DebugResetLockers()
            }
        }
    }
}