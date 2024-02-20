package it.polito.did.g2.shopdrop

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import it.polito.did.g2.shopdrop.data.Compartment
import it.polito.did.g2.shopdrop.data.Locker
import it.polito.did.g2.shopdrop.data.Order
import it.polito.did.g2.shopdrop.data.OrderState
import it.polito.did.g2.shopdrop.data.OrderStateName
import it.polito.did.g2.shopdrop.data.Store
import it.polito.did.g2.shopdrop.data.StoreItem
import it.polito.did.g2.shopdrop.data.StoreItemCategory
import it.polito.did.g2.shopdrop.data.users.AdmUser
import it.polito.did.g2.shopdrop.data.users.CrrUser
import it.polito.did.g2.shopdrop.data.users.CstUser
import it.polito.did.g2.shopdrop.data.users.User
import it.polito.did.g2.shopdrop.data.users.UserRole
import java.time.LocalDateTime

//DEBUG
const val FB_DEBUG = true

class FirebaseRepository {
    //DB REFs
    private val dbRef = Firebase.database.reference
    private val debugRef = dbRef.child("debug")
    private val dbRefLockers = dbRef.child("lockers")
    private val dbRefUsers = dbRef.child("users")
    private val dbRefOrders = dbRef.child("orders")
    private val dbRefProducts = dbRef.child("products")
    private val dbRefStores = dbRef.child("stores")

    //DATA RETRIVED
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

    fun initUsers(){
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

                        when(UserRole.valueOf(snap.child("role").getValue<String>()?:"NUL")){
                            UserRole.ADM -> {
                                newUser = AdmUser(
                                    uid = snap.key.toString(),
                                    email = snap.child("email").getValue<String>()?:"ERR",
                                    password = snap.child("password").getValue<String>()?:"ERR",
                                    name = snap.child("name").getValue<String>()?:"ERR",
                                    role = UserRole.valueOf(snap.child("role").getValue<String>()?:"ADM"),
                                )
                            }

                            UserRole.CST -> {
                                val orderMap = mutableMapOf<String, OrderStateName>()
                                for(order in snap.child("orders").children){
                                    orderMap[order.key.toString()] = OrderStateName.valueOf(order.getValue<String>()?:"ERROR")
                                }

                                newUser = CstUser(
                                    uid = snap.key.toString(),
                                    email = snap.child("email").getValue<String>()?:"ERR",
                                    password = snap.child("password").getValue<String>()?:"ERR",
                                    name = snap.child("name").getValue<String>()?:"ERR",
                                    role = UserRole.valueOf(snap.child("role").getValue<String>()?:"ADM"),
                                    orders = orderMap.toMap(),
                                    isBanned = snap.child("isBanned").getValue<Boolean>()?:false,
                                )
                            }

                            UserRole.CRR -> {
                                newUser = CrrUser(
                                    uid = snap.key.toString(),
                                    email = snap.child("email").getValue<String>()?:"ERR",
                                    password = snap.child("password").getValue<String>()?:"ERR",
                                    name = snap.child("name").getValue<String>()?:"ERR",
                                    role = UserRole.valueOf(snap.child("role").getValue<String>()?:"ADM"),
                                    isFree = snap.child("isFree").getValue<Boolean>()?:false
                                )
                            }

                            else -> {
                                newUser = null
                            }
                        }

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

    fun initProducts() {

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

    fun initOrders() {

        fun cLog(msg: String){
            if(FB_DEBUG)
                Log.i("DB_ORDRS", msg)
        }

        dbRefOrders.addValueEventListener(
            object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    cLog("DATA CHANGED in ORDERS")
                    cLog("\tReading updated orders collection (number of children ${snapshot.childrenCount})")

                    for(snap in snapshot.children){
                        cLog("Trying to add to list the order ${snap.key}")

                        val itemMap = mutableMapOf<String, Int>()
                        for(item in snap.child("items").children){
                            itemMap[item.key.toString()] = item.getValue<Int>()?:0
                        }

                        val stateList = mutableListOf<OrderState>()
                        for(state in snap.child("stateList").children){
                            stateList.add(
                                OrderState(
                                    enumValueOf<OrderStateName>(state.key.toString()),
                                    LocalDateTime.parse(state.getValue<String>())
                                )
                            )
                        }

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

    fun initLockers() {

        fun cLog(msg: String){
            if(FB_DEBUG)
                Log.i("DB_LKRS", msg)
        }

        dbRefLockers.addValueEventListener(
            object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    cLog("DATA CHANGED in LOCKERS")
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

    fun initStores(){
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
}