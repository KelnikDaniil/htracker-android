package com.kelnik.htracker.ui.common

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kelnik.htracker.R
import com.kelnik.htracker.ui.page.add_habits.AddHabitsPage
import com.kelnik.htracker.ui.page.habits.HabitsPage
import com.kelnik.htracker.ui.page.settings.SettingsPage
import com.kelnik.htracker.ui.page.splash.SplashPage
import com.kelnik.htracker.ui.theme.AppTheme
import com.kelnik.htracker.ui.theme.drawerShape
import com.kelnik.htracker.ui.widgets.bottom_bar.BottomNavigateBar
import com.kelnik.htracker.ui.widgets.top_bar.MainTopBar
import com.kelnik.htracker.ui.widgets.top_bar.StepTopBar
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppScaffold(onThemeChange: (AppTheme.Theme) -> Unit) {
    val navController = rememberAnimatedNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val systemUiCtrl = rememberSystemUiController()
    val systemBarColor = AppTheme.colors.colorPrimary
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier,
        scaffoldState = scaffoldState,
        backgroundColor = AppTheme.colors.colorPrimary,
        bottomBar = {
            when (currentDestination?.route) {
                RouteName.TODAY -> BottomNavigateBar(navController = navController)
                RouteName.HABITS -> BottomNavigateBar(navController = navController)
                RouteName.HISTORY -> BottomNavigateBar(navController = navController)
            }
        },
        drawerShape = drawerShape,
        topBar = {
            when (currentDestination?.route) {
                RouteName.TODAY -> MainTopBar(
                    stringResource(id = R.string.today),
                    currentDestination.route!!,
                    onOpenDrawer = { scope.launch { scaffoldState.drawerState.open() } },
                    onNavigateToAddHabits = {
                        navController.navigateTo(
                            route = RouteName.ADD_HABITS
                        )
                    }
                )
                RouteName.HABITS -> MainTopBar(
                    stringResource(id = R.string.habits),
                    currentDestination.route!!,
                    onOpenDrawer = { scope.launch { scaffoldState.drawerState.open() } },
                    onNavigateToAddHabits = {
                        navController.navigateTo(
                            route = RouteName.ADD_HABITS
                        )
                    }
                )
                RouteName.HISTORY -> MainTopBar(
                    stringResource(id = R.string.history),
                    currentDestination.route!!,
                    onOpenDrawer = { scope.launch { scaffoldState.drawerState.open() } },
                    onNavigateToAddHabits = {
                        navController.navigateTo(
                            route = RouteName.ADD_HABITS
                        )
                    }
                )
                RouteName.SETTINGS -> StepTopBar(
                    title = stringResource(id = R.string.settings)
                ) { scope.launch { navController.back() } }
                RouteName.ADD_HABITS -> StepTopBar(
                    title = stringResource(id = R.string.add_habits)
                ) { scope.launch { navController.back() } }
            }
        },
        drawerContentColor = AppTheme.colors.colorOnPrimary,
        drawerContent = {
            MainDrawer(
                onNavigateToToday = {
                    navController.navigateTo(route = RouteName.TODAY, isClearBackStack = true)
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
                onNavigateToHabits = {
                    navController.navigateTo(route = RouteName.HABITS, isClearBackStack = true)
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
                onNavigateToHistory = {
                    navController.navigateTo(route = RouteName.HISTORY, isClearBackStack = true)
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
                onNavigateToSettings = {
                    navController.navigateTo(route = RouteName.SETTINGS)
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
            )
        },
        drawerGesturesEnabled = currentDestination?.route == RouteName.TODAY ||
                currentDestination?.route == RouteName.HISTORY || currentDestination?.route == RouteName.HABITS,
        drawerBackgroundColor = AppTheme.colors.colorPrimary,
        drawerScrimColor = AppTheme.colors.colorOnPrimary.copy(alpha = 0.4f),
        content = {
            AnimatedNavHost(
                modifier = Modifier
                    .padding(it),
                navController = navController,
                startDestination = RouteName.SPLASH,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                composable(route = RouteName.TODAY) {
                    Text(text = "TODAY")

                }
                composable(route = RouteName.HABITS) {
                    HabitsPage {
                        navController.navigateTo(
                            route = RouteName.ADD_HABITS,
                        )
                    }
                }
                composable(route = RouteName.HISTORY) {
                    Text(text = "HISTORY")

                }
                composable(route = RouteName.ADD_HABITS) {
                    AddHabitsPage()
                }
                composable(route = RouteName.SETTINGS) {
                    SettingsPage(onThemeChange)
                }
                composable(route = RouteName.SPLASH) {
                    SplashPage {
                        navController.navigateTo(
                            route = RouteName.TODAY,
                            isClearBackStack = true,
                            sideEffect = {
                                systemUiCtrl.setStatusBarColor(systemBarColor)
                                systemUiCtrl.setNavigationBarColor(systemBarColor)
                                systemUiCtrl.setSystemBarsColor(systemBarColor)
                            })
                    }
                }
            }
        }
    )
}