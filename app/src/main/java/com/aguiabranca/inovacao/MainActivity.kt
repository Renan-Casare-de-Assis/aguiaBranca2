package com.aguiabranca.inovacao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.aguiabranca.inovacao.data.session.SessionManager
import com.aguiabranca.inovacao.ui.navigation.AppNavGraph
import com.aguiabranca.inovacao.ui.theme.AguiaTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AguiaTheme {
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController,
                    sessionManager = sessionManager
                )
            }
        }
    }
}
