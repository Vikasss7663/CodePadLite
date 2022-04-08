package com.coding.codepadlite.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.coding.codepadlite.R
import com.coding.codepadlite.room.Code
import com.coding.codepadlite.room.getDummyCodeItem

@Composable
fun EmptyCodePadList(editCodeItemListener: (codeItem: Code) -> Unit) {

    // remember lottie composition ,which
    // accepts the lottie composition result
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(R.raw.empty_box)
    )

    // to control the animation
    val progress by animateLottieCompositionAsState(
        // pass the composition created above
        composition,

        // Iterates Forever
        iterations = LottieConstants.IterateForever,

        // pass isPlaying we created above,
        // changing isPlaying will recompose
        // Lottie and pause/play
        isPlaying = true,

        // pass speed we created above,
        // changing speed will increase Lottie
        speed = 1f,

        // this makes animation to restart when paused and play
        // pass false to continue the animation at which is was paused
        restartOnPlay = false

    )

    // Column Composable
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LottieAnimation(
            composition,
            progress,
            modifier = Modifier
                .weight(1f)
        )
        
        Text(
            text = "No Item Added, Please Add Some Item",
            modifier = Modifier
                .padding(16.dp),
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = {
                editCodeItemListener(getDummyCodeItem())
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Add Item")
        }

    }

}