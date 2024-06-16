package com.daksh2k.allenmac.ui.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daksh2k.allenmac.domain.model.Expense
import com.daksh2k.allenmac.domain.model.User
import com.daksh2k.allenmac.ui.MainEvent
import com.daksh2k.allenmac.ui.MainViewModel
import com.daksh2k.allenmac.ui.components.SelectUserDialog
import com.daksh2k.allenmac.ui.components.TextField
import com.daksh2k.allenmac.ui.components.UserChip
import kotlinx.coroutines.launch
import java.util.UUID

//Add expenses: Users should be able to add expenses,
// specifying the amount, the payer, and the participants involved in the expense.
// The app should split the expense equally among the participants.

@Composable
fun AddScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

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

        var expenseTitle by remember { mutableStateOf("") }
        var expenseAmount by remember { mutableDoubleStateOf(0.0) }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Expense")
            TextField(value = expenseTitle, onValueChange = {
                expenseTitle = it
            })

        }
        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Total")
            TextField(
                value = expenseAmount.toString(),
                onValueChange = {
                    expenseAmount = it.toDouble()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        var payer by remember { mutableStateOf<User?>(null) }
        var participants by remember { mutableStateOf<List<User>>(emptyList()) }

        UserSection(
            users = uiState.users,
            payer = payer,
            setPayer = { payer = it },
            participants = participants,
            addParticipant = { user ->
                participants = participants + user
            }
        )



        OutlinedButton(
            enabled = payer != null && participants.isNotEmpty() && expenseAmount > 0.0 && expenseTitle.isNotBlank(),
            onClick = {
                coroutineScope.launch {
                    viewModel.events.send(
                        MainEvent.AddExpense(
                            expense = Expense(
                                id = UUID.randomUUID().hashCode(),
                                amount = expenseAmount,
                                payer = payer!!,
                                participants = participants
                            )
                        )
                    )
                    //reset state
                    expenseTitle = ""
                    expenseAmount = 0.0
                    payer = null
                    participants = emptyList()
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ADD")
        }

    }

}

@Composable
fun UserSection(
    users: List<User>,
    payer: User?,
    setPayer: (User) -> Unit,
    participants: List<User>,
    addParticipant: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var selectingPayer by remember { mutableStateOf(false) }

        var selectingParticipants by remember { mutableStateOf(false) }

        Column {
            Text("Paid By")
            Spacer(
                modifier = Modifier.height(8.dp)
            )

            UserChip(name = payer?.name ?: "Select payer", modifier = Modifier.clickable {
                selectingPayer = true
            })

            if (selectingPayer) {
                SelectUserDialog(
                    onDismiss = {
                        selectingPayer = false
                    },
                    users = users,
                    onSelectUser = { user ->
                        setPayer(user)
                        selectingPayer = false
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Participants")
                IconButton(
                    onClick = {
                        selectingParticipants = true
                    },
                    enabled = users.size > participants.size // disable when all users are added
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                participants.forEach { user ->
                    UserChip(name = user.name)
                }
            }

            if (selectingParticipants && users.size > participants.size) {
                SelectUserDialog(
                    onDismiss = {
                        selectingParticipants = false
                    },
                    users = users.filter { participants.contains(it).not() },
                    onSelectUser = { user ->
                        addParticipant(user)
                        selectingParticipants = false
                    }
                )
            }
        }
    }
}



