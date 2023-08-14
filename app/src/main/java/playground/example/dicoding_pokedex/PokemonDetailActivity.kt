package playground.example.dicoding_pokedex

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import playground.example.dicoding_pokedex.data.ResultPokemon
import playground.example.dicoding_pokedex.databinding.ActivityPokemonDetailBinding
import playground.example.dicoding_pokedex.model.Pokemon
import playground.example.dicoding_pokedex.model.PokemonAbility
import playground.example.dicoding_pokedex.model.PokemonStats
import playground.example.dicoding_pokedex.model.PokemonType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonDetailActivity : AppCompatActivity() {
    private var id: Int? = null

    private lateinit var binding: ActivityPokemonDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        id = intent.getIntExtra("id", -1)

        val actionbar = supportActionBar!!
        actionbar.title = "Pokemon"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.elevation = 0.toFloat()

        binding.svRoot.loadSkeleton()

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
                        abilities = ArrayList(resp.abilities!!.map{
                            PokemonAbility(
                            name = it?.ability?.name!!,
                            isHidden = it.isHidden!!,
                        )
                        }),
                        stats = ArrayList(resp.stats!!.map{PokemonStats(
                            name = it?.stat?.name!!,
                            base_stat = it.baseStat!!.toInt(),
                            effort = it.effort!!.toInt(),
                        )}),
                        types = ArrayList(resp.types!!.map{PokemonType.fromString(it?.type?.name.toString())}),
                    )


                    DownloadImageFromInternet(binding.pokemonImage).execute(pokemon.sprite_url)

                    actionbar.setBackgroundDrawable(ColorDrawable(pokemon.types[0].relatedColor()))

                    val backgroundDrawable = binding.pokemonImage.background
                    if(backgroundDrawable is GradientDrawable) {
                        val gradientDrawable = backgroundDrawable as GradientDrawable
                        gradientDrawable.setColor(pokemon.types[0].relatedColor())
                    binding.pokemonImage.background = gradientDrawable
                    }
                    
                    binding.pokemonName.text = pokemon.name
                    binding.pokemonBaseXp.text = pokemon.base_xp.toString()
                    binding.pokemonHeight.text = pokemon.height.toString()
                    binding.pokemonWeight.text = pokemon.weight.toString()

                    pokemon.types.forEach {
                        val typeView = View.inflate(this@PokemonDetailActivity, R.layout.pokemon_type_icon, null) as ImageView

                        typeView.setImageResource(
                            when(it) {
                                PokemonType.NORMAL -> R.drawable.normal
                                PokemonType.FIRE -> R.drawable.fire
                                PokemonType.WATER -> R.drawable.water
                                PokemonType.ELECTRIC -> R.drawable.electric
                                PokemonType.GRASS -> R.drawable.grass
                                PokemonType.ICE -> R.drawable.ice
                                PokemonType.FIGHTING -> R.drawable.fighting
                                PokemonType.POISON -> R.drawable.poison
                                PokemonType.GROUND -> R.drawable.ground
                                PokemonType.FLYING -> R.drawable.flying
                                PokemonType.PSYCHIC -> R.drawable.psychic
                                PokemonType.BUG -> R.drawable.bug
                                PokemonType.ROCK -> R.drawable.rock
                                PokemonType.GHOST -> R.drawable.ghost
                                PokemonType.DRAGON -> R.drawable.dragon
                                PokemonType.DARK -> R.drawable.dark
                                PokemonType.STEEL -> R.drawable.steel
                                PokemonType.FAIRY -> R.drawable.fairy
                                else -> R.drawable.unknown
                            }
                        )

                        binding.llType.addView(typeView)
                    }

                    pokemon.stats.forEach{
                        val statView = View.inflate(this@PokemonDetailActivity, R.layout.pokemon_stat, null) as LinearLayout
                        val progressBar = statView.findViewById<ProgressBar>(R.id.stat_progress)

                        statView.findViewById<TextView>(R.id.stat_label).text = it.nameLabel
                        progressBar.max = 255
                        progressBar.progressBackgroundTintList = ColorStateList.valueOf(it.relatedColor)
                        progressBar.progressTintList = ColorStateList.valueOf(it.relatedColor)
                        progressBar.progress = it.base_stat

                        binding.llStats.addView(statView)
                    }

                    var idx = 1
                    pokemon.abilities.forEach {
                        when(idx) {
                            1-> {
                                binding.ability1.text = it.name
                                idx++
                            }
                            2-> {
                                binding.ability2.text = it.name
                                idx++
                            }
                            3-> {
                                binding.ability3.text = it.name
                                idx++
                            }
                            4-> {
                                binding.ability4.text = it.name
                                idx++
                            }
                            else -> {}
                        }
                    }

                    binding.svRoot.hideSkeleton()
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
