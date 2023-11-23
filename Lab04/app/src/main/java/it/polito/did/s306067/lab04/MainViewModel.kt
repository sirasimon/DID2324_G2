package it.polito.did.s306067.lab04

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import it.polito.did.s306067.lab04.classes.Order
import it.polito.did.s306067.lab04.classes.User

val UID = "0x11F"

class MainViewModel : ViewModel() {
    var user : User? = null

    private val _testText : MutableLiveData<String> = MutableLiveData("[test here]")
    val testText : LiveData<String> = _testText

    private val _nickname : MutableLiveData<String> = MutableLiveData("[nickname here]")
    val nickname : LiveData<String> = _nickname

    private val _email : MutableLiveData<String> = MutableLiveData("[email here]")
    val email : LiveData<String> = _email

    private val _ordersList : MutableLiveData<MutableList<Order>?> = MutableLiveData(null)
    //val ordersList : LiveData<MutableList<Order>>? = _ordersList

    //DB REFERENCES
    //val dbRef = Firebase.database.reference
    //val users = Firebase.database.reference.child("users")
    //val orders = Firebase.database.reference.child("orders")
    val lockers = Firebase.database.reference.child("lockers")


    private val dbRef = Firebase.database.reference.addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("DB CON", "Listening to value in dbRef")
                _testText.value = snapshot.child("Test").getValue<String>()
                Log.i("DB CON", "\t_testText.value is ${_testText.value}")
                Log.i("DB CON", "\ttestText.value is ${testText.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                Log.i("DB CON", "CANCELLED")
            }
        }
    )


    private val users = Firebase.database.reference.child("users").addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("USERS CON", "Listening to value in users")
                _nickname.value = snapshot.child(UID).child("nickname").getValue<String>()
                Log.i("USERS CON", "\t_nickname.value is ${_nickname.value}")
                Log.i("USERS CON", "\tnickname.value is ${nickname.value}")

                _email.value = snapshot.child(UID).child("email").getValue<String>()
                Log.i("USERS CON", "\t_email.value is ${_email.value}")
                Log.i("USERS CON", "\temail.value is ${email.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                Log.i("DB CON", "CANCELLED")
            }
        }
    )

    private val orders = Firebase.database.reference.child("orders").addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("ORDERS CON", "Listening to value in orders")
                //Looking for orders for UID

                var prova : Order? = null

                val buffer = mutableListOf<Order>()

                for(snap in snapshot.children){
                    Log.i("ORDERS CON", "ENTRATO nel FOR")
                    if(snap.child("addresseeID").getValue<String>()==UID){
                        Log.i("ORDERS CON", "ENTRATO nell'IF")
                        Log.i("ORDERS CON", "\t${snap.key}, ${snap.child("insertionCode").getValue<String>()}, ${snap.child("gatheringCode").getValue<String>()}")

                        prova = Order(
                            orderID = snap.key,
                            states = null,
                            creationTime = null,
                            lastUpdateTime = null,
                            senderID = null,
                            addresseeID = UID,
                            lockerID = null,
                            insertionCode = snap.child("insertionCode").getValue<String>(),
                            gatheringCode = snap.child("gatheringCode").getValue<String>()
                        )

                        buffer.add(prova)
                    }
                }

                _ordersList.value = buffer

                Log.i("ORDERS CON", "\t${_ordersList.value}")
                //println("")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                Log.i("DB CON", "CANCELLED")
            }
        }
    )


    /*
    fun startConnection(){
        Log.i("DB CON", "Start connection")

        dbRef.child("Test").get().addOnSuccessListener {
            //_testText = it.getValue<String>() ?: "è nullo!"
            _testText.value = it.getValue<String>() ?: "è nullo!"
            Log.i("DB CON", "Got value of Test: ${it.value}")
            //testText.value=_testText
            Log.i("DB CON", "while ${_testText.value}")
        }.addOnFailureListener{
            Log.e("DB CON", "Error getting data", it)
        }

        Log.i("DB CON", "Now reading UID child")

        users.child(UID).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                Log.i("DB CON", "$snapshot")

                email.value = snapshot.child("email").getValue<String>()
                var em = email.value?:""

                nickname.value = snapshot.child("nickname").getValue<String>()
                var nick = nickname.value?:""

                Log.i("DB CON", "\tNickname is ${nickname.value}")
                Log.i("DB CON", "\tEmail is ${email.value}")

                var pending = snapshot.child("pending").value
                Log.i("DB CON", "\tPending is $pending")

                //user = User(UID, nick, em, pending)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        val key = dbRef.child("users").push().key

        if(key == null)
            Log.i("DB CON", "Chiave NON creata")
        else
            Log.i("DB CON", "Chiave creata ($key)")

        if(key!=null){
            val newUser = User(key,"sirasimo", "sira.simon@icloud.com", null)

            val usrRef = dbRef.child("users").child(key)

            usrRef.child("nickname").setValue(newUser.nickname).addOnSuccessListener {
                Log.i("DB CON", "Ce l'ho fatta?")
            }.addOnFailureListener {
                Log.i("DB CON", "MA KOTLIN MALEDETTO")
            }
        }


        //nickname.value = users.child(UID).child("nickname").key

        /*
        usrsDBRef.child(UID).child("email").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("DB CON", "$snapshot")

                //val userData = snapshot.getValue<User>()

                //nickname.value = userData?.nickname
                //email.value = userData?.email
                email.value = snapshot.getValue<String>()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        usrsDBRef.child(UID).child("nickname").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("DB CON", "$snapshot")

                nickname.value = snapshot.getValue<String>()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        usrsDBRef.child(UID).get().addOnSuccessListener {
            Log.i("DB CON", "$it")

        }.addOnFailureListener{
            Log.e("DB CON", "Error getting data", it)
        }

         */
    }

     */


    fun getUID() : String{
        return UID
    }
}