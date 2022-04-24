package com.coding.codepadlite

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.coding.codepadlite.application.MyApplication
import com.coding.codepadlite.room.Code
import com.coding.codepadlite.ui.CodePadEditor
import com.coding.codepadlite.ui.CodePadList
import com.coding.codepadlite.ui.theme.CodePadLiteTheme
import com.coding.codepadlite.viewmodel.CodeViewModel
import com.coding.codepadlite.viewmodel.CodeViewModelFactory
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

    // Listener
    private val showCodeItemListener: (codeItem: Code) -> Unit = {
        viewModel.selectedItem = it
        navController.navigate("codePadEditor") {
            launchSingleTop = true
        }
    }
    private val deleteCodeItemListener: (codeItem: Code) -> Unit = {
        viewModel.deleteItem(it)
    }
    private val addCodeItemListener: (codeItem: Code) -> Unit = {
        viewModel.addNewItem(it)
        navController.popBackStack()
        showSnackBarMessageListener("Item Saved Successfully.")
    }
    private val addToFavCodeItemListener: (codeItem: Code) -> Unit = {
        it.codeFav = !it.codeFav
        viewModel.addNewItem(it)
        val message =
            if(it.codeFav) "Item Added to Favorite Successfully."
            else "Item Removed From Favorite Successfully."

        if(it.codeFav)
            showSnackBarMessageListener(message)
        else
            showSnackBarMessageListener(message)

    }
    private val getSelectedCodeItemListener: () -> Code? = {
        viewModel.selectedItem
    }
    private val showSnackBarMessageListener: (message: String) -> Unit = {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
        }
    }
    private val copyCodeItemListener: (codeItem: Code) -> Unit = {
        val clipboard =
            getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", it.codeContent)
        clipboard.setPrimaryClip(clip)
        showSnackBarMessageListener("Item Copied Successfully")
    }
    private val shareCodeItemListener: (codeItem: Code) -> Unit = {
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, it.codeContent)
            putExtra(Intent.EXTRA_TITLE, it.codeTitle)
        }, "Share With Friends")
        startActivity(share)
    }
    private val toggleSearchBarListener = {
        viewModel.topBarVisible.value = !viewModel.topBarVisible.value
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
                            favCodes = viewModel.favCodes,
                            topBarVisible = viewModel.topBarVisible,
                            scaffoldState = scaffoldState,
                            coroutineScope = coroutineScope,
                            showCodeItemListener = showCodeItemListener,
                            deleteCodeItemListener = deleteCodeItemListener,
                            addToFavCodeItemListener = addToFavCodeItemListener,
                            copyCodeItemListener = copyCodeItemListener,
                            shareCodeItemListener = shareCodeItemListener,
                            toggleSearchBarListener = toggleSearchBarListener
                        )
                    }
                    composable(
                        "codePadEditor"
                    ) {
                        CodePadEditor(
                            showSnackBarMessageListener = showSnackBarMessageListener,
                            getSelectedCodeItemListener = getSelectedCodeItemListener,
                            addCodeItemListener = addCodeItemListener,
                            copyCodeItemListener = copyCodeItemListener,
                            shareCodeItemListener = shareCodeItemListener
                        )
                    }
                }


            }
        }
    }

    override fun onBackPressed() {
        when {
            scaffoldState.drawerState.isOpen -> coroutineScope.launch {
                scaffoldState.drawerState.close()
            }
            !viewModel.topBarVisible.value -> viewModel.topBarVisible.value = true
            viewModel.favCodes.value -> viewModel.favCodes.value = false
            else -> super.onBackPressed()
        }
    }

}

@Composable
fun DrawerContent(
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    favCodes: MutableState<Boolean>
) {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {

        Surface(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth(),
            color = Color(1f, 0f, 1f, 0.5f)
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 8.dp, 8.dp, 0.dp),
                    text = "CodePad Lite",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 8.dp, 8.dp, 0.dp),
                    text = "Save All Your Codes in One Place",
                    fontSize = 16.sp
                )
            }
        }

        Surface(
            modifier = Modifier
                .padding(8.dp, 8.dp, 8.dp, 0.dp)
                .fillMaxWidth()
                .clickable {
                    favCodes.value = false
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
            shape = MaterialTheme.shapes.medium,
            color = if(!favCodes.value) Color(1f, 0.2f, 1f, 0.5f) else Color.Transparent
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                text = "Home"
            )
        }

        Surface(
            modifier = Modifier
                .padding(8.dp, 8.dp, 8.dp, 8.dp)
                .fillMaxWidth()
                .clickable {
                    favCodes.value = true
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
            shape = MaterialTheme.shapes.medium,
            color = if(favCodes.value) Color(1f, 0.2f, 1f, 0.5f) else Color.Transparent
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                text = "Favorites"
            )
        }

        Divider(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp))

        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            text = "Version 1.0",
            fontSize = 12.sp
        )

    }
}

/*

TODO:

1. Recently Used
2. Most Frequently Used
3. Favorite --DONE

 */
