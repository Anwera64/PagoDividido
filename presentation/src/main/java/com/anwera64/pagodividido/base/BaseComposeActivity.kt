package com.anwera64.pagodividido.base

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.anwera64.pagodividido.base.compose.AppTheme

abstract class BaseComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme() {
                Content()
            }
        }
    }

    @Composable
    internal abstract fun Content()
}