package it.polito.did.g2.shopdrop.data.users

class UserCrr(uid: String, name: String, password: String, role: UserRole, private var isFree:Boolean = false) : User(uid, name, password, role){
    fun setAvailablity(isAvailable: Boolean = true){
        isFree = isAvailable
    }

    fun getAvailability(): Boolean{
        return isFree
    }
}