package br.com.lucascordeiro.pokedex.compose.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.ui.pokedex.generateList
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.utils.SharedElementsRoot
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import kotlin.math.ceil

@Composable
fun PokemonCollection(
    onPokemonSelected: (Pokemon) -> Unit,
    scrollPosition: () -> Float,
    scrollState: ScrollState? = null,
    setScrollPosition: (Float) -> Unit,
    pokemons: List<Pokemon>,
    loadMoreItems: () -> Unit,
    updateLike: (Long, Boolean) -> Unit,
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    val listScrollState = scrollState?:rememberScrollState()

    onActive{
        listScrollState.scrollTo(scrollPosition())
        Log.d("BUG", "onActive List: pokemons: ${pokemons.size}")
    }

    onCommit(callback = {
        Log.d("BUG", "onCommit List: pokemons: ${pokemons.size}")
    })

    ScrollableColumn(
            scrollState = listScrollState,
            modifier = modifier.fillMaxSize()
    ) {
        StaggeredVerticalGrid(
                maxColumnWidth = 220.dp,
                modifier = Modifier.fillMaxHeight().padding(4.dp)
        ) {
            pokemons.forEach { pokemon ->
                PokemonItem(
                        scrollState = listScrollState,
                        setPosition = setScrollPosition,
                        onPokemonSelected = onPokemonSelected,
                        modifier = Modifier,
                        updateLike = updateLike,
                        pokemon = pokemon
                )
            }
        }

        if (!loading) {
            Button(
                    onClick = {
                        setScrollPosition(listScrollState.value)
                        loadMoreItems()
                    },
                    modifier = Modifier
                            .padding(0.dp, 8.dp),
                    backgroundColor = MaterialTheme.colors.background
            ) {
                Text(
                        text = "Carregar mais",
                        style = TextStyle(
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(4.dp)

                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewPokemonList() {
    PokedexComposeTheme(darkTheme = true) {
        SharedElementsRoot {
            Surface(contentColor = contentColor()) {
                PokemonCollection(
                        setScrollPosition = {},
                        scrollPosition = { 1f },
                        pokemons = remember { generateList() },
                        onPokemonSelected = {},
                        loadMoreItems = {},
                        updateLike = { _, _ -> },
                        loading = false
                )
            }
        }
    }
}
