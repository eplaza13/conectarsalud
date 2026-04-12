package com.conectarsalud.app.data.repository

import android.content.Context
import android.util.Log
import com.conectarsalud.app.R
import com.conectarsalud.app.data.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(private val context: Context) {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user!!
            // Si el perfil no existe en Firestore, lo creamos desde los datos de Auth
            ensureUserProfileExists(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUpWithEmail(
        email: String,
        password: String,
        nombre: String,
        apellido: String,
        dni: String
    ): Result<FirebaseUser> {
        return try {
            Log.d(TAG, "Intentando crear usuario con email: $email")
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user!!
            Log.d(TAG, "Usuario creado en Auth: ${user.uid}")

            // Guardar en Firestore de forma independiente (no bloquea el registro si falla)
            try {
                saveUserToFirestore(
                    User(
                        uid = user.uid,
                        nombre = nombre,
                        apellido = apellido,
                        dni = dni,
                        email = email
                    )
                )
                Log.d(TAG, "Perfil guardado en Firestore correctamente")
            } catch (firestoreEx: Exception) {
                // Firestore falló pero el usuario fue creado en Auth — loguear y continuar
                Log.w(TAG, "Error guardando en Firestore (el usuario fue creado en Auth): ${firestoreEx.message}")
            }

            Result.success(user)
        } catch (e: Exception) {
            Log.e(TAG, "Error creando usuario en Auth: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val user = result.user!!
            if (result.additionalUserInfo?.isNewUser == true) {
                val displayName = user.displayName ?: ""
                val parts = displayName.split(" ")
                saveUserToFirestore(
                    User(
                        uid = user.uid,
                        nombre = if (parts.isNotEmpty()) parts[0] else displayName,
                        apellido = if (parts.size > 1) parts.drop(1).joinToString(" ") else "",
                        email = user.email ?: "",
                        photoUrl = user.photoUrl?.toString()
                    )
                )
            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users")
            .document(user.uid)
            .set(user)
            .await()
    }

    suspend fun getUser(uid: String): User? {
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            doc.toObject(User::class.java)
        } catch (e: Exception) {
            Log.w(TAG, "Error obteniendo usuario de Firestore: ${e.message}")
            null
        }
    }

    /**
     * Si el usuario no tiene perfil en Firestore (ej: se registró antes de que
     * Firestore estuviera configurado), lo crea con los datos disponibles en Auth.
     */
    suspend fun ensureUserProfileExists(firebaseUser: FirebaseUser) {
        try {
            val doc = firestore.collection("users").document(firebaseUser.uid).get().await()
            if (!doc.exists()) {
                Log.d(TAG, "Perfil no encontrado en Firestore, creando...")
                val displayName = firebaseUser.displayName ?: ""
                val parts = displayName.split(" ")
                val user = User(
                    uid = firebaseUser.uid,
                    nombre = if (parts.isNotEmpty() && parts[0].isNotBlank()) parts[0]
                             else firebaseUser.email?.substringBefore("@") ?: "Usuario",
                    apellido = if (parts.size > 1) parts.drop(1).joinToString(" ") else "",
                    email = firebaseUser.email ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString()
                )
                saveUserToFirestore(user)
                Log.d(TAG, "Perfil creado en Firestore para ${firebaseUser.uid}")
            } else {
                Log.d(TAG, "Perfil ya existe en Firestore para ${firebaseUser.uid}")
            }
        } catch (e: Exception) {
            Log.w(TAG, "No se pudo verificar/crear perfil en Firestore: ${e.message}")
        }
    }

    fun signOut() {
        auth.signOut()
    }

    companion object {
        private const val TAG = "UserRepository"
    }
}
