package com.ufpr.oscarapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ufpr.oscarapp.databinding.ActivityFilmDetailBinding
import com.ufpr.oscarapp.model.Film

class FilmDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmDetailBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)

        val film = intent.getParcelableExtra<Film>("film")
        if (film != null) {
            displayFilmDetails(film)
        } else {
            Toast.makeText(this, "Erro ao carregar detalhes do filme", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnVote.setOnClickListener {
            film?.let {
                registerVoteLocally(it)
                Toast.makeText(this, "Voto registrado para o filme: ${film.nome}", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun displayFilmDetails(film: Film) {
        binding.tvFilmName.text = film.nome
        binding.tvFilmGenre.text = film.genero
        Glide.with(this).load(film.foto).into(binding.ivFilmPoster)
    }

    private fun registerVoteLocally(film: Film) {
        val editor = sharedPref.edit()
        editor.putInt("voted_film_id", film.id)
        editor.apply()
    }
}
