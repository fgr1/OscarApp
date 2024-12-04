package com.ufpr.oscarapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ufpr.oscarapp.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera o token do SharedPreferences
        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val userToken = sharedPref.getInt("user_token", -1)

        binding.tvToken.text = "Seu token: $userToken"

        binding.btnVoteFilm.setOnClickListener {
            val intent = Intent(this, FilmListActivity::class.java)
            val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
            val userToken = sharedPref.getInt("user_token", -1)

            intent.putExtra("user_token", userToken)
            startActivity(intent)
        }

        binding.btnVoteDirector.setOnClickListener {
            val intent = Intent(this, DirectorVoteActivity::class.java)
            startActivity(intent)

        }

        binding.btnConfirmVote.setOnClickListener {
            // TODO
        }

        binding.btnExit.setOnClickListener {
            finishAffinity()
        }
    }
}
