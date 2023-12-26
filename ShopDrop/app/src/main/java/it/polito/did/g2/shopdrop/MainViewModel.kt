package it.polito.did.g2.shopdrop

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

class MainViewModel : ViewModel(){
    //TODO

    //DB REF
    private val dbRef = Firebase.database.reference

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