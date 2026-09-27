package com.example.gerenciamento_usuarios

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserRepository(private val context: Context) {
    private val gson = Gson()
    private val USERS_KEY = stringPreferencesKey("users_list")

    // Obtém o Fluxo contínuo da lista de usuários
    val usersFlow: Flow<List<User>> = context.dataStore.data.map { prefs ->
        val json = prefs[USERS_KEY] ?: "[]"
        val type = object : TypeToken<List<User>>() {}.type
        gson.fromJson(json, type) ?: emptyList()
    }

    suspend fun addUser(user: User) {
        val currentList = usersFlow.first().toMutableList()
        currentList.add(user)
        saveList(currentList)
    }

    suspend fun updateUser(updatedUser: User) {
        val currentList = usersFlow.first().toMutableList()
        val index = currentList.indexOfFirst { it.id == updatedUser.id }
        if (index != -1) {
            currentList[index] = updatedUser
            saveList(currentList)
        }
    }

    suspend fun deleteUser(userId: String) {
        val currentList = usersFlow.first().toMutableList()
        currentList.removeAll { it.id == userId }
        saveList(currentList)
    }

    private suspend fun saveList(list: List<User>) {
        val json = gson.toJson(list)
        context.dataStore.edit { prefs ->
            prefs[USERS_KEY] = json
        }
    }
}
