// Transaction form with validation
package com.sai.expensetracker.presentation.transactions

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sai.expensetracker.domain.model.TransactionType
import com.sai.expensetracker.presentation.transactions.components.CategorySelector
import com.sai.expensetracker.util.DateUtils
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddEditTransactionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is AddEditTransactionEvent.TransactionSaved -> onNavigateBack()
                is AddEditTransactionEvent.TransactionDeleted -> onNavigateBack()
                is AddEditTransactionEvent.Error -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.isEditing) "Edit Transaction" else "Add Transaction",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state.isEditing) {
                        IconButton(onClick = viewModel::onDeleteTransaction) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Transaction Type Toggle
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TransactionType.entries.forEach { type ->
                    FilterChip(
                        selected = state.type == type,
                        onClick = { viewModel.onTypeChange(type) },
                        label = {
                            Text(type.name.lowercase().replaceFirstChar { it.uppercase() })
                        }
                    )
                }
            }

            // Amount Input
            OutlinedTextField(
                value = state.amount,
                onValueChange = viewModel::onAmountChange,
                label = { Text("Amount") },
                prefix = { Text("$") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            // Date Picker
            OutlinedButton(
                onClick = {
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = state.date
                    }
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            val selectedCalendar = Calendar.getInstance().apply {
                                set(year, month, dayOfMonth)
                            }
                            viewModel.onDateChange(selectedCalendar.timeInMillis)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.CalendarToday, contentDescription = null)
                Text(
                    text = "  ${DateUtils.formatDate(state.date)}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Category Selector
            Text(
                text = "Category",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            CategorySelector(
                categories = state.categories,
                selectedCategoryId = state.selectedCategoryId,
                onCategorySelect = viewModel::onCategorySelect
            )

            // Note Input
            OutlinedTextField(
                value = state.note,
                onValueChange = viewModel::onNoteChange,
                label = { Text("Note (optional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = viewModel::saveTransaction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = state.amount.isNotBlank() && state.selectedCategoryId > 0
            ) {
                Text(
                    text = if (state.isEditing) "Update Transaction" else "Save Transaction",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
