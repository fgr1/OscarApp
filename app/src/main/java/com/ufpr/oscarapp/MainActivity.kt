package com.ufpr.oscarapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ufpr.oscarapp.databinding.ActivityMainBinding
import com.ufpr.oscarapp.network.ApiClient
import com.ufpr.oscarapp.network.ApiService
import com.ufpr.oscarapp.network.LoginRequest
import com.ufpr.oscarapp.network.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var apiService: ApiService
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiClient.instance.create(ApiService::class.java)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                login(username, password)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(username: String, password: String) {
        coroutineScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.login(LoginRequest(username, password))
                }
                // Salva o token e informações do usuário
                val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
                sharedPref.edit().apply {
                    putInt("user_token", response.token)
                    putInt("user_id", response.userId)
                    putString("user_name", username)
                    apply()
                }

                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Erro no login: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
