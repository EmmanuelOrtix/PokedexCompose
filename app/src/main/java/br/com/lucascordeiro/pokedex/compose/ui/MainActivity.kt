package br.com.lucascordeiro.pokedex.compose.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.lifecycleScope
import br.com.lucascordeiro.pokedex.compose.ui.home.Home
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexComposeTheme {
               PokedexApp(backPressedDispatcher = onBackPressedDispatcher, viewModel = viewModel)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.errorMessage.filter { it!=null }.collect {error ->
                Toast.makeText(this@MainActivity, error!!, Toast.LENGTH_LONG).show()
            }
        }
    }
}