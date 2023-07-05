package playground.example.dicoding_pokedex

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import playground.example.dicoding_pokedex.data.ResultPokemonList
import playground.example.dicoding_pokedex.model.Pokemon
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

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
    fun getPokemons(): Call<ResultPokemonList>
}