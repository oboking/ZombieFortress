package com.oscarboking.zombiefortress

/**
 * Created by boking on 2017-08-15.
 */
class Item(name:String,quantity:Int) {
    private var name : String
    private var quantity : Int
    init {
        this.name=name
        this.quantity=quantity
    }
    fun getName():String{
        return name
    }
    fun getQuantity():Int{
        return quantity
    }

    fun increaseQuantity(amount : Int){
        quantity+=amount
    }
    fun decreaseQuantity(amount : Int){
        quantity-=amount
    }
}