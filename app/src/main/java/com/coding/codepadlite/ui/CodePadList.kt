package com.coding.codepadlite.ui

import CodePadItem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.coding.codepadlite.DrawerContent
import com.coding.codepadlite.room.Code
import com.coding.codepadlite.room.getDummyCodeItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CodePadList(
    codePadItems: List<Code>,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    showCodeItemListener: (codeItem: Code) -> Unit,
    deleteCodeItemListener: (codeItem: Code) -> Unit,
    addToFavCodeItemListener: (codeItem: Code) -> Unit,
    copyCodeItemListener: (codeItem: Code) -> Unit,
    shareCodeItemListener: (codeItem: Code) -> Unit,
    toggleSearchBarListener: () -> Unit,
    topBarVisible: MutableState<Boolean>,
    favCodes: MutableState<Boolean>
) {

    val searchText = remember { mutableStateOf(TextFieldValue("")) }

    var isListEmpty by remember {
        mutableStateOf(false)
    }
    isListEmpty = codePadItems.isEmpty()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if(topBarVisible.value)
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = {
                        if(favCodes.value) Text(text = "Favorites")
                        else Text("Home")
                    },
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
                        IconButton(onClick = {
                            favCodes.value = !favCodes.value
                        }) {
                            if(favCodes.value)
                                Icon(Icons.Filled.Favorite, "Favorite")
                            else
                                Icon(Icons.Filled.FavoriteBorder, "Not Favorite")
                        }
                        IconButton(onClick = {
                            topBarVisible.value = !topBarVisible.value
                        }) {
                            Icon(Icons.Filled.Search, "Search")
                        }
                    }
                )
        },floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if(!isListEmpty) {
                FloatingActionButton(
                    onClick = {
                        showCodeItemListener(getDummyCodeItem())
                    },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Item")
                }
            }
        },
        drawerContent = {
            DrawerContent(scaffoldState, coroutineScope, favCodes)
        }
    ) {

        Column {

            if(!topBarVisible.value)
                SearchView(searchText, toggleSearchBarListener)
            
            if (codePadItems.isNullOrEmpty()) {
                // Show list is empty
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    EmptyCodePadList(showCodeItemListener)
                }
            } else {

                LazyColumn {
                    items(codePadItems) { item ->
                        if(!favCodes.value || favCodes.value && item.codeFav) {
                            if(topBarVisible.value || (searchText.value.text.isNotEmpty() && item.codeTitle.contains(searchText.value.text))) {
                                CodePadItem(
                                    codePadItem = item,
                                    showCodeItemListener = showCodeItemListener,
                                    deleteCodeItemListener = deleteCodeItemListener,
                                    addToFavCodeItemListener = addToFavCodeItemListener,
                                    copyCodeItemListener = copyCodeItemListener,
                                    shareCodeItemListener = shareCodeItemListener
                                )
                            }
                        }
                    }
                }

            }

        }

    }

}