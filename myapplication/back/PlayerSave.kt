package com.example.myapplication.back

import android.content.Context

/** Wallet, purchases and equipment are written together; dialogue saves cannot overwrite them. */
internal class PlayerSave(context: Context) {
    private val preferences = (context.applicationContext ?: context).getSharedPreferences("player_state", Context.MODE_PRIVATE)
    fun int(key: String, default: Int) = preferences.getInt(key, default)
    fun bool(key: String) = preferences.getBoolean(key, false)
    fun ids(key: String): Set<String> = preferences.getStringSet(key, emptySet())!!.toSet()
    fun write(state: GameState) {
        preferences.edit()
            .putInt("money", state.money.intValue)
            .putInt("food", state.food.intValue)
            .putInt("weariness", state.weariness.intValue)
            .putInt("savings", state.savingsBalance.intValue)
            .putInt("deposit", state.depositBalance.intValue)
            .putInt("deposit_days", state.depositDaysLeft.intValue)
            .putBoolean("deposit_active", state.depositActive.value)
            .putStringSet("owned", state.ownedProductIds)
            .putStringSet("equipped", state.equippedProductIds)
            .putString("last_session", state.lastSettledSession)
            .putBoolean("inventory_migrated", state.inventoryMigrated)
            .apply()
    }
    fun lastSession(): String? = preferences.getString("last_session", null)
}
