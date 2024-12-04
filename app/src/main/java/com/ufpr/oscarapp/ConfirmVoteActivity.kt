package com.ufpr.oscarapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ufpr.oscarapp.databinding.ActivityConfirmVoteBinding
import com.ufpr.oscarapp.model.VoteRequest
import com.ufpr.oscarapp.network.ApiClient
import com.ufpr.oscarapp.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfirmVoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmVoteBinding
    private lateinit var sharedPref: SharedPreferences
    private val apiService: ApiService by lazy { ApiClient.serverInstance.create(ApiService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmVoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)

        val userId = sharedPref.getInt("user_id", -1)
        if (userId != -1) {
            checkVotingLocked(userId)
        }

        loadVotes()

        binding.btnSubmitVote.setOnClickListener {
            val tokenInput = binding.etToken.text.toString().toIntOrNull()
            val storedToken = sharedPref.getInt("user_token", -1)

            if (tokenInput == storedToken) {
                submitVotes(tokenInput!!)
            } else {
                Toast.makeText(this, "Token inválido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadVotes() {
        val filmId = sharedPref.getInt("voted_film_id", -1)
        val directorId = sharedPref.getInt("voted_director_id", -1)

        binding.tvFilmVote.text =
            if (filmId != -1) "Filme votado: $filmId" else "Filme votado: Nenhum"
        binding.tvDirectorVote.text =
            if (directorId != -1) "Diretor votado: $directorId" else "Diretor votado: Nenhum"
    }

    private fun checkVotingLocked(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.checkVotes(userId)
                withContext(Dispatchers.Main) {
                    if (response.hasVoted) {
                        disableVoting()
                        Toast.makeText(
                            this@ConfirmVoteActivity,
                            "Votos já registrados. Não é possível modificar.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ConfirmVoteActivity,
                        "Erro ao verificar votos: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun submitVotes(token: Int) {
        val userId = sharedPref.getInt("user_id", -1)
        val filmId = sharedPref.getInt("voted_film_id", -1)
        val directorId = sharedPref.getInt("voted_director_id", -1)

        if (filmId == -1 || directorId == -1 || userId == -1) {
            Toast.makeText(this, "Você precisa votar em filme e diretor", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val voteRequest = VoteRequest(
                    userId = userId,
                    token = token,
                    filmId = filmId,
                    directorId = directorId
                )
                val response = apiService.submitVotes(voteRequest)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        showAlertDialog("Sucesso", "Voto registrado com sucesso!")
                        disableVoting()
                    } else {
                        showAlertDialog("Erro", "Falha ao registrar o voto: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showAlertDialog("Erro", "Falha ao registrar o voto: ${e.message}")
                }
            }
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun disableVoting() {
        binding.btnSubmitVote.isEnabled = false
        sharedPref.edit().putBoolean("voting_locked", true).apply()
    }
}
