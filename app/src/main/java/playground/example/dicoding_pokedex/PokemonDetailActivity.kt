package playground.example.dicoding_pokedex

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import playground.example.dicoding_pokedex.data.ResultPokemon
import playground.example.dicoding_pokedex.databinding.ActivityMainBinding
import playground.example.dicoding_pokedex.databinding.ActivityPokemonDetailBinding
import playground.example.dicoding_pokedex.model.Pokemon
import playground.example.dicoding_pokedex.model.PokemonAbility
import playground.example.dicoding_pokedex.model.PokemonStats
import playground.example.dicoding_pokedex.model.PokemonType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonDetailActivity : AppCompatActivity() {
    private var id: Int? = null;

    private lateinit var binding: ActivityPokemonDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        id = intent.getIntExtra("id", -1)

        val actionbar = supportActionBar
        actionbar!!.title = "Pokemon"
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        NetworkConfig().getService()
            .getPokemon(id!!)
            .enqueue(object : Callback<ResultPokemon> {
                override fun onFailure(call: Call<ResultPokemon>, t: Throwable) {
                }
                override fun onResponse(
                    call: Call<ResultPokemon>,
                    response: Response<ResultPokemon>
                ) {
                    val resp = response.body()
                    val pokemon = Pokemon(
                        id = resp!!.id.toString(),
                        name = resp.name!!,
                        description = "",
                        height = resp.height!!,
                        weight = resp.weight!!,
                        base_xp = resp.baseExperience!!,
                        sprite_url= "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
                        abilities = ArrayList<PokemonAbility>(resp.abilities!!.map{
                            PokemonAbility(
                            name = it?.ability?.name!!,
                            isHidden = it.isHidden!!,
                        )
                        }),
                        stats = ArrayList<PokemonStats>(resp.stats!!.map{PokemonStats(
                            name = it?.stat?.name!!,
                            base_stat = it.baseStat!!.toInt(),
                            effort = it.effort!!.toInt(),
                        )}),
                        types = ArrayList(resp.types!!.map{PokemonType.fromString(it?.type?.name.toString())}),
                    )


                    DownloadImageFromInternet(binding.pokemonImage).execute(pokemon.sprite_url)

                    binding.pokemonImage.setBackgroundColor(pokemon.types[0].relatedColor())
                    binding.pokemonName.text = pokemon.name
                    binding.pokemonBaseXp.text = pokemon.base_xp.toString()
                    binding.pokemonHeight.text = pokemon.height.toString()
                    binding.pokemonWeight.text = pokemon.weight.toString()

                    pokemon.stats.forEach{
                        val statView = layoutInflater.inflate(R.layout.pokemon_stat, null) as LinearLayout

                        statView.findViewById<TextView>(R.id.stat_label).text = it.nameLabel!!
                        statView.findViewById<ProgressBar>(R.id.stat_progress).max = 255
                        statView.findViewById<ProgressBar>(R.id.stat_progress).progress = it.base_stat

                        binding.llStats.addView(statView)
                    }
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
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
