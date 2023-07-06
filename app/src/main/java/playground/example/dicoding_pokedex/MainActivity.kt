package playground.example.dicoding_pokedex

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import playground.example.dicoding_pokedex.adapter.PokemonsAdapter
import playground.example.dicoding_pokedex.component.DrawerActivity
import playground.example.dicoding_pokedex.data.ResultPokemonList
import playground.example.dicoding_pokedex.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import kotlin.math.ceil


class MainActivity : DrawerActivity() {
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val limit = 20
    private var cachedSize = 10
    private var visibleThreshold = 4 // 4 / 2 = 2 row (portrait) 8 / 4 = 2 row (landscape)

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPokemon.adapter = PokemonsAdapter()
        binding.rvPokemon.setHasFixedSize(true)
        handleOrientation(this.resources.configuration.orientation)

        loadData()

        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && !isLastPage && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
                    // Reached the threshold for loading more items
                    currentPage++
                    loadData()
                }
            }
        }

        binding.rvPokemon.addOnScrollListener(scrollListener)
    }

    private fun loadData() {

        isLoading = true

        val offset = (currentPage - 1) * limit

        NetworkConfig().getService()
            .getPokemons(offset, limit)
            .enqueue(object : Callback<ResultPokemonList> {
                override fun onFailure(call: Call<ResultPokemonList>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    isLoading = false
                }

                override fun onResponse(
                    call: Call<ResultPokemonList>,
                    response: Response<ResultPokemonList>
                ) {
                    isLoading = false

                    if (response.isSuccessful) {
                        val pokemonList = response.body()
                        (binding.rvPokemon.adapter as? PokemonsAdapter)?.apply {
                            addData(pokemonList)
                            notifyDataSetChanged()
                        }

                        isLastPage = currentPage >= ceil(pokemonList!!.count!!.toFloat() / limit).toInt()
                    } else {
                        Toast.makeText(this@MainActivity, "Failed to load data.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        handleOrientation(newConfig.orientation.toInt())
    }

    private fun handleOrientation(orientation: Int) {
        when(orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                cachedSize = 10
                binding.rvPokemon.setItemViewCacheSize(cachedSize)
                binding.rvPokemon.layoutManager = GridLayoutManager(this, 4)
                visibleThreshold = 8
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                cachedSize = 14
                binding.rvPokemon.setItemViewCacheSize(cachedSize)
                binding.rvPokemon.layoutManager = GridLayoutManager(this, 2)
                visibleThreshold = 4
            }
            else -> {}
        }
    }
}
