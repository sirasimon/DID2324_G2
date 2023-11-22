package it.polito.did.s306067.lab04

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import it.polito.did.s306067.lab04.classes.User

val UID = "0x11F"

class MainViewModel : ViewModel() {
    var user : User? = null

    val nickname = MutableLiveData<String>("[nickname here]")
    val email = MutableLiveData<String>("[email here]")
    //var _testText = "[test here]"
    val testText = MutableLiveData("[test here]")

    //DB REFERENCES
    val dbRef = Firebase.database.reference
    val users = Firebase.database.reference.child("users")
    val orders = Firebase.database.reference.child("orders")
    val lockers = Firebase.database.reference.child("lockers")

    fun startConnection(){
        Log.i("DB CON", "Start connection")

        dbRef.child("Test").get().addOnSuccessListener {
            //_testText = it.getValue<String>() ?: "è nullo!"
            testText.value = it.getValue<String>() ?: "è nullo!"
            Log.i("DB CON", "Got value of Test: ${it.value}")
            //testText.value=_testText
            Log.i("DB CON", "while ${testText.value}")
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


    fun getUID() : String{
        return UID
    }
}