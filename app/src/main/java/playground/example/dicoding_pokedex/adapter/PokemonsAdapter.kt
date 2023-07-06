package playground.example.dicoding_pokedex.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import playground.example.dicoding_pokedex.PokemonDetailActivity
import playground.example.dicoding_pokedex.R
import playground.example.dicoding_pokedex.data.ResultPokemonList
import playground.example.dicoding_pokedex.model.Pokemon

class PokemonsAdapter() : RecyclerView.Adapter<PokemonsAdapter.MyHolder>() {
    private val pokemonList: MutableList<Pokemon> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int = pokemonList.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(pokemonList[position])
    }

    fun addData(newPokemonList: ResultPokemonList?) {
        if(newPokemonList != null) {
            pokemonList.addAll(newPokemonList?.results!!.map {
                val id = it?.url?.removePrefix("https://pokeapi.co/api/v2/pokemon/")?.removeSuffix("/")!!
                Pokemon(
                    id = id,
                    name=it.name!!,
                    height = 0,
                    weight = 0,
                    base_xp = 0,
                    description = "",
                    abilities = ArrayList(),
                    sprite_url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
                    stats = ArrayList(),
                    types = ArrayList(),
                )
            })
        }

        notifyDataSetChanged()
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pokemon: Pokemon) {
            val id = pokemon.id.toInt()

            itemView.findViewById<TextView>(R.id.name).text = pokemon.name
            DownloadImageFromInternet(itemView.findViewById<ImageView>(R.id.sprite)).execute("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png")

            itemView.findViewById<CardView>(R.id.cv_container).setOnClickListener{
                handleCardClick(id)
            }
        }

        private fun handleCardClick(id: Int) {
            val intent = Intent(itemView.context, PokemonDetailActivity::class.java)
            intent.putExtra("id", id)
            itemView.context.startActivity(intent)
        }

        private inner class DownloadImageFromInternet(var imageView: ImageView): AsyncTask<String, Void, Bitmap?>() {
            override fun doInBackground(vararg urls: String?): Bitmap? {
                val imageURL = urls[0]
                var image: Bitmap? = null

                try {
                    val `in` = java.net.URL(imageURL).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                } catch (e: Exception) {
                    Log.e("Error Message", e.message.toString())
                    e.printStackTrace()
                }
                return image
            }

            override fun onPostExecute(result: Bitmap?) {
                imageView.setImageBitmap(result)
            }
        }
    }
}