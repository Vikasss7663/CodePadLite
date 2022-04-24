package com.coding.codepadlite.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.coding.codepadlite.R
import com.coding.codepadlite.room.Code
import com.coding.codepadlite.room.CodeLang
import com.coding.codepadlite.room.getAllCodeLang
import com.coding.codepadlite.room.getDummyCodeItem
import com.coding.codepadlite.utils.Key
import com.coding.codepadlite.utils.Parser

@Composable
fun CodePadEditor(
    showSnackBarMessageListener: (message: String) -> Unit,
    getSelectedCodeItemListener: () -> Code?,
    addCodeItemListener: (codeItem: Code) -> Unit,
    copyCodeItemListener: (codeItem: Code) -> Unit,
    shareCodeItemListener: (codeItem: Code) -> Unit
) {

    val selectedCodeItem = getSelectedCodeItemListener() ?: getDummyCodeItem()
    var codeTitle by remember {
        mutableStateOf(selectedCodeItem.codeTitle)
    }
    var editorMode by remember {
        mutableStateOf(false)
    }

    if(selectedCodeItem.codeTitle.isEmpty() && selectedCodeItem.codeContent.isEmpty())
        editorMode = true

    // Drop Down Menu
    val options = getAllCodeLang()
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(selectedCodeItem.codeLang) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val map = when(selectedCodeItem.codeLang) {
        CodeLang.C -> Parser.getCSpan(selectedCodeItem.codeContent)
        CodeLang.JAVA -> Parser.getJavaSpan(selectedCodeItem.codeContent)
        CodeLang.PYTHON -> Parser.getPythonSpan(selectedCodeItem.codeContent)
        else -> Parser.getNoSpan()
    }

    val spannableString = buildAnnotatedString {
        append(selectedCodeItem.codeContent)
        for (list in map[Key.KEYWORD]!!) {
            addStyle(style = SpanStyle(color = colorResource(id = R.color.keyword)), start = list.start, end = list.end)
        }
        for (list in map[Key.COMMENT]!!) {
            addStyle(style = SpanStyle(color = colorResource(id = R.color.comment)), start = list.start, end = list.end)
        }
        for (list in map[Key.SEMICOLON]!!) {
            addStyle(style = SpanStyle(color = colorResource(id = R.color.semicolon)), start = list.start, end = list.end)
        }
    }

    var codeContent by remember {
        mutableStateOf(selectedCodeItem.codeContent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text(text = "CodePad Lite")
                },
                actions = {
                    IconButton(onClick = {
                        copyCodeItemListener(selectedCodeItem)
                    }) {
                        Icon(painterResource(id = R.drawable.icon_copy), "Copy")
                    }
                    IconButton(onClick = {
                        shareCodeItemListener(selectedCodeItem)
                    }) {
                        Icon(Icons.Filled.Share, "Share")
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if(editorMode) {
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
                        painterResource(id = R.drawable.icon_save), contentDescription = "Save Item"
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(text = "Save")
                }
            } else {
                Button(
                    onClick = {
                        editorMode = !editorMode
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        Icons.Filled.Edit, contentDescription = "Edit Item"
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(text = "Edit")
                }
            }
        }
    ) {


        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
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

            // Show Code Content
            if(editorMode) {

                OutlinedTextField(
                    value = codeContent,
                    onValueChange = { codeContent = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState(0)),
                    label = {
                        Text(text = "Write Your Code Here")
                    }
                )

            } else {
                Surface(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.Gray
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    SelectionContainer {
                        Text(
                            text = spannableString,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .horizontalScroll(rememberScrollState(0))
                        )
                    }
                }
            }

        }

    }


}