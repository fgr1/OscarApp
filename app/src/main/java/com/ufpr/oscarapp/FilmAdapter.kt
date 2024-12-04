package com.ufpr.oscarapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ufpr.oscarapp.databinding.ItemFilmBinding
import com.ufpr.oscarapp.model.Film

class FilmAdapter(private val onClick: (Film) -> Unit) :
    RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    private val films = mutableListOf<Film>()

    fun submitList(newFilms: List<Film>) {
        films.clear()
        films.addAll(newFilms)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(films[position])
    }

    override fun getItemCount() = films.size

    inner class FilmViewHolder(private val binding: ItemFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(film: Film) {
            binding.tvName.text = film.nome
            binding.tvGenre.text = film.genero
            Glide.with(binding.root.context).load(film.foto).into(binding.ivPoster)

            binding.root.setOnClickListener {
                onClick(film)
            }
        }
    }
}
