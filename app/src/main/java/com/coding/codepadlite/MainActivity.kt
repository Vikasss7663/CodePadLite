package com.coding.codepadlite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.coding.codepadlite.room.Code
import com.coding.codepadlite.ui.CodePadEditor
import com.coding.codepadlite.ui.CodePadList
import com.coding.codepadlite.ui.theme.CodePadLiteTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var scaffoldState: ScaffoldState
    private val viewModel: CodeViewModel by viewModels {
        CodeViewModelFactory(
            (application as MyApplication).database
                .codeDao()
        )
    }

    private val editCodeItemListener: (codeItem: Code) -> Unit = {
        viewModel.selectedItem.value = it
        navController.navigate("codePadEditor")
    }

    private val deleteCodeItemListener: (codeItem: Code) -> Unit = {
        viewModel.deleteItem(it)
    }

    private val addCodeItemListener: (codeItem: Code) -> Unit = {
        viewModel.addNewItem(
            codeId = it.codeId,
            codeTitle = it.codeTitle,
            codeDesc = it.codeDesc,
            codeContent = it.codeContent,
            codeLang = it.codeLang,
            codeFav = it.codeFav
        )
        navController.popBackStack()
        showSnackBarMessageListener("Item Added Successfully.")
    }

    private val addToFavCodeItemListener: (codeItem: Code) -> Unit = {
        it.codeFav = !it.codeFav
        viewModel.addNewItem(
            codeId = it.codeId,
            codeTitle = it.codeTitle,
            codeDesc = it.codeDesc,
            codeContent = it.codeContent,
            codeLang = it.codeLang,
            codeFav = it.codeFav
        )
        val message =
            if(it.codeFav) "Item Added to Favorite Successfully."
            else "Item Removed From Favorite Successfully."

        if(it.codeFav)
            showSnackBarMessageListener(message)
        else
            showSnackBarMessageListener(message)

    }

    private val getSelectedCodeItemListener: () -> Code? = {
        viewModel.selectedItem.value
    }

    private val showSnackBarMessageListener: (message: String) -> Unit = {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            CodePadLiteTheme {

                scaffoldState = rememberScaffoldState(
                    drawerState = DrawerState(DrawerValue.Closed),
                    snackbarHostState = SnackbarHostState()
                )

                coroutineScope = rememberCoroutineScope()

                navController = rememberNavController()


                NavHost(navController = navController, startDestination = "codePadList") {
                    composable("codePadList") {
                        CodePadList(
                            codePadItems = viewModel.codes.value,
                            editCodeItemListener,
                            deleteCodeItemListener,
                            addToFavCodeItemListener,
                            scaffoldState,
                            coroutineScope
                        )
                    }
                    composable(
                        "codePadEditor"
                    ) {
                        CodePadEditor(
                            getSelectedCodeItemListener,
                            addCodeItemListener,
                            showSnackBarMessageListener
                        )
                    }
                }


            }
        }
    }

    override fun onBackPressed() {
        if(scaffoldState.drawerState.isOpen)
            coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
        else
            super.onBackPressed()
    }

}

@Composable
fun DrawerContent() {
    Column {

        Surface(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colors.primaryVariant
        ) {
            // Header
        }

    }
}

/*

TODO:

1. Recently Used
2. Most Frequently Used
3. Favorite

 */
