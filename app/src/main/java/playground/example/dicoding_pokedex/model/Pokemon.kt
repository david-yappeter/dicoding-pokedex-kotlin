package playground.example.dicoding_pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val id: String,
    val name: String,
    val description: String,
    val sprite_url: String,
    val abilities: ArrayList<PokemonAbility>,
    val stats: ArrayList<PokemonStats>,
    val types: ArrayList<PokemonType>,
): Parcelable

@Parcelize
data class PokemonStats (
    val name: String,
    val base_stat: Int,
    val effort: Int,
): Parcelable

@Parcelize
data class PokemonAbility (
    val name: String,
    val isHidden: Boolean,
): Parcelable

enum class PokemonType {
    NORMAL,
    FIGHTING,
    FLYING,
    POISON,
    GROUND,
    ROCK,
    BUG,
    GHOST,
    STEEL,
    FIRE,
    WATER,
    GRASS,
    ELECTRIC,
    PSYCHIC,
    ICE,
    DRAGON,
    DARK,
    FAIRY,
    UNKNOWN,
    SHADOW;

    override fun toString(): String {
        return when(this) {
            NORMAL->"Normal"
            FIGHTING->"Fighting"
            FLYING->"Flying"
            POISON->"Poison"
            GROUND->"Ground"
            ROCK->"Rock"
            BUG->"Bug"
            GHOST->"Ghost"
            STEEL->"Steel"
            FIRE->"Fire"
            WATER->"Water"
            GRASS->"Grass"
            ELECTRIC->"Electric"
            PSYCHIC->"Psychic"
            ICE->"Ice"
            DRAGON->"Dragon"
            DARK->"Dark"
            FAIRY->"Fairy"
            UNKNOWN->"Unknown"
            SHADOW->"Shadow"
        }
    }
}
