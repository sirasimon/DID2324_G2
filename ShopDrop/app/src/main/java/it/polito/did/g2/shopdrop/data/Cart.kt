package it.polito.did.g2.shopdrop.data

class Cart {
    var items: MutableMap<String, Int> = mutableMapOf()
        private set
    var totalItems: Int = 0
        private set
    var subtot: Float = 0f
        private set

    fun modify(item: StoreItem, quantity: Int = 1){
        val delta = quantity - (items[item.name]?:0)

        items[item.name] = quantity
        totalItems += delta
        subtot += item.price*delta
    }
}