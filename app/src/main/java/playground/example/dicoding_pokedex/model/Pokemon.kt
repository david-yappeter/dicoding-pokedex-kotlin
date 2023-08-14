package playground.example.dicoding_pokedex.model

import android.graphics.Color
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val id: String,
    val name: String,
    val description: String,
    val sprite_url: String,
    val weight: Int,
    val height: Int,
    val base_xp: Int,
    val abilities: ArrayList<PokemonAbility>,
    val stats: ArrayList<PokemonStats>,
    val types: ArrayList<PokemonType>,
): Parcelable

@Parcelize
data class PokemonStats (
    val name: String,
    val base_stat: Int,
    val effort: Int,
): Parcelable {
    val nameLabel
        get() = when(name) {
            "hp"->"HP"
            "attack" -> "ATK"
            "defense" -> "DEF"
            "special-attack" -> "SP.ATK"
            "special-defense"-> "SP.DEF"
            "speed" -> "SPD"
            else -> "Unknown"
        }

    val relatedColor
        get() = when(name) {
            "hp"->Color.parseColor("#D63944")
            "attack" -> Color.parseColor("#FAAA22")
            "defense" -> Color.parseColor("#0093F0")
            "special-attack" -> Color.parseColor("#8EB0C4")
            "special-defense"-> Color.parseColor("#398E3B")
            "speed" -> Color.parseColor("#F0CF98")
            else -> Color.parseColor("#000000")
        }
}

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

    companion object {
        fun fromString(value: String): PokemonType {
            return valueOf(value.uppercase())
        }
    }

    fun relatedColor(): Int {
        return when(this) {
            NORMAL -> Color.parseColor("#A8A77A")
            FIRE -> Color.parseColor("#EE8130")
            WATER -> Color.parseColor("#6390F0")
            ELECTRIC -> Color.parseColor("#F7D02C")
            GRASS -> Color.parseColor("#7AC74C")
            ICE -> Color.parseColor("#96D9D6")
            FIGHTING -> Color.parseColor("#C22E28")
            POISON -> Color.parseColor("#A33EA1")
            GROUND -> Color.parseColor("#E2BF65")
            FLYING -> Color.parseColor("#A98FF3")
            PSYCHIC -> Color.parseColor("#F95587")
            BUG -> Color.parseColor("#A6B91A")
            ROCK -> Color.parseColor("#B6A136")
            GHOST -> Color.parseColor("#735797")
            DRAGON -> Color.parseColor("#6F35FC")
            DARK -> Color.parseColor("#705746")
            STEEL -> Color.parseColor("#B7B7CE")
            FAIRY -> Color.parseColor("#D685AD")
            else -> Color.WHITE
        }
    }

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
