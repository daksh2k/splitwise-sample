package com.daksh2k.allenmac.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daksh2k.allenmac.ui.add.AddScreen
import com.daksh2k.allenmac.ui.balances.BalancesScreen
import com.daksh2k.allenmac.ui.theme.AllenMacTheme
import dagger.hilt.android.AndroidEntryPoint

enum class Screens(val title: String) {
    Add("Add"),
    Balances("Balances")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AllenMacTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var activeScreen by remember { mutableStateOf<Screens>(Screens.Add) }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Splitwise")
            })
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Screens.entries.forEach { screen ->
                    TextButton(
                        onClick = { activeScreen = screen },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(screen.title)
                    }
                }

            }

            when (activeScreen) {
                Screens.Add -> AddScreen(modifier = Modifier)
                Screens.Balances -> BalancesScreen(modifier = Modifier)
            }
        }

    }

}