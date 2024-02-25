package it.polito.did.g2.shopdrop

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import it.polito.did.g2.shopdrop.data.Compartment
import it.polito.did.g2.shopdrop.data.Locker
import it.polito.did.g2.shopdrop.data.Order
import it.polito.did.g2.shopdrop.data.OrderState
import it.polito.did.g2.shopdrop.data.OrderStateName
import it.polito.did.g2.shopdrop.data.Store
import it.polito.did.g2.shopdrop.data.StoreItem
import it.polito.did.g2.shopdrop.data.StoreItemCategory
import it.polito.did.g2.shopdrop.data.users.User
import it.polito.did.g2.shopdrop.data.users.UserRole
import java.time.LocalDateTime

//DEBUG
const val FB_DEBUG = true

class FirebaseRepository {
    //DB REFs
    private val dbRef = FirebaseDatabase.getInstance()
    private val debugRef = dbRef.getReference("debug")
    private val dbRefLockers = dbRef.getReference("lockers")
    private val dbRefUsers = dbRef.getReference("users")
    private val dbRefOrders = dbRef.getReference("orders")
    private val dbRefProducts = dbRef.getReference("products")
    private val dbRefStores = dbRef.getReference("stores")
    /*
    private val dbRef = Firebase.database.reference
    private val debugRef = dbRef.child("debug")
    private val dbRefLockers = dbRef.child("lockers")
    private val dbRefUsers = dbRef.child("users")
    private val dbRefOrders = dbRef.child("orders")
    private val dbRefProducts = dbRef.child("products")
    private val dbRefStores = dbRef.child("stores")
     */

    //DATA RETRIVED
    private val _isDataSet : MutableLiveData<Boolean> = MutableLiveData(false)
    val isDataSet : LiveData<Boolean> = _isDataSet

    private val _usersList : MutableLiveData<MutableList<User>> = MutableLiveData(mutableListOf())
    val usersList : LiveData<MutableList<User>> = _usersList

    private val _prodsList : MutableLiveData<MutableList<StoreItem>> = MutableLiveData(mutableListOf())
    val prodsList : LiveData<MutableList<StoreItem>> = _prodsList

    private val _ordersList : MutableLiveData<MutableList<Order>> = MutableLiveData(mutableListOf())
    val ordersList : LiveData<MutableList<Order>> = _ordersList

    private val _lockersList : MutableLiveData<MutableList<Locker>> = MutableLiveData(mutableListOf())
    val lockersList : LiveData<MutableList<Locker>> = _lockersList

    private val _storesList : MutableLiveData<MutableList<Store>> = MutableLiveData(mutableListOf())
    val storesList : LiveData<MutableList<Store>> = _storesList

