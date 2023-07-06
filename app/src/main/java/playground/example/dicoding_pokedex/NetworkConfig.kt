package playground.example.dicoding_pokedex

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import playground.example.dicoding_pokedex.data.ResultPokemon
import playground.example.dicoding_pokedex.data.ResultPokemonList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class NetworkConfig {
    // set interceptor
    fun getInterceptor() : OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getService() = getRetrofit().create(Pokemons::class.java)
}

interface Pokemons {
    @GET("api/v2/pokemon")
    fun getPokemons(@Query("offset") offset: Int, @Query("limit") limit: Int): Call<ResultPokemonList>

    @GET("api/v2/pokemon/{id}")
    fun getPokemon(@Path("id") id: Int): Call<ResultPokemon>
}