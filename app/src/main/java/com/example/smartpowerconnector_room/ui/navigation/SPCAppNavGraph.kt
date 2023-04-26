package com.example.smartpowerconnector_room.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.smartpowerconnector_room.ui.routinescreens.IntermediateDestination
import com.example.smartpowerconnector_room.ui.routinescreens.IntermediateScreen
import com.example.inventory.ui.routines.*
import com.example.smartpowerconnector_room.ui.device.*
import com.example.smartpowerconnector_room.home.HomeScreen
import com.example.smartpowerconnector_room.ui.routinescreens.clock.ClockRoutineDetailsDestination
import com.example.smartpowerconnector_room.ui.routinescreens.clock.ClockRoutineDetailsScreen
import com.example.smartpowerconnector_room.ui.routinescreens.clock.ClockRoutineEntryDestination
import com.example.smartpowerconnector_room.ui.routinescreens.clock.ClockRoutineEntryScreen
import com.example.smartpowerconnector_room.ui.routinescreens.mixed.MixRoutineEntryDestination
import com.example.smartpowerconnector_room.ui.routinescreens.mixed.MixRoutineEntryScreen
import com.example.smartpowerconnector_room.ui.routinescreens.multi.*
import com.example.smartpowerconnector_room.ui.routinescreens.routine.*
import com.example.smartpowerconnector_room.usage.UsageDetailsDestination
import com.example.smartpowerconnector_room.usage.UsageScreen

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
                },
                navigateToUsageScreen = {navController.navigate(UsageDetailsDestination.route)}
            )
        }
        composable(route = AddDeviceDestination.route){
            AddDeviceScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(route = UsageDetailsDestination.route) {
            UsageScreen(
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
                navigateBack = { navController.navigateUp() },
                navigateToIntermediateScreen = {
                    navController.navigate(IntermediateDestination.route)
                },
                navigateToTimerRoutineUpdate = {
                    navController.navigate("${TimerRoutineDetailsDestination.route}/${it}")
                },
                navigateToClockRoutineUpdate = {
                    navController.navigate("${ClockRoutineDetailsDestination.route}/${it}")
                },
                navigateToMultiRoutineUpdate = {
                    navController.navigate("${MultiRoutineDetailsDestination.route}/${it}")
                },
                navigateToMixRoutineUpdate = {
                    navController.navigate("${MixRoutineDetailsDestination.route}/${it}")
                },
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
        composable(
            route = IntermediateDestination.route,
        ) {
            IntermediateScreen(
                navigateBack = {navController.navigateUp()},
                navigateToTimerRoutineEntry = {navController.navigate(TimerRoutineEntryDestination.route)},
                navigateToClockRoutineEntry = {navController.navigate(ClockRoutineEntryDestination.route)},
                navigateToMultiRoutineEntry = {navController.navigate(MultiRoutineEntryDestination.route)},
                navigateToMixRoutineEntry = {navController.navigate(MixRoutineEntryDestination.route)},
            )
        }
        composable(
            route = TimerRoutineEntryDestination.route
        ) {
            TimerRoutineEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },

                )
        }
        composable(
            route = TimerRoutineDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(TimerRoutineDetailsDestination.timerRoutineIdArg) {
                type = NavType.IntType
            })
        ){
            TimerRoutineDetailsScreen(
            navigateBack = { navController.navigateUp() },
            //onNavigateUp = { navController.navigateUp() },
            navigateToEditTimerRoutine = { navController.navigate("${TimerRoutineEditDestination.route}/$it") },
            //navigateToTimerRoutineEntry = {navController.navigate(TimerRoutineEntryDestination.route)},

        )
        }
        composable(
            route = TimerRoutineEditDestination.routeWithArgs,
            arguments = listOf(navArgument(TimerRoutineEditDestination.timerRoutineIdArg) {
                type = NavType.IntType
            })
        ) {
            TimerRoutineEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ClockRoutineEntryDestination.route
        ) {
            ClockRoutineEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
        composable(
            route = ClockRoutineDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ClockRoutineDetailsDestination.clockRoutineIdArg) {
                type = NavType.IntType
            })
        ){
            ClockRoutineDetailsScreen(
            navigateBack = { navController.navigateUp() },
            //onNavigateUp = { navController.navigateUp() },
            navigateToEditClockRoutine = { navController.navigate("${ClockRoutineEditDestination.route}/$it") }
        )
        }

        composable(
            route = ClockRoutineEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ClockRoutineEditDestination.clockRoutineIdArg) {
                type = NavType.IntType
            })
        ) {
            ClockRoutineEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = MultiRoutineEntryDestination.route
        ) {
            MultiRoutineEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
        composable(
            route = MultiRoutineDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(MultiRoutineDetailsDestination.multiRoutineIdArg) {
                type = NavType.IntType
            })
        ){
            MultiRoutineDetailsScreen(
            navigateBack = { navController.navigateUp() },
            //onNavigateUp = { navController.navigateUp() },
            navigateToEditMultiRoutine = { navController.navigate("${MultiRoutineEditDestination.route}/$it") }


        )
        }
        composable(
            route = MultiRoutineEditDestination.routeWithArgs,
            arguments = listOf(navArgument(MultiRoutineEditDestination.multiRoutineIdArg) {
                type = NavType.IntType
            })
        ) {
            MultiRoutineEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = MixRoutineEntryDestination.route
        ) {
            MixRoutineEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
        composable(
            route = MixRoutineDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(MixRoutineDetailsDestination.mixRoutineIdArg) {
                type = NavType.IntType
            })
        ){
            MixRoutineDetailsScreen(
            navigateBack = { navController.navigateUp() },
            //onNavigateUp = { navController.navigateUp() },
            navigateToEditMixRoutine = { navController.navigate("${MixRoutineEditDestination.route}/$it") }
            )
        }
        composable(
            route = MixRoutineEditDestination.routeWithArgs,
            arguments = listOf(navArgument(MixRoutineEditDestination.mixRoutineIdArg) {
                type = NavType.IntType
            })
        ) {
            MixRoutineEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}