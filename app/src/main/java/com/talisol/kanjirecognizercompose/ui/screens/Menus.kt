package com.talisol.kanjirecognizercompose.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.talisol.kanjirecognizercompose.R


@Composable
fun DrawingPropertiesMenu(
    modifier: Modifier = Modifier,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onSubmit: () -> Unit,
    onEraseAll: () -> Unit
) {


    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        IconButton(
            onClick = {
                onEraseAll()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_eraser_black_24dp),
                contentDescription = null,
            )
        }


        IconButton(onClick = {
            onUndo()
        }) {
            Icon(
                painterResource(id = R.drawable.ic_baseline_undo_24),
                contentDescription = null,
                tint = Color.LightGray
            )
        }

        IconButton(onClick = {
            onRedo()
        }) {
            Icon(
                painterResource(id = R.drawable.ic_baseline_redo_24),
                contentDescription = null,
                tint = Color.LightGray
            )
        }

        IconButton(onClick = {
            onSubmit()
        }) {
            Icon(
                painter = painterResource(id =  R.drawable.ic_outline_check_24),
                contentDescription = null,
                tint = Color.Black)
        }

    }


}




