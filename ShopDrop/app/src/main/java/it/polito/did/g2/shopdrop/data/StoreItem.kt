package it.polito.did.g2.shopdrop.data

data class StoreItem(
    val name: String,
    val price: Float,
    val category: StoreItemCategory?,
    val thumbnail: String //Bitmap?
)
