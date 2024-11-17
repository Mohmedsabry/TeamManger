@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.teammanger.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammanger.ui.theme.TeamMangerTheme
import com.example.teammanger.ui.theme.blackOrWhite
import com.example.teammanger.ui.theme.selectedNavigationColor
import com.example.teammanger.ui.theme.unSelectedNavigationColor
import com.example.teammanger.util.toDate
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.launch

@Composable
fun BottomSheet(
    desc: String,
    emails: List<String>,
    sheetState: SheetState,
    onDescChange: (String) -> Unit,
    onDueChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onAddTask: () -> Unit
) {
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current
    val coroutine = rememberCoroutineScope()
    val dateState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        initialSelectedDateMillis = System.currentTimeMillis(),
    )
    LaunchedEffect(sheetState.isVisible) {
        if (sheetState.isVisible) {
            focusRequester.requestFocus()
        }
    }
    var showPicker by remember {
        mutableStateOf(false)
    }
    if (sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutine.launch {
                    sheetState.hide()
                }
            },
            sheetState = sheetState,
            modifier = Modifier
                .fillMaxHeight(.5f),
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Column(
                Modifier
                    .padding(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = desc,
                    onValueChange = { onDescChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .padding(5.dp),
                    label = { Text("Name") },
                    isError = desc.isEmpty(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.clearFocus()
                        }
                    )
                )
                DropDownGender(
                    label = "due",
                    items = emails
                ) {
                    onDueChange(it)
                }
                Text(
                    dateState.selectedDateMillis?.toDate() ?: "",
                    fontSize = 12.ssp,
                    color = blackOrWhite,
                    modifier = Modifier.clickable { showPicker = true }
                )
                if (showPicker) {
                    DatePickerDialog(
                        onDismissRequest = { showPicker = false },
                        confirmButton = {
                            OutlinedButton(onClick = {
                                onDateChange(dateState.selectedDateMillis ?: 0)
                                showPicker = false
                            }) {
                                Text("Confirm")
                            }
                        },
                        dismissButton = {
                            OutlinedButton(onClick = {
                                dateState.selectedDateMillis = System.currentTimeMillis()
                                showPicker = false
                            }) {
                                Text("Dismiss")
                            }
                        }
                    ) {
                        DatePicker(
                            state = dateState,
                            title = { Text("Dead Line pick", modifier = Modifier.padding(10.dp)) },
                            showModeToggle = false
                        )
                    }
                }
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                        onAddTask()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = selectedNavigationColor,
                        contentColor = unSelectedNavigationColor
                    )
                ) {
                    Text("Add Task")
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BottomPrev() {
    val bottom = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val co = rememberCoroutineScope()
    TeamMangerTheme {
        Button(onClick = {
            co.launch {
                bottom.show()
            }
        }) { Text("sheet") }
        BottomSheet(
            desc = "",
            emails = listOf("ahmed", "ali"),
            bottom,
            {},
            {},
            {}
        ) { }
    }
}