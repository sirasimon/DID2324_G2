package it.polito.did.s306067.lab04

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import com.google.firebase.database.values

val UID = "0x11F"

@IgnoreExtraProperties
data class User(
    val nickname : String,
    val email : String
)

class MainViewModel : ViewModel() {
    val nickname = MutableLiveData<String>("[email here]")
    val email = MutableLiveData<String>("[email here]")
    //var _testText = "[test here]"
    val testText = MutableLiveData("[test here]")

    //DB REFERENCES
    val dbRef = Firebase.database.reference
    val usrsDBRef = Firebase.database.reference.child("users")
    val ordsDBRef = Firebase.database.reference.child("orders")
    val lcksDBRef = Firebase.database.reference.child("lockers")

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


        /*
        dbRef.child("Test").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    _testText = snapshot.getValue<String>() ?: "null"
                    testText.value = _testText
                    Log.i("DB CON", "Data fetched ${testText.value}")
                }else{
                    Log.w("DB CON", "Non data fetched")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
         */

        usrsDBRef.child(UID).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("DB CON", "$snapshot")

                //val userData = snapshot.getValue<User>()

                //nickname.value = userData?.nickname
                //email.value = userData?.email
                email.value = snapshot.child("email").getValue<String>()
                nickname.value = snapshot.child("nickname").getValue<String>()
                Log.i("DB CON", "\tNickname is ${nickname.value}")
                Log.i("DB CON", "\tEmail is ${email.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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