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
import it.polito.did.g2.shopdrop.data.UserQuery
import it.polito.did.g2.shopdrop.data.UserRole
import java.time.LocalDateTime

class MainViewModel : ViewModel(){
    //TODO

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

        return result
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
}