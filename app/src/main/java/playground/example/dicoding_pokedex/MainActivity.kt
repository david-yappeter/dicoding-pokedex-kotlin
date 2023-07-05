package playground.example.dicoding_pokedex

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import playground.example.dicoding_pokedex.adapter.PokemonsAdapter
import playground.example.dicoding_pokedex.component.DrawerActivity
import playground.example.dicoding_pokedex.data.ResultPokemonList
import playground.example.dicoding_pokedex.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL


class MainActivity : DrawerActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPokemon.setHasFixedSize(true)
        binding.rvPokemon.setItemViewCacheSize(20)
        binding.rvPokemon.layoutManager = GridLayoutManager(this, 2)

        NetworkConfig().getService()
            .getPokemons()
            .enqueue(object : Callback<ResultPokemonList> {
                override fun onFailure(call: Call<ResultPokemonList>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<ResultPokemonList>,
                    response: Response<ResultPokemonList>
                ) {
                    binding.rvPokemon.adapter = PokemonsAdapter(response.body())
                }
            })

//        binding.btnIntent.setOnClickListener {
//            val intent = Intent(this, AboutActivity::class.java)
//            startActivity(intent)
//        }

//        DownloadImageFromInternet(binding.testImage).execute("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvPokemon.layoutManager = GridLayoutManager(this, 4)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            binding.rvPokemon.layoutManager = GridLayoutManager(this, 2)
        }
    }


    private inner class DownloadImageFromInternet(var imageView: ImageView): AsyncTask<String, Void, Bitmap?>() {
        init {
            Toast.makeText(applicationContext, "Please wait, it may take a few seconds...", Toast.LENGTH_SHORT).show()
        }

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
