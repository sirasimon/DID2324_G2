package it.polito.did.g2.shopdrop.data.users

class UserCst(uid: String, name: String, password: String, role: UserRole, isBanned:Boolean) : User(uid, name, password, role)