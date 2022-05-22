package ikhwan.binar.myroomdatabase.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(context: Context) {
    private val dataStore : DataStore<Preferences> = context.createDataStore(name = "user_prefs")

    suspend fun saveData(email:String){
        dataStore.edit {
            it[EMAIL] = email
        }
    }

    val email : Flow<String> = dataStore.data.map {
        it[EMAIL] ?: ""
    }

    companion object{
        val EMAIL = preferencesKey<String>("USER_EMAIL")
    }
}