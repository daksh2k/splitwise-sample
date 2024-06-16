package com.daksh2k.allenmac.ui.balances

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daksh2k.allenmac.ui.MainEvent
import com.daksh2k.allenmac.ui.MainViewModel
import com.daksh2k.allenmac.ui.components.UserChip

@Composable
fun BalancesScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        if (uiState.users.isEmpty()) {
            viewModel.events.send(MainEvent.LoadData)
        }
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(state = rememberScrollState()),
    ) {
        uiState.balances.forEach { (user, balance) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                UserChip(
                    name = user.name,
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "$balance",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}