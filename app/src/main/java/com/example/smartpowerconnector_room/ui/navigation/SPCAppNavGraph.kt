package com.example.smartpowerconnector_room.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.smartpowerconnector_room.ui.device.*
import com.example.smartpowerconnector_room.ui.navigation.home.HomeScreen

@Composable
fun SPCNavHost(
    navController: NavHostController,
    modifier: Modifier= Modifier
){
    NavHost(
            navController = navController,
            startDestination= HomeScreen.route,
            modifier= modifier
    ){
        composable(route = HomeScreen.route){
            HomeScreen(
                navigateToDeviceEntry = {navController.navigate(AddDeviceDestination.route)},
                navigateToDeviceUpdate = {
                    navController.navigate("${DeviceDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = AddDeviceDestination.route){
            AddDeviceScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = DeviceDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(DeviceDetailsDestination.deviceIDArg){
                type = NavType.IntType
            })
        ){
            DeviceDetailScreen(
                navigateToEditDevice= { navController.navigate("${DeviceEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = DeviceEditDestination.routeWithArgs,
            arguments = listOf(navArgument(DeviceEditDestination.deviceIDArg){
                type = NavType.IntType
            })
        ){
            EditDeviceScreen(
                navigateBack = {navController.popBackStack()},
                onNavigateUp = {navController.navigateUp()}
            )
        }
    }
}