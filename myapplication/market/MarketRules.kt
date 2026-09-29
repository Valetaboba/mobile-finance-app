package com.example.myapplication.market



enum class PurchaseStatus { PURCHASED, ALREADY_OWNED, NOT_ENOUGH_MONEY, INVALID_PRODUCT }

object MarketRules {
    fun checkPurchase(balance: Int, owned: Set<String>, product: MarketProduct): PurchaseStatus = when {
        product.id.isBlank() || product.price < 0 -> PurchaseStatus.INVALID_PRODUCT
        product.id in owned -> PurchaseStatus.ALREADY_OWNED
        balance < product.price -> PurchaseStatus.NOT_ENOUGH_MONEY
        else -> PurchaseStatus.PURCHASED
    }
}
