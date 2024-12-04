package com.ufpr.oscarapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ufpr.oscarapp.databinding.ActivityFilmListBinding
import com.ufpr.oscarapp.network.ApiClient
import com.ufpr.oscarapp.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class Film(val id: Int, val name: String, val genre: String, val posterUrl: String)

class FilmListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmListBinding
    private lateinit var adapter: FilmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = FilmAdapter { film ->
            val intent = Intent(this, FilmDetailActivity::class.java)
            intent.putExtra("film", film)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        loadFilms()
    }

    private fun loadFilms() {
        val api = ApiClient.externalInstance.create(ApiService::class.java)

        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val films = api.getFilms()
                withContext(Dispatchers.Main) {
                    adapter.submitList(films)
                    binding.progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FilmListActivity, "Erro ao carregar filmes", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }


}

