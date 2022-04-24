import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coding.codepadlite.R
import com.coding.codepadlite.room.Code
import com.coding.codepadlite.ui.Chip

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CodePadItem(
    codePadItem: Code,
    showCodeItemListener: (codeItem: Code) -> Unit,
    deleteCodeItemListener: (codeItem: Code) -> Unit,
    addToFavCodeItemListener: (codeItem: Code) -> Unit,
    copyCodeItemListener: (codeItem: Code) -> Unit,
    shareCodeItemListener: (codeItem: Code) -> Unit
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
            .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
        onClick = {
            showCodeItemListener(codePadItem)
        }
    ) {

        Column() {

            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                // Show Code Title
                Text(
                    text = codePadItem.codeTitle,
                    maxLines = 2,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(8.dp)
                )

                // Show Code Language
                Chip(
                    name = codePadItem.codeLang.toString(),
                    isSelected = true
                )

            }

            // Show Code
            Text(
                text = codePadItem.codeContent,
                maxLines = 4,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End
            ) {

                IconButton(onClick = {
                    copyCodeItemListener(codePadItem)
                }) {
                    Icon(painterResource(id = R.drawable.icon_copy), "Copy")
                }

                IconButton(onClick = {
                    shareCodeItemListener(codePadItem)
                }) {
                    Icon(Icons.Filled.Share, "Share")
                }

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
                            tint = MaterialTheme.colors.primary
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