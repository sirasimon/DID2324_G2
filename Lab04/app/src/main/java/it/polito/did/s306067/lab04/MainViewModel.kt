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
import com.google.firebase.database.snapshots
import com.google.firebase.database.values

val UID = "0x11F"

class MainViewModel : ViewModel() {
    val nickname = MutableLiveData<String>()
    val email = MutableLiveData<String>("[email here]")
    var _testText = "[test here]"
    val testText = MutableLiveData<String>()

    //DB REFERENCES
    val dbRef = Firebase.database.reference
    val usrsDBRef = Firebase.database.reference.child("users")
    val ordsDBRef = Firebase.database.reference.child("orders")
    val lcksDBRef = Firebase.database.reference.child("lockers")

    fun startConnection(){
        Log.i("DB CON", "Start connection")

        dbRef.child("Test").get().addOnSuccessListener {
            _testText = it.getValue<String>() ?: "è nullo!"
            Log.i("DB CON", "Got value of Test: ${it.value}")
            Log.i("DB CON", "_ is ${_testText}")
            testText.value=_testText
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
    }


    fun getUID() : String{
        return UID
    }
}