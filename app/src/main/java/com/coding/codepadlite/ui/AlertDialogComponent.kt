import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.coding.codepadlite.room.Code

@Composable
fun AlertDialogComponent(
    openDialog: MutableState<Boolean>,
    codePadItem: Code,
    itemListener: (codeItem: Code) -> Unit
) {

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "Delete Code") },
            text = {
                Text(
                    "Are you sure want to Delete this Code ?\n\n" +
                            codePadItem.codeTitle
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        itemListener(codePadItem)
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        /* Do some other action */
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }

}