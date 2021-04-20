package com.components.tools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.text.font.FontFamily
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.sp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            mainUi()
        }
    }

}

@Preview
@Composable
fun mainUi(){
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalGravity = Alignment.CenterHorizontally
    ) {
        Text(text = "AAA1")
        Text(
            text = "AAA2",
            color = Color.Red,
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif
        )
        Text(text = "AAA3", color = Color.Blue)
    }
}