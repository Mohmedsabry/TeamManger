@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.teammanger.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.dp
import com.example.teammanger.util.Constants.FEMALE
import com.example.teammanger.util.Constants.MALE
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun DropDownGender(
    items: List<String> = listOf(MALE, FEMALE),
    label: String = "gender",
    onValueChange: (String) -> Unit
) {
    var selectedItem by remember {
        mutableStateOf(items.getOrNull(0)?:"")
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.sdp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                label = { Text(label.capitalize(LocaleList())) },
            )
            ExposedDropdownMenu(expanded, onDismissRequest = { expanded = !expanded }) {
                items.forEach { item ->
                    Text(
                        text = item, fontSize = 16.ssp,
                        modifier = Modifier
                            .clickable {
                                onValueChange(item)
                                selectedItem = item
                                expanded = false
                            }
                            .padding(5.dp)
                    )
                }
            }
        }
    }
}