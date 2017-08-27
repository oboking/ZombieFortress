package com.oscarboking.zombiefortress

import java.io.Serializable

/**
 * Created by boking on 2017-08-15.
 */
class Item(name:String,quantity:Int) :Serializable{
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
        if(quantity<0)
            quantity=0
    }
}