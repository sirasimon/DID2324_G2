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
                        /*
                        when(UserRole.valueOf(snap.child("role").getValue<String>()?:"NUL")){
                            UserRole.ADM -> {
                                cLog("\tNew user has role ADM and id ${snap.key.toString()}")

                                newUser = AdmUser(
                                    uid = snap.key.toString(),
                                    email = snap.child("email").getValue<String>()?:"ERR",
                                    password = snap.child("password").getValue<String>()?:"ERR",
                                    name = snap.child("name").getValue<String>()?:"ERR",
                                    role = UserRole.valueOf(snap.child("role").getValue<String>()?:"ADM"),
                                )

                                cLog("\tNew user is $newUser")
                            }

                            UserRole.CST -> {
                                cLog("\tNew user has role CST")

                                cLog("\tPreparing list of orders...")

                                val orderMap = mutableMapOf<String, OrderStateName>()
                                for(order in snap.child("orders").children){
                                    orderMap[order.key.toString()] = OrderStateName.valueOf(order.getValue<String>()?:"ERROR")
                                }
                                cLog("\tOrder list is ${orderMap.size} item long")

                                newUser = CstUser(
                                    uid = snap.key.toString(),
                                    email = snap.child("email").getValue<String>()?:"ERR",
                                    password = snap.child("password").getValue<String>()?:"ERR",
                                    name = snap.child("name").getValue<String>()?:"ERR",
                                    role = UserRole.valueOf(snap.child("role").getValue<String>()?:"ADM"),
                                    orders = orderMap.toMap(),
                                    isBanned = snap.child("isBanned").getValue<Boolean>()?:false,
                                )

                                cLog("\tNew user is $newUser")
                            }

                            UserRole.CRR -> {
                                cLog("\tNew user has role CRR")

                                newUser = CrrUser(
                                    uid = snap.key.toString(),
                                    email = snap.child("email").getValue<String>()?:"ERR",
                                    password = snap.child("password").getValue<String>()?:"ERR",
                                    name = snap.child("name").getValue<String>()?:"ERR",
                                    role = UserRole.valueOf(snap.child("role").getValue<String>()?:"ADM"),
                                    isFree = snap.child("isFree").getValue<Boolean>()?:false
                                )

                                cLog("\tNew user is $newUser")
                            }

                            else -> {
                                newUser = null
                            }
                        }
                        */

                        if(newUser!=null){
                            _usersList.value?.add(newUser)
                            cLog("\tAdded new user to list: ${_usersList.value?.last()?.name.toString()}")
                        }else{
                            Log.w("DB_USRS", "CONVERSIONE ANDATA STORTO")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DB_USRS", "ERROR: $error")
                }
            }
        )
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

                        _ordersList.value?.add(newOrder)
                        //_storeItems.value = _storeItems.value

                        cLog("\tList is now ${_ordersList.value?.size} items long and the last product added is ${_ordersList.value?.last().toString()}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DB_ORDRS", "ERROR: $error")
                }

            }
        )
    }

    suspend fun initLockers() {

        fun cLog(msg: String){
            if(FB_DEBUG)
                Log.i("DB_LKRS", msg)
        }

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
                            compartments = compsList.toList()
                        )

                        cLog("\tNew object of Order is: ${newLocker}\n\tPutting it inside the list")

                        _lockersList.value?.add(newLocker)

                        cLog("\tList is now ${_lockersList.value?.size} items long and the last product added is ${_lockersList.value?.last().toString()}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DB_LKRS", "ERROR: $error")
                }

            }
        )
    }

    fun getUserByEmail(email : String) : User?{
        var user: User? = null

        /*
        dbRefUsers.addListenerForSingleValueEvent(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snap in snapshot.children){
                        if(snap.child("email").getValue<String>()==email){
                            Log.i("getUbE", "TROVATO!")

                            user = User(
                                uid = snap.key.toString(),
                                email = snap.child("email").getValue<String>() ?: "ERR",
                                password = snap.child("password").getValue<String>()
                                    ?: "ERR",
                                name = snap.child("name").getValue<String>() ?: "ERR",
                                role = UserRole.CST
                            )

                            /*
                            when(UserRole.valueOf(snap.child("role").getValue<String>()!!)){
                                UserRole.CST -> {
                                    val orderMap = mutableMapOf<String, OrderStateName>()
                                    for(order in snap.child("orders").children){
                                        orderMap[order.key.toString()] = OrderStateName.valueOf(order.getValue<String>()?:"ERROR")
                                    }

                                    user = CstUser(
                                        uid = snap.key.toString(),
                                        email = snap.child("email").getValue<String>() ?: "ERR",
                                        password = snap.child("password").getValue<String>()
                                            ?: "ERR",
                                        name = snap.child("name").getValue<String>() ?: "ERR",
                                        role = UserRole.CST,
                                        orders = orderMap.toMap(),
                                        isBanned = snap.child("isBanned").getValue<Boolean>()?:false
                                    )
                                }

                                UserRole.CRR -> {
                                    user = CrrUser(
                                        uid = snap.key.toString(),
                                        email = snap.child("email").getValue<String>()?:"ERR",
                                        password = snap.child("password").getValue<String>()?:"ERR",
                                        name = snap.child("name").getValue<String>()?:"ERR",
                                        role = UserRole.valueOf(snap.child("role").getValue<String>()?:"ADM"),
                                        isFree = snap.child("isFree").getValue<Boolean>()?:false
                                    )
                                }
                                UserRole.ADM -> {
                                    user = AdmUser(
                                        uid = snap.key.toString(),
                                        email = snap.child("email").getValue<String>()?:"ERR",
                                        password = snap.child("password").getValue<String>()?:"ERR",
                                        name = snap.child("name").getValue<String>()?:"ERR",
                                        role = UserRole.valueOf(snap.child("role").getValue<String>()?:"ADM"),
                                    )
                                }
                                UserRole.NUL -> {
                                    user = null
                                }
                            }
                             */

                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
        )

        */

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

        if(order!=null && userID!=null){
            cLog("\tAdding a new order into the DB")

            val orderRef = dbRefOrders.push()

            dbRefUsers.child(userID)
                .child("orders")
                .setValue(
                    mapOf(
                        orderRef.key to order.stateList?.last()?.state.toString()
                    )
                )
                .addOnSuccessListener { cLog("\tOrder ${order.id} added to user $userID") }
                .addOnFailureListener { Log.e("ADD_ORD", "FAILED TO ADD THE ORDER (# ${order.id}) TO THE USER $userID") }

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
        val currState : OrderState? = ordersList.value?.find { it.id==id }?.stateList?.last()

        if(currState!=null){
            val nextStateName = OrderStateName.values()[currState.state.ordinal+1]


            dbRefOrders.child(id).child("stateList").setValue(mapOf(nextStateName.toString() to LocalDateTime.now().toString()))
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