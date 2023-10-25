package com.example.listaspesa

enum class Tag{
    NONE, SURGELATI, FORNO, GASTRONOMIA, SCATOLAME, CASALINGHI, DETERSIVI, PESCHERIA, LATTICINI
}

data class PurchasableItem (var description : String, var tag : Tag, var purchased : Boolean){

}