package com.ufpr.oscarapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ufpr.oscarapp.databinding.ActivityDirectorVoteBinding
import com.ufpr.oscarapp.model.Director
import com.ufpr.oscarapp.network.ApiClient
import com.ufpr.oscarapp.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DirectorVoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDirectorVoteBinding
    private lateinit var sharedPref: SharedPreferences
    private val apiService: ApiService by lazy { ApiClient.externalInstance.create(ApiService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDirectorVoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)

        loadDirectors()

        binding.btnConfirmVote.setOnClickListener {
            val selectedId = binding.rgDirectors.checkedRadioButtonId

            if (selectedId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedId)
                val selectedDirectorId = selectedRadioButton.tag as Int

                registerVoteLocally(selectedDirectorId)
                Toast.makeText(this, "Voto registrado!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Selecione um diretor!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadDirectors() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val directors = apiService.getDirectors()
                withContext(Dispatchers.Main) {
                    populateRadioGroup(directors)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DirectorVoteActivity, "Erro ao carregar diretores", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun populateRadioGroup(directors: List<Director>) {
        for (director in directors) {
            val radioButton = RadioButton(this).apply {
                text = director.nome
                tag = director.id
            }
            binding.rgDirectors.addView(radioButton)
        }
    }

    private fun registerVoteLocally(directorId: Int) {
        val editor = sharedPref.edit()
        editor.putInt("voted_director_id", directorId)
        editor.apply()
    }
}
