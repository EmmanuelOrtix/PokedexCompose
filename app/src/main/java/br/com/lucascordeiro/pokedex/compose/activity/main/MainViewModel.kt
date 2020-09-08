package br.com.lucascordeiro.pokedex.compose.activity.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import br.com.lucascordeiro.pokedex.domain.model.ErrorEntity
import br.com.lucascordeiro.pokedex.domain.model.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class MainViewModel(private val useCase: GetPokemonUseCase) : ViewModel() {
    var pokemons: List<Pokemon> by mutableStateOf(listOf())
        private set

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?>
        get() = _errorMessage

    init {
        viewModelScope.launch(IO) {
            useCase.doGetPokemon()
                    .collect {
                        withContext(Main) {
                            when (it) {
                                is Result.Success -> {
                                    pokemons = it.data
                                }
                                is Result.Error -> {
                                    when (it.error) {
                                        is ErrorEntity.ApiError.Network -> {
                                            _errorMessage.value = "Falha na conexão com a internet, verifique e tente novamente"
                                        }
                                        is ErrorEntity.App.Unknown -> {
                                            _errorMessage.value = "Ocorreu um erro, tente novamente"
                                        }
                                    }
                                }
                            }
                        }
                    }
        }
    }
}