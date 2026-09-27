package com.example.gerenciamento_usuarios

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppointmentRepository(private val context: Context) {

    private val gson = Gson()
    private val APPOINTMENTS_KEY = stringPreferencesKey("appointment_list")

    val appointmentsFlow: Flow<List<Appointment>> = context.dataStore.data.map { prefs ->
        val json = prefs[APPOINTMENTS_KEY] ?: "[]"
        val type = object : TypeToken<List<Appointment>>() {}.type
        gson.fromJson(json, type) ?: emptyList()
    }

    fun getAppointmentForUser(userId: String): Flow<List<Appointment>> {
        return appointmentsFlow.map { list ->
            list.filter { it.userId == userId }
        }
    }

    suspend fun addAppointment(appointment: Appointment) {
        val currentList = appointmentsFlow.first().toMutableList()
        currentList.add(appointment)
        saveList(currentList)
    }

    suspend fun updateAppointment(updatedAppointment: Appointment) {
        val currentList = appointmentsFlow.first().toMutableList()
        val index = currentList.indexOfFirst { it.id == updatedAppointment.id }
        if (index != -1) {
            currentList[index] = updatedAppointment
            saveList(currentList)
        }
    }

    suspend fun deleteAppointment(appointmentId: String) {
        val currentList = appointmentsFlow.first().toMutableList()
        currentList.removeAll { it.id == appointmentId }
        saveList(currentList)
    }

    private suspend fun saveList(list: List<Appointment>) {
        val json = gson.toJson(list)
        context.dataStore.edit { prefs ->
            prefs[APPOINTMENTS_KEY] = json
        }
    }

}