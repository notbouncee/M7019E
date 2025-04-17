package com.example.labassignment.ui.files.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TabItem(val title: String, val icon: ImageVector) {
    object Home : TabItem("Home", Icons.Default.Home)
    object Search : TabItem("Search", Icons.Default.Search)
}