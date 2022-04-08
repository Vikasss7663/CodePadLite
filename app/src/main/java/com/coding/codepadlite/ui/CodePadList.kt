package com.coding.codepadlite.ui

import CodePadItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.codepadlite.DrawerContent
import com.coding.codepadlite.room.Code
import com.coding.codepadlite.room.getDummyCodeItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CodePadList(
    codePadItems: List<Code>,
    editCodeItemListener: (codeItem: Code) -> Unit,
    deleteCodeItemListener: (codeItem: Code) -> Unit,
    addToFavCodeItemListener: (codeItem: Code) -> Unit,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope
) {

    var isListEmpty by remember {
        mutableStateOf(false)
    }
    isListEmpty = codePadItems.isEmpty()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = { Text("CodePadLite") },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(Icons.Filled.Menu, "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.Favorite, "Favorite")
                    }
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.Search, "Search")
                    }
                }
            )
        },floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if(!isListEmpty) {
                FloatingActionButton(
                    onClick = {
                        editCodeItemListener(getDummyCodeItem())
                    }
                )
                {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Item")
                }
            }
        },
        drawerContent = {
            DrawerContent()
        }
    ) {

        Column {

            if (codePadItems.isNullOrEmpty()) {
                // Show list is empty
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        EmptyCodePadList(editCodeItemListener)
                    }
            } else {

                LazyColumn {
                    items(codePadItems) { item ->
                        CodePadItem(
                            item,
                            editCodeItemListener,
                            deleteCodeItemListener,
                            addToFavCodeItemListener
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                        )
                    }
                }

            }

        }

    }

}