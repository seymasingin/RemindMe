package com.seymasingin.remindme.data.repos

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.util.Constants.PREFERENCE_KEY
import com.seymasingin.remindme.util.Constants.PREFERENCE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferenceKeys {
        val sortState = stringPreferencesKey(name = PREFERENCE_KEY)
    }

    private val datastore = context.datastore

    suspend fun persistSortState(priority: Priority){
        datastore.edit {preference ->
                preference[PreferenceKeys.sortState] = priority.name
        }
    }

    val readSortState: Flow<String> = datastore.data
        .catch {exception ->
            if( exception is IOException){
                emit(emptyPreferences())
            }
            else {
                throw exception
            }
            }.map { preferences ->
                    val sortState = preferences[PreferenceKeys.sortState] ?: Priority.NONE.name
                    sortState
                }

    }
