package com.aguiabranca.inovacao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.aguiabranca.inovacao.ui.theme.AguiaBranca2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AguiaBranca2Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // NavGraph será adicionado aqui na Fase 8
                }
            }
        }
    }
}
