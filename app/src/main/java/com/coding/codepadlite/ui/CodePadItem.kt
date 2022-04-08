import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coding.codepadlite.room.Code
import com.coding.codepadlite.ui.Chip

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CodePadItem(
    codePadItem: Code,
    editCodeItemListener: (codeItem: Code) -> Unit,
    deleteCodeItemListener: (codeItem: Code) -> Unit,
    addToFavCodeItemListener: (codeItem: Code) -> Unit
) {

    var openDialog = remember {
        mutableStateOf(false)
    }
    var favIcon = remember {
        mutableStateOf(false)
    }
    favIcon.value = codePadItem.codeFav
    // make code pad item
    // show the title then first one/two lines of codes

    // icon you can make based on the language ( optional )
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 0.dp),
        onClick = {
            editCodeItemListener(codePadItem)
        }
    ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(8.dp)
                ){

                    // Show Code Title
                    Text(
                        text = codePadItem.codeTitle,
                        maxLines = 2,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Show Code
                    Text(
                        text = codePadItem.codeContent,
                        modifier = Modifier
                            .fillMaxWidth(),
                        maxLines = 4,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        overflow = TextOverflow.Ellipsis
                    )


                }

                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.End
                ) {

                    // Show Code Language
                    Chip(
                        name = codePadItem.codeLang.toString(),
                        isSelected = true
                    )


                    // Add to Favorite Button
                    IconButton(
                        onClick = {
                            favIcon.value = !codePadItem.codeFav
                            addToFavCodeItemListener(codePadItem)
                        }
                    ) {
                        if(favIcon.value)
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Add Item to Favorite",
                                tint = MaterialTheme.colors.secondary
                            )
                        else
                            Icon(
                                Icons.Filled.FavoriteBorder,
                                contentDescription = "Remove Item From Favorite"
                            )

                    }

                    // Delete Button
                    IconButton(
                        onClick = { openDialog.value = !openDialog.value }
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete Item"
                        )
                    }

                }

            }
    }

    AlertDialogComponent(
        openDialog = openDialog,
        codePadItem,
        deleteCodeItemListener
    )

}