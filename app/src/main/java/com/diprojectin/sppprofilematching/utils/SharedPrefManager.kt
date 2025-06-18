package com.diprojectin.sppprofilematching.utils

import android.content.Context
import com.diprojectin.models.User
import com.google.gson.Gson

class SharedPrefManager(context: Context) {

    private val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveUser(dataLogin: User) {
        val json = gson.toJson(dataLogin)
        prefs.edit().putString(Constant.dataLogin, json).apply()
    }

    fun getUser(): User? {
        val json = prefs.getString(Constant.dataLogin, null)
        return if (json != null) gson.fromJson(json, User::class.java) else null
    }

    fun clearUser() {
        prefs.edit().remove(Constant.dataLogin).apply()
    }

    fun isOnboarding() = prefs.getBoolean(Constant.onboarding, false)

    fun setOnboarding(isOnboarded: Boolean) {
        prefs.edit().putBoolean(Constant.onboarding, isOnboarded).apply()
    }

    fun isSaveAccount() = prefs.getBoolean(Constant.saveAccount, false)

    fun getUsername() = prefs.getString(Constant.username,"")

    fun setSaveAccount(isSaveAccount: Boolean, username: String) {
        prefs.edit().putBoolean(Constant.saveAccount, isSaveAccount).apply()
        prefs.edit().putString(Constant.username, username).apply()
    }

    fun clearSaveAccount() {
        prefs.edit().remove(Constant.saveAccount).apply()
        prefs.edit().remove(Constant.username).apply()
    }
}