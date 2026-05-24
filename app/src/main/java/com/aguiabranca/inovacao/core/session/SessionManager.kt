package com.aguiabranca.inovacao.core.session

import android.content.Context
import android.content.SharedPreferences
import com.aguiabranca.inovacao.domain.model.Session
import com.aguiabranca.inovacao.domain.model.User
import com.aguiabranca.inovacao.domain.model.UserRole
import com.aguiabranca.inovacao.domain.model.toDb
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("aguia_session", Context.MODE_PRIVATE)
    }

    fun saveSession(user: User) {
        prefs.edit()
            .putString("uid", user.id)
            .putString("name", user.name)
            .putString("email", user.email)
            .putString("role", user.role.toDb())
            .putString("area", user.unit)
            .putBoolean("active", true)
            .apply()
    }

    fun getSession(): Session? {
        val uid = prefs.getString("uid", null) ?: return null
        val name = prefs.getString("name", null) ?: return null
        val email = prefs.getString("email", null) ?: return null
        val roleStr = prefs.getString("role", null) ?: return null
        val area = prefs.getString("area", null)
        val active = prefs.getBoolean("active", true)

        val role = UserRole.fromDb(roleStr)
        val user = User(uid, name, email, role, area, null, 0L)
        return Session(user)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean = prefs.getString("uid", null) != null
}