    suspend fun initUsers(){
        fun cLog(msg: String){
            if(FB_DEBUG)
                Log.i("DB_USRS", msg)
        }

        //val newUserList = mutableListOf<User>()

        dbRefUsers.addValueEventListener(
            object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    cLog("DATA CHANGED in USERS")
                    cLog("\tReading updated users collection (number of children ${snapshot.childrenCount})")

                    for(snap in snapshot.children) {
                        var newUser : User?
                        cLog("\tCreation of a new user (id: ${snap.key.toString()}) to add locally (of role ${snap.child("role").getValue<String>()})")

                        newUser = User(
                            uid = snap.key.toString(),
                            email = snap.child("email").getValue<String>()?:"ERR",
                            password = snap.child("password").getValue<String>()?:"ERR",
                            name = snap.child("name").getValue<String>()?:"ERR",
                            role = UserRole.valueOf(snap.child("role").getValue<String>()?:"ADM"),
                        )

                        if(newUser!=null){
                            if(_usersList.value?.find { it.uid == newUser.uid }!=null)
                                _usersList.value?.remove(_usersList.value!!.find { it.uid == newUser.uid }!!)

                            _usersList.value?.add(newUser)

                            cLog("\tAdded new user to list: ${_usersList.value?.last()?.name}")
                        }else{
                            Log.w("DB_USRS", "CONVERSIONE ANDATA STORTO")
                        }
                    }

                    //_usersList.value?.clear()
                    //_usersList.value = newUserList.toMutableList()

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DB_USRS", "ERROR: $error")
                }
            }
        )

        Log.i("DB_USRS", "_userList has ${_usersList.value?.size} values")
    }

    suspend fun initProducts() {

        fun cLog(msg: String){
            if(FB_DEBUG)
                Log.i("DB_PROD", msg)
        }

        dbRefProducts.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cLog("DATA CHANGED in PRODUCTS")
                    cLog("\tReading updated products collection (number of children ${snapshot.childrenCount})")

                    for(snap in snapshot.children){
                        cLog("Trying to add to list the product ${snap.key}")
                        cLog("\tThe new item should contain:\n\tname: ${snap.key}\n" +
                                "\tprice: ${snap.child("price").getValue<Float>()}\n" +
                                "\tcategory: ${StoreItemCategory.valueOf(snap.child("category").getValue<String>()?.uppercase()?:"<null>").toString()}\n" +
                                "\tthumbnail: ${snap.child("thumbnail").getValue<String>()}")

                        val newItem = StoreItem(
                            snap.key.toString(),
                            snap.child("price").getValue<Float>()?:.0f,
                            StoreItemCategory.valueOf(snap.child("category").getValue<String>()?.uppercase()?:"<null>"),
                            snap.child("thumbnail").getValue<String>()?:""
                            //publicURL.toString()
                        )

                        cLog("\tNew object of StoreItem is: ${newItem}\n\tPutting it inside the list")

                        _prodsList.value?.add(newItem)

                        cLog("\tList is now ${_prodsList.value?.size} items long and the last product added is ${_prodsList.value?.last().toString()}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DB_PROD", "ERROR: $error")
                }

            }
        )
    }

    suspend fun initOrders() {

        //var newOrderList = mutableListOf<Order>()

        fun cLog(msg: String){
            if(FB_DEBUG)
                Log.i("DB_ORDRS", msg)
        }

        dbRefOrders.addValueEventListener(
            object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)

                override fun onDataChange(snapshot: DataSnapshot) {
                    cLog("DATA ADDED in ORDERS")
                    cLog("\tReading updated orders collection (number of children ${snapshot.childrenCount})")

                    for(snap in snapshot.children){
                        cLog("Trying to add to list the order ${snap.key.toString()}")

                        cLog("Creazione della mappa di item...")

                        val itemMap = mutableMapOf<String, Int>()
                        for(item in snap.child("items").children){
                            itemMap[item.key.toString()] = item.getValue<Int>()?:0
                        }

                        cLog("Fatto! (num elementi ${itemMap.size})")

                        cLog("Creazione della lista di stati...")

                        val stateList = mutableListOf<OrderState>()
                        for(state in snap.child("stateList").children){
                            stateList.add(
                                OrderState(
                                    enumValueOf<OrderStateName>(state.key.toString()),
                                    LocalDateTime.parse(state.getValue<String>())
                                )
                            )
                        }

                        cLog("Fatto! (num stati = ${stateList.size})")

                        cLog("Creazione dell'ordine...")

                        val newOrder = Order(
                            snap.key.toString(),
                            snap.child("storeID").getValue<String>()?:"ERR",
                            snap.child("carrierID").getValue<String>()?:"ERR",
                            snap.child("customerID").getValue<String>()?:"ERR",
                            snap.child("lockerID").getValue<String>()?:"ERR",
                            itemMap.toMap(),
                            stateList.toMutableList()
                        )

                        cLog("\tNew object of Order is: ${newOrder}\n\tPutting it inside the list")

                        //newOrderList.add(newOrder)
                        if(_ordersList.value?.find { it.id == newOrder.id }!=null)
                            _ordersList.value?.remove(_ordersList.value!!.find { it.id == newOrder.id }!!)

                        _ordersList.value?.add(newOrder)

                        //_storeItems.value = _storeItems.value

                        cLog("\tList will is now be ${_ordersList.value?.size} items long and the last product added is ${_ordersList.value?.last().toString()}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DB_ORDRS", "ERROR: $error")
                }

            }
        )

        //_ordersList.value?.clear()
        //_ordersList.value = newOrderList

    }

    suspend fun initLockers() {

        fun cLog(msg: String){
            if(FB_DEBUG)
                Log.i("DB_LKRS", msg)
        }

        //var newLockersList = mutableListOf<Locker>()

        dbRefLockers.addValueEventListener(
            object : ValueEventListener {

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    cLog("DATA ADDED in LOCKERS")
                    cLog("\tReading updated lockers collection (number of children ${snapshot.childrenCount})")

                    for(snap in snapshot.children){
                        cLog("Trying to add to list the locker ${snap.key}")

                        val compsList = mutableListOf<Compartment>()
                        for(comp in snap.child("compartments").children){
                            cLog("Trying to read compartments")

                            compsList.add(
                                Compartment(
                                    comp.key?.toInt()?:0,
                                    comp.child("isOpen").getValue<Boolean>()?:false,
                                    comp.child("isEmpty").getValue<Boolean>()?:false,
                                    comp.child("isWorking").getValue<Boolean>()?:false,
                                    comp.child("isAvailable").getValue<Boolean>()?:false,
                                    comp.child("orderID").getValue<String>(),
                                )
                            )
                        }

                        val newLocker = Locker(
                            id = snap.key.toString(),
                            name = snap.child("name").getValue<String>(),
                            address = snap.child("address").getValue<String>()?:"ERR",
                            isOnline = snap.child("isOnline").getValue<Boolean>()?:false,
                            isWorking = snap.child("isWorking").getValue<Boolean>()?:false,
                            isInteracting = snap.child("isInteracting").getValue<Boolean>()?:false,
                            otp = snap.child("otp").getValue<String>()?:"9999",
                            compartments = compsList.toList()
                        )

                        cLog("\tNew object of Order is: ${newLocker}\n\tPutting it inside the list")

                        //newLockersList.add(newLocker)

                        if(_lockersList.value?.find { it.id == newLocker.id }!=null)
                            _lockersList.value?.remove(_lockersList.value!!.find { it.id == newLocker.id }!!)

                        _lockersList.value?.add(newLocker)

                        cLog("\tList will now be ${_lockersList.value?.size} items long and the last product added is ${_lockersList.value?.last().toString()}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DB_LKRS", "ERROR: $error")
                }

            }
        )

        //_lockersList.value = newLockersList
    }

    fun getUserByEmail(email : String) : User?{
        var user: User? = null

        user = _usersList.value?.find { it.email == email }

        Log.i("!!!", "Sto mandando al main l'utente ${user?.uid}")

        return user
    }

    suspend fun initStores(){
        fun cLog(msg: String){
            if(FB_DEBUG)
                Log.i("DB_STRS", msg)
        }

        dbRefStores.addValueEventListener(
            object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    cLog("DATA CHANGED in STORES")
                    cLog("\tReading updated stores collection (number of children ${snapshot.childrenCount})")

                    for(snap in snapshot.children){
                        cLog("Trying to add to list the store ${snap.key}")

                        val newStore = Store(
                            snap.key.toString(),
                            snap.child("address").getValue<String>()
                        )

                        _storesList.value?.add(newStore)

                        cLog("\tList is now ${_storesList.value?.size} items long and the last product added is ${_storesList.value?.last().toString()}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DB_STRS", "ERROR: $error")
                }

            }
        )
    }

    fun addOrder(order: Order?, userID : String?){
        fun cLog(msg: String){
            if(FB_DEBUG)
                Log.i("ADD_ORD", msg)
        }

        if(order!=null && userID!=null && order.lockerID!=null){
            cLog("\tAdding a new order into the DB")

            val orderRef = dbRefOrders.push()

            // AGGIUNGO VOCE ALL'UTENTE
            dbRefUsers.child(userID)
                .child("orders")
                .child(orderRef.key.toString())
                .setValue(
                    order.stateList?.last()?.state.toString()
                )
                .addOnSuccessListener { cLog("\tOrder ${order.id} added to user $userID") }
                .addOnFailureListener { Log.e("ADD_ORD", "FAILED TO ADD THE ORDER (# ${order.id}) TO THE USER $userID") }

            // AGGIUNGO VOCE AL LOCKER
            // Find free compartment
            var targetCompartment : Int? = _lockersList.value?.find { it.id == order.lockerID }?.compartments?.find { it.isAvailable }?.id

            if(targetCompartment!=null){
                dbRefLockers.child(order.lockerID!!)
                    .child("compartments")
                    .child(targetCompartment.toString())
                    .child("isAvailable").setValue(false)
                    .addOnSuccessListener { cLog("Modifica avvenuta con successo del campo isAvailable sul compartimento $targetCompartment del locker ${order.lockerID}") }
                    .addOnFailureListener { Log.e("ADD_ORD", "FAILED TO ADD THE ORDER TO COMPARTMENT") }

                dbRefLockers.child(order.lockerID!!)
                    .child("compartments")
                    .child(targetCompartment.toString())
                    .child("orderID").setValue(orderRef.key.toString())
                    .addOnSuccessListener { cLog("Modifica avvenuta con successo del campo orderID sul compartimento $targetCompartment del locker ${order.lockerID}") }
                    .addOnFailureListener { Log.e("ADD_ORD", "FAILED TO ADD THE ORDER TO COMPARTMENT") }
            }

            // AGGIUNGO VOCE IN ORDINI
            orderRef.setValue(
                mapOf(
                    "carrierID" to order.carrierID,
                    "customerID" to order.customerID,
                    "lockerID" to order.lockerID,
                    "storeID" to order.storeID,
                    "stateList" to (order.stateList?.map{
                        it.state.toString() to it.timestamp.toString()
                    }?.toMap() ?: "<EMPTY LIST>"),
                    "items" to (order.items?.toMap() ?: "<EMPTY LIST>")
                )
            ).addOnSuccessListener {
                Log.i("ORD_PL", "\tInserimento nel DB avvenuto correttamente")
            }.addOnFailureListener {
                Log.e("ORD_PL", "\tINSERIMENTO NEL DB FALLITO: $it")
            }


        }else{
            Log.w("ADD_ORD", "THE ORDER PASSED IS NULL!!!")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateOrderState(id: String){
        val targetOrder : Order? = _ordersList.value?.find { it.id==id }
        val lastState = targetOrder?.stateList?.last()?.state // TODO prendere quello giusto

        if(targetOrder!=null && lastState!=null){
            val nextStateName = OrderStateName.values()[lastState.ordinal+1]

            dbRefOrders.child(id)
                .child("stateList")
                .child(nextStateName.toString())
                .setValue(LocalDateTime.now().toString())
                .addOnSuccessListener { Log.i("UPDATE_ORDERSTATE","Aggiornamento dell'ordine avvenuto con successo in Order DB") }
                .addOnFailureListener { Log.e("UPDATE_ORDERSTATE", "FAILED UPDATE") }

            dbRefUsers.child(targetOrder.customerID!!)
                .child("orders")
                .child(targetOrder.id!!)
                .setValue(nextStateName.toString())
                .addOnSuccessListener { Log.i("UPDATE_ORDERSTATE","Aggiornamento dell'ordine avvenuto con successo in User DB") }
                .addOnFailureListener { Log.e("UPDATE_ORDERSTATE", "FAILED UPDATE") }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun cancelOrder(id: String){
        val targetOrder : Order? = _ordersList.value?.find { it.id==id }

        dbRefOrders.child(id)
            .child("stateList")
            .child(OrderStateName.CANCELLED.toString())
            .setValue(LocalDateTime.now().toString())
            .addOnSuccessListener { Log.i("CANCEL_ORDER","Annullamento dell'ordine con id $id") }
            .addOnFailureListener { Log.e("CANCEL_ORDER", "FAILED CANCEL") }

        if(targetOrder!=null){
            dbRefUsers.child(targetOrder.customerID!!)
                .child("orders")
                .child(targetOrder.id!!)
                .setValue(OrderStateName.CANCELLED.toString())
                .addOnSuccessListener { Log.i("CANCEL_ORDER","Annullamento dell'ordine con id $id da USERS") }
                .addOnFailureListener { Log.e("CANCEL_ORDER", "FAILED CANCEL FROM USER") }

            dbRefLockers.child(targetOrder.lockerID!!)
                .child("compartments")
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(snap in snapshot.children){
                                if(snap.child("orderID").getValue<String>()==targetOrder.id){
                                    snap.child("isAvailable").ref.setValue(true)
                                    snap.child("orderID").ref.setValue("null")
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    }
                )
        }

    }

    fun cstStartsCollection(lockerCode: String, orderID: String) : Boolean{
        Log.i("CST_COLLECTION", "Customer tries to collect goods")

        val lockerID = lockerCode.substring(0,6)
        val otp = lockerCode.substring(6)

        Log.i("CST_COLLECTION", "\tFrom QR: LockerID = $lockerID, OTP=$otp")
        Log.i("CST_COLLECTION", "\tChecking if correct locker")

        if(_ordersList.value?.find { it.id==orderID }?.lockerID != lockerID) {
            Log.w("CST_COLLECTION", "\tWrong locker (${_ordersList.value?.find { it.id==orderID }?.lockerID}) and orderID = $orderID")
            return false
        }

        Log.i("CST_COLLECTION", "\tRight locker")

        Log.i("CST_COLLECTION", "\tChecking OTP")

        if(_lockersList.value?.find{it.id == lockerID}?.otp != otp){
            Log.w("CST_COLLECTION", "\tWrong OTP")
            return false
        }

        Log.i("CST_COLLECTION", "\tRight OTP")


        Log.i("CRR_OCC_LOCK", "\tRight OTP")

        dbRefLockers.child(lockerID).child("isInteracting").setValue(true)

        //TODO apertura
        dbRefLockers.child(lockerID)
            .child("compartments")
            .addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(snap in snapshot.children){
                            if(snap.child("orderID").getValue<String>()==orderID) {
                                snap.child("isOpen").ref.setValue(true)

                                targetCompartment.value?.locker = lockerID
                                targetCompartment.value?.compartment = snap.key.toString()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO()
                    }
                }
            )

        return true
    }

    fun crrOccupyLocker(lockerCode: String, orderID: String) : Boolean{
        Log.i("CRR_OCC_LOCK", "Carrier tries to occupy locker")

        val lockerID = lockerCode.substring(0,6)
        val otp = lockerCode.substring(6)

        Log.i("CRR_OCC_LOCK", "\tFrom QR: LockerID = $lockerID, OTP=$otp")
        Log.i("CRR_OCC_LOCK", "\tChecking if correct locker")

        if(_ordersList.value?.find { it.id==orderID }?.lockerID != lockerID) {
            Log.w("CRR_OCC_LOCK", "\tWrong locker")
            return false
        }

        Log.i("CRR_OCC_LOCK", "\tRight locker")

        Log.i("CRR_OCC_LOCK", "\tChecking OTP")

        //var currOTP : String? = null

        /*
        dbRefLockers.child(lockerID)    //TODO cambiare a lettura in locale
            .child("otp")
            .get()
            .addOnSuccessListener {
                Log.i("CRR_OCC_LOCK", "\tCurrent OTP is ${it.getValue<String>()}")
                currOTP = it.getValue<String>()
            }
            .addOnFailureListener {
                Log.e("ERRORE","ERRORE $it")
            }
        */

        if(_lockersList.value?.find{it.id == lockerID}?.otp != otp){
            Log.w("CRR_OCC_LOCK", "\tWrong OTP (${_lockersList.value?.find { it.id == lockerID }?.otp})")
            return false
        }

        Log.i("CRR_OCC_LOCK", "\tRight OTP")

        var success = false
        dbRefLockers.child(lockerID).child("isInteracting").setValue(true)
            //.addOnSuccessListener { Log.i("CRR_OCC_LOCK", "\tisInteracting is now false, the locker results occupied"); success = true }
            //.addOnFailureListener { Log.e("CRR_OCC_LOCK", "\tFAILED TO OCCUPY THE LOCKER") ; success = false }

        return true
    }

    fun crrDepositing(orderID: String){
        val currOrder = _ordersList.value?.find { it.id == orderID }

        if(currOrder!=null){
            dbRefLockers.child(currOrder.lockerID!!)
                .child("compartments")
                .addListenerForSingleValueEvent(
                    object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(snap in snapshot.children){
                                if(snap.child("orderID").getValue<String>()==orderID)
                                    snap.child("isOpen").ref.setValue(true)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    }
                )
        }
    }

    // ASSIGN FUNS
    fun assignStore(): String {
        return dbRefStores.child("STO_01").key.toString()
    }

    fun assignCarrier(): String {
        //val carriersList : List<CrrUser>? = _usersList.value?.filterIsInstance<CrrUser>()
        val carriersList : List<User>? = _usersList.value?.filter { it.role==UserRole.CRR }

        return if(carriersList!=null){
            //carriersList.filter { it.isFree }.sortedBy { it.uid }[0].uid
            carriersList.first().uid
        }else{
            "ERR"
        }
    }

    data class TargetCompartment(var locker : String?, var compartment : String?)

    private val targetCompartment = MutableLiveData<TargetCompartment>()

    private val _isCompartmentOpen = MutableLiveData(false)
    val isCompartmentOpen : LiveData<Boolean> = _isCompartmentOpen

    fun startObserveCompartment() {
        if(targetCompartment.value?.locker!=null && targetCompartment.value?.compartment!=null){

            dbRefLockers.child(targetCompartment.value!!.locker!!)
                .child("compartments")
                .child(targetCompartment.value!!.compartment!!)
                .child("isOpen")
                .addValueEventListener(
                    object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            _isCompartmentOpen.value = snapshot.getValue<Boolean>()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    }
                )
        }
    }

    fun resetTargetComp(){
        targetCompartment.value?.locker = null
        targetCompartment.value?.compartment = null
    }

    // DEBUG
    fun debugResetOrders(){
        Log.w("RESET ORDERS", "RESET STARTED")

        //TODO: leggo la lista di ordini e per ognuno di esso
        _ordersList.value?.forEach {ord ->
            if(ord.id!=null) {
                if (ord.customerID != null)
                    dbRefUsers.child(ord.customerID).child(ord.id).removeValue()
                        .addOnSuccessListener {
                            Log.w("RESET ORDERS", "Order ${ord.id} succesfully removed from ${ord.customerID}")
                        }
                        .addOnFailureListener {
                            Log.e("RESET ORDERS", "ERROR WHILE REMOVING DATA! – $it")
                        }

                if (ord.lockerID!=null){
                    dbRefLockers.child(ord.lockerID!!).child("compartments").addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(snap in snapshot.children){
                                    if(snap.child("orderID").getValue<String>()==ord.id){
                                        snap.child("isAvailable").ref.setValue(true)
                                        snap.child("orderID").ref.setValue("null")
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        }
                    )
                }

                dbRefOrders.child(ord.id).removeValue()
            }
        }

        //TODO: elimino dal db l'oggetto ordine
    }

    fun DebugResetLockers(){
        //TODO
        Log.w("RESET LOCKERS", "TODO")
    }
}