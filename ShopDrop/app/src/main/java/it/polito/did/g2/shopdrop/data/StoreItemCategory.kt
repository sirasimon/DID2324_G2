package it.polito.did.g2.shopdrop.data

import it.polito.did.g2.shopdrop.R

enum class StoreItemCategory {
    BAKERY,
    FRUITVEG,
    NEW,   // convert to tags
    PROMO; // convert to tags

    fun getStringRef(): Int {
        return when (this){
            BAKERY -> R.string.store_it_cat_bakery
            FRUITVEG -> R.string.store_it_cat_fruitveg
            NEW -> R.string.store_it_cat_new
            PROMO -> R.string.store_it_cat_promo
        }
    }
}