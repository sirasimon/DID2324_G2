package it.polito.did.g2.shopdrop.data

import it.polito.did.g2.shopdrop.R

enum class StoreItemCategory {
    FORNO,
    ORTOFRUTTA,
    GASTRONOMIA,
    SCATOLAME,
    NEW,   // convert to tags
    PROMO; // convert to tags

    fun getStringRef(): Int {
        return when (this){
            FORNO -> R.string.store_it_cat_bakery
            ORTOFRUTTA -> R.string.store_it_cat_fruitveg
            GASTRONOMIA -> R.string.store_it_cat_gastronomy
            SCATOLAME -> R.string.store_it_cat_canned
            NEW -> R.string.store_it_cat_new
            PROMO -> R.string.store_it_cat_promo
        }
    }
}