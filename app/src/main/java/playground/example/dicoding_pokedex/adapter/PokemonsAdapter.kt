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
import playground.example.dicoding_pokedex.data.ResultsItem

class PokemonsAdapter(private val data: ResultPokemonList?) : RecyclerView.Adapter<PokemonsAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return MyHolder(v)
    }
    override fun getItemCount(): Int = data?.results?.size ?: 0
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(data?.results?.get(position))
    }
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(get: ResultsItem?) {
            val id = get?.url?.removePrefix("https://pokeapi.co/api/v2/pokemon/")?.removeSuffix("/")!!.toInt()

            itemView.findViewById<TextView>(R.id.name).text = get?.name
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

