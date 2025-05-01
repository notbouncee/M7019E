package com.example.bingchilling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.bingchilling.database.AppDatabase
import com.example.bingchilling.ui.theme.BingChillingTheme
import com.example.bingchilling.screens.TabItem
import com.example.bingchilling.utils.isNetworkConnected
import com.example.bingchilling.viewmodel.MovieDBViewModel
import com.example.bingchilling.workmanager.MovieSyncWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val syncRequest = PeriodicWorkRequestBuilder<MovieSyncWorker>(6, TimeUnit.HOURS).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "MovieSync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
        enableEdgeToEdge()
        setContent {
            BingChillingTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {

    // tabs + selection
    val tabs = listOf(TabItem.Home, TabItem.Favourites)
    var selectedTab by remember { mutableStateOf<TabItem>(TabItem.Home) }

    // single VM / DB / connectivity
    val viewModel: MovieDBViewModel = viewModel()
    val context = LocalContext.current
    val db      = remember { AppDatabase.getDatabase(context) }
    val isConnected by remember { mutableStateOf(isNetworkConnected(context)) }

    // dummy NavController just to satisfy the signature
    val navController = rememberNavController()


    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            selectedTab.screen(navController, viewModel, db, isConnected)
        }
    }
}
