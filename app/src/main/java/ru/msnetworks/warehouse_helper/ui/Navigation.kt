package ru.msnetworks.warehouse_helper.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.msnetworks.warehouse_helper.MainViewModel
import ru.msnetworks.warehouse_helper.Screen

@Composable
fun Navigation(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.ShipmentsListScreen.route) {
        composable(route = Screen.ShipmentsListScreen.route) {
            ShipmentsListScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(
            route = Screen.ShipmentScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { entry ->
            ShipmentScreen(
                id = entry.arguments?.getInt("id") ?: -1,
                mainViewModel = mainViewModel,
                navController = navController
            )
        }
    }
}