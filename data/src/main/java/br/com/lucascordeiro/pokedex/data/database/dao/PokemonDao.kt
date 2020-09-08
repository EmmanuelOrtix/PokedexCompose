package br.com.lucascordeiro.pokedex.data.database.dao

import androidx.room.*
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonCrossTypeEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonWithTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon")
    fun getAll() : Flow<List<PokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<PokemonEntity>)



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(join: PokemonCrossTypeEntity)

    @Transaction
    @Query("SELECT * FROM pokemon")
    suspend fun getPokemonWithTypeEntity(): PokemonWithTypeEntity

}