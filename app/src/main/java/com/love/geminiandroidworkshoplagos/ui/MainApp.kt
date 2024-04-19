package com.love.geminiandroid.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.ClosedCaptioning
import compose.icons.fontawesomeicons.regular.Comment
import compose.icons.fontawesomeicons.regular.FileImage


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainApp() {
    val items = listOf(
        FontAwesomeIcons.Regular.ClosedCaptioning to "Text",
        FontAwesomeIcons.Regular.FileImage to "Photo",
        FontAwesomeIcons.Regular.Comment to "Chat"
    )
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomAppBar {
                Icons
                items.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                item.first,
                                contentDescription = item.second,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        label = { Text(item.second) },
                        selected = navController.currentDestination?.route == item.second,
                        onClick = { navController.navigate(item.second) }
                    )
                }
            }
        }
    ) {
        // Navigation destinations
        NavHost(navController = navController, startDestination = "text") {
            composable(route = "text") { TextScreen() }
            composable(route = "Photo") { PhotoScreen() }
            composable(route = "Chat") { ChatScreen() }
        }
    }
}

