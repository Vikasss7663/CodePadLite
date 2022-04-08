package com.coding.codepadlite.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coding.codepadlite.DrawerContent
import com.coding.codepadlite.room.*
import kotlinx.coroutines.launch

@Composable
fun CodePadEditor(
    getSelectedCodeItemListener: () -> Code?,
    addCodeItemListener: (codeItem: Code) -> Unit,
    showSnackBarMessageListener: (message: String) -> Unit
) {
    // make one edit text
    // and a save button
    // when click on save, prompt user for title

    val selectedCodeItem = getSelectedCodeItemListener() ?: getDummyCodeItem()

    var codeTitle by remember {
        mutableStateOf(selectedCodeItem.codeTitle)
    }
    var codeContent by remember {
        mutableStateOf(selectedCodeItem.codeContent)
    }

    // Drop Down Menu
    val options = getAllCodeLang()
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(selectedCodeItem.codeLang) }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = { Text("CodePadLite") },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.Favorite, "Favorite")
                    }
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.Search, "Search")
                    }
                }
            )
        }
    ) {


        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {


            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    readOnly = true,
                    value = selectedOptionText.value,
                    onValueChange = { },
                    label = { Text("Choose Language") },
                    trailingIcon = {
                        IconButton(onClick = {
                            expanded = !expanded
                        }) {
                            Icon(
                                icon,
                                contentDescription = "Drop Down Menu"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                            }
                        ) {
                            Text(text = selectionOption.value)
                        }
                    }
                }
            }

            OutlinedTextField(
                value = codeTitle,
                onValueChange = { codeTitle = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Code Title")
                },
                maxLines = 2
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {

                OutlinedTextField(
                    value = codeContent,
                    onValueChange = { codeContent = it },
                    modifier = Modifier.fillMaxSize(),
                    label = {
                        Text(text = "Write Your Code Here")
                    }
                )

                Button(
                    onClick = {

                        // check for empty title or code
                        if (codeTitle.isEmpty() || codeContent.isEmpty()) {
                            showSnackBarMessageListener("Code Title or Code can not be empty.")
                            return@Button
                        }

                        val code = selectedCodeItem.let {
                            Code(
                                it.codeId,
                                codeTitle,
                                it.codeDesc,
                                codeContent,
                                selectedOptionText
                            )
                        }
                        addCodeItemListener(code)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        Icons.Filled.Edit, contentDescription = "Save Item"
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(text = "Save")
                }
            }

        }

    }


}