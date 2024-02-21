package it.polito.did.g2.shopdrop.data

class Fees {
    companion object {
        const val SHIPMENT = 2.5f
        const val SERVICE =  0.0f

        fun total(): Float{
            return SERVICE + SHIPMENT
        }
    }
}