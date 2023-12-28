package it.polito.did.g2.shopdrop

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import it.polito.did.g2.shopdrop.data.Locker
import it.polito.did.g2.shopdrop.data.Order
import it.polito.did.g2.shopdrop.data.OrderState
import it.polito.did.g2.shopdrop.data.OrderStateName
import it.polito.did.g2.shopdrop.data.StoreItem
import it.polito.did.g2.shopdrop.data.StoreItemCategory
import it.polito.did.g2.shopdrop.data.UserQuery
import it.polito.did.g2.shopdrop.data.UserRole
import java.time.LocalDateTime

class MainViewModel() : ViewModel(){
    //TODO
    val shipmentFee = 2.5
    val serviceFee = 0.0


    //CART DATA
    private val _cart : MutableLiveData<MutableMap<String, Int>> = MutableLiveData(mutableMapOf())
    val cart : LiveData<MutableMap<String, Int>> = _cart

    private val _subtot : MutableLiveData<Float> = MutableLiveData(.0f )
    val subtot : LiveData<Float> = _subtot

    //SHARED PREFs
    //private val sharedPreferences = application.getSharedPreferences("shopdrop_pref", Context.MODE_PRIVATE)

    //DB REFs
    private val dbRef = Firebase.database.reference
    private val debugRef = dbRef.child("debug")
    private val lockersRef = dbRef.child("lockers")
    private val usersRef = dbRef.child("users")
    private val ordersRef = dbRef.child("orders")
    private val productsRef = dbRef.child("products")
    private val storesRef = dbRef.child("stores")

    //USER INFO
    private val _userID : MutableLiveData<String?> = MutableLiveData(null)
    val userID : LiveData<String?> = _userID

    //ORDERS
    private val _hasPending : MutableLiveData<Boolean> = MutableLiveData(true)
    val hasPending : LiveData<Boolean> = _hasPending

    var targetOrderID : String? = null

    //LOCKERS
    private val _lockers = MutableLiveData<MutableList<Locker>>()
    val lockers : LiveData<MutableList<Locker>> = _lockers

    //TODO Adesso HARDCODED ma da fare su server
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
    val pendingOrdersList : LiveData<MutableList<Order>> = _pendingOrdersList

    //PRODUCTS
    private val _storeItems = MutableLiveData<MutableList<StoreItem>>(mutableListOf())
    val storeItems :LiveData<MutableList<StoreItem>> = _storeItems


    //DEBUG

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

    private val users = dbRef.child("users")

    //LOGIN
    fun login(userQuery: UserQuery) : Boolean{
        var result = false

        when(userQuery.role){
            UserRole.CRR -> {
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

                                        Log.i("LOGIN", "FOUND ${_userID.value}")
                                    }
                                }else{
                                    Log.e("LOGIN", "FOR EMAIL ${userQuery.email} COUNTED ${dataSnapshot.children.count()} OCCURRENCES")

                                    result = false
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Gestisci eventuali errori
                                println("Errore nella query: $databaseError")
                            }
                        }
                    )
            }
            else -> {
                //TODO
            }
        }

        if(_userID.value!=null){
            users.child(_userID.value!!)
                .child("password")
                .addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            result = userQuery.password==dataSnapshot.value.toString()

                            Log.i("LOGIN", "PASSWORD IS ${if(result) "CORRECT" else "INCORRECT"} ($result)")
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Gestisci eventuali errori
                            result = false
                            println("Errore nella query: $databaseError")
                        }
                    }
                )
        }

        /*
        if(result){
            viewModelScope.launch {
                //sharedPreferences.edit().putString("userID", _userID.value).apply()
            }
        }
         */

        return result
    }

    fun loadUserInfo() : String?{
        /*
        viewModelScope.launch {
            //sharedPreferences.getString("userID", _userID.value)
        }
         */

        return _userID.value
    }

    //DEBUG FUNS
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

    init{
        productsRef.addListenerForSingleValueEvent(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.i("PROD_INIT", "INITIALIZING Products list")
                    for(snap in snapshot.children){
                        Log.i("PROD_INIT", "\tTrying to add to list the product ${snap.key}")
                        Log.i("PROD_INIT", "\tThe new item should contain:\n\tname: ${snap.key}\n" +
                                "\tprice: ${snap.child("price").getValue<Float>()}\n" +
                                "\tcategory: ${StoreItemCategory.valueOf(snap.child("category").getValue<String>()?.uppercase()?:"<null>").toString()}\n" +
                                "\tthumbnail: ${snap.child("thumbnail").getValue<String>()}")

                        val newItem = StoreItem(
                            snap.key?:"<null>",
                            snap.child("price").getValue<Float>()?:.0f,
                            StoreItemCategory.valueOf(snap.child("category").getValue<String>()?.uppercase()?:"<null>"),
                            snap.child("thumbnail").getValue<String>()?:"<null>"
                        )

                        Log.i("PROD_INIT", "\tNew object of StoreItem is: ${newItem}\n\tPutting it inside the list")

                        _storeItems.value?.add(newItem)
                        //_storeItems.value = _storeItems.value

                        Log.i("PROD_INIT", "\tList is now ${_storeItems.value?.size} items long and the last product added is ${_storeItems.value?.last()}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
        )
    }

    fun modifyCart(item: StoreItem?, quantity: Int){
        Log.i("MODIFY_CART", "Trying to modify the cart")

        if(item!=null){
            if(_cart.value!!.contains(item.name)){
                Log.i("MODIFY_CART", "\t\"${item.name}\" already in the cart with quantity ${_cart.value!![item.name]}, but willing to change into $quantity")
                _cart.value!![item.name] = quantity
            }else{
                Log.i("MODIFY_CART", "\t\"${item.name}\" is not in the cart yet")
                _cart.value!![item.name] = quantity
            }
            Log.i("MODIFY_CART", "\tUpdated quantity of \"${item.name}\" in map is ${_cart.value!![item.name]} (desired change was $quantity)")

            updateSubtot()
        }else{
            Log.w("MODIFY_CART", "\tITEM IS NULL")
        }
    }

    fun updateSubtot(){
        Log.i("UPD_SUBTOT", "UPDATING SUBTOTAL")

        var subtot = 0f

        _cart.value?.forEach {
            subtot += (it.value.toFloat()*(_storeItems.value?.find{storeItem ->  storeItem.name==it.key}?.price?:.0f))
        }

        _subtot.value = subtot

        Log.i("UPD_SUBTOT", "\tNew subtotal is ${_subtot.value}")
    }
}