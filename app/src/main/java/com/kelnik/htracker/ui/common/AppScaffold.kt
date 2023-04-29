package com.kelnik.htracker.ui.common

import android.os.Parcelable
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.invalidate
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kelnik.htracker.R
import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.domain.entity.Habit.Companion.TargetType
import com.kelnik.htracker.domain.entity.Language
import com.kelnik.htracker.ui.page.add_habits.AddHabitPage
import com.kelnik.htracker.ui.page.edit_habits.EditHabitPage
import com.kelnik.htracker.ui.page.habits.HabitsPage
import com.kelnik.htracker.ui.page.history.HistoryPage
import com.kelnik.htracker.ui.page.settings.SettingsPage
import com.kelnik.htracker.ui.page.splash.SplashPage
import com.kelnik.htracker.ui.page.templates_habits.TemplatesHabitsPage
import com.kelnik.htracker.ui.theme.AppTheme
import com.kelnik.htracker.ui.theme.MiddlePadding
import com.kelnik.htracker.ui.theme.drawerShape
import com.kelnik.htracker.ui.theme.typography
import com.kelnik.htracker.ui.utils.fromJson
import com.kelnik.htracker.ui.widgets.bottom_bar.BottomNavigateBar
import com.kelnik.htracker.ui.widgets.modal_bottom_sheet.*
import com.kelnik.htracker.ui.widgets.top_bar.MainTopBar
import com.kelnik.htracker.ui.widgets.top_bar.StepTopBar
import com.kelnik.htracker.ui.widgets.top_bar.WindowTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime


@Parcelize
sealed class ModalBottomSheetState : Parcelable {
    object Hide : ModalBottomSheetState()
    data class ChooseIcon(val initValue: Int, val setIconId: (Int) -> Unit) :
        ModalBottomSheetState()

    data class ChooseColor(val initValue: Int, val setColorRGBA: (Int) -> Unit) :
        ModalBottomSheetState()

    data class ChooseEventDays(
        val initValue: Set<Habit.Companion.Day>,
        val setEventDays: (Set<Habit.Companion.Day>) -> Unit
    ) :
        ModalBottomSheetState()

    data class ChooseTimeStart(val initValue: LocalTime?,val minValue: LocalTime?, val maxValue: LocalTime?, val setTime: (LocalTime) -> Unit) :
        ModalBottomSheetState()

    data class ChooseTimeEnd(val initValue: LocalTime?,val minValue: LocalTime?, val maxValue: LocalTime?, val setTime: (LocalTime) -> Unit) :
        ModalBottomSheetState()

    data class ChooseTime(val initValue: LocalTime?,val minValue: LocalTime?, val maxValue: LocalTime?, val setTime: (LocalTime) -> Unit) :
        ModalBottomSheetState()

    data class ChooseDate(val initValue: LocalDate?, val setDate: (LocalDate) -> Unit) :
        ModalBottomSheetState()

    data class ChooseTargetType(
        val initValue: TargetType,
        val setTargetType: (TargetType) -> Unit
    ) : ModalBottomSheetState()

    data class ChooseDuration(val initValue: LocalTime?, val setDuration: (LocalTime) -> Unit) :
        ModalBottomSheetState()

    data class ChooseRepeatCount(val initValue: Int?, val setRepeatCount: (Int) -> Unit) :
        ModalBottomSheetState()
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun AppScaffold(
    onThemeChange: (AppTheme.Theme) -> Unit,
    onLanguageChange: (Language) -> Unit,
    lang: String
) {
    var counter = 0
    val navController = rememberAnimatedNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val systemUiCtrl = rememberSystemUiController()
    val systemBarColor = AppTheme.colors.colorPrimary
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    var modalBottomSheetType: ModalBottomSheetState by rememberSaveable {
        mutableStateOf(ModalBottomSheetState.Hide)
    }


    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            when (val action = modalBottomSheetType) {
                is ModalBottomSheetState.ChooseColor -> ChooseColorModalBottomSheet(
                    initValue = action.initValue,
                    callback = action.setColorRGBA,
                    onCancel = { scope.launch { bottomSheetState.hide() }}
                )
                is ModalBottomSheetState.ChooseDate -> ChooseDateModalBottomSheet(
                    action.initValue,
                    action.setDate,
                    onCancel = { scope.launch { bottomSheetState.hide() }}
                )
                is ModalBottomSheetState.ChooseEventDays -> ChooseEventDaysModalBottomSheet(
                    action.initValue,
                    action.setEventDays,
                    onCancel = { scope.launch { bottomSheetState.hide() }}
                )
                is ModalBottomSheetState.ChooseIcon -> ChooseIconModalBottomSheet(
                    action.initValue,
                    action.setIconId,
                    onCancel = { scope.launch { bottomSheetState.hide() }}
                )
                is ModalBottomSheetState.ChooseTime -> {
                    ChooseTimeModalBottomSheet(
                        action.initValue,
                        action.minValue,
                        action.maxValue,
                        action.setTime,
                        onCancel = { scope.launch { bottomSheetState.hide() }}
                    )
                }
                is ModalBottomSheetState.ChooseTimeStart -> {
                    ChooseTimeModalBottomSheet(
                        action.initValue,
                        action.minValue,
                        action.maxValue,
                        action.setTime,
                        onCancel = { scope.launch { bottomSheetState.hide() }}
                    )
                }
                is ModalBottomSheetState.ChooseTimeEnd -> {
                    ChooseTimeModalBottomSheet(
                        action.initValue,
                        action.minValue,
                        action.maxValue,
                        action.setTime,
                        onCancel = { scope.launch { bottomSheetState.hide()  }}
                    )
                }
                is ModalBottomSheetState.ChooseDuration -> ChooseTimeModalBottomSheet(
                    action.initValue,
                    null,
                    null,
                    action.setDuration,
                    onCancel = { scope.launch { bottomSheetState.hide()  }}
                )
                is ModalBottomSheetState.ChooseRepeatCount -> ChooseRepeatCountModalBottomSheet(
                    action.initValue,
                    action.setRepeatCount,
                    onCancel = { scope.launch { bottomSheetState.hide() }}
                )
                is ModalBottomSheetState.ChooseTargetType -> ChooseTargetTypeModalBottomSheet(
                    action.initValue,
                    action.setTargetType,
                    onCancel = { scope.launch { bottomSheetState.hide() }}
                )
                ModalBottomSheetState.Hide -> {}
            }
        },
        sheetShape = RoundedCornerShape(
            topStart = MiddlePadding,
            topEnd = MiddlePadding
        ),
        sheetBackgroundColor = AppTheme.colors.colorPrimary,
        sheetContentColor = AppTheme.colors.colorOnPrimary,
        scrimColor = AppTheme.colors.colorOnPrimary.copy(alpha = 0.4f)
    ) {

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
                            scope.launch {
                                navController.navigateTo(
                                    route = RouteName.ADD_HABITS
                                )
                            }
                        }
                    )
                    RouteName.HABITS -> MainTopBar(
                        stringResource(id = R.string.habits),
                        currentDestination.route!!,
                        onOpenDrawer = { scope.launch { scaffoldState.drawerState.open() } },
                        onNavigateToAddHabits = {
                            scope.launch {
                                navController.navigateTo(
                                    route = RouteName.ADD_HABITS
                                )
                            }
                        }
                    )
                    RouteName.HISTORY -> MainTopBar(
                        stringResource(id = R.string.history),
                        currentDestination.route!!,
                        onOpenDrawer = { scope.launch { scaffoldState.drawerState.open() } },
                        onNavigateToAddHabits = {
                            scope.launch {
                                navController.navigateTo(
                                    route = RouteName.ADD_HABITS
                                )
                            }
                        }
                    )
                    RouteName.SETTINGS -> StepTopBar(
                        title = stringResource(id = R.string.settings),
                        lang = lang,
                    ) { scope.launch { navController.back() } }
                    RouteName.ADD_HABITS -> StepTopBar(
                        title = stringResource(id = R.string.add_habits)
                    ) { scope.launch { navController.back() } }
                    RouteName.TEMPLATES_HABITS + "/{templatesId}" -> {
                        WindowTopBar { scope.launch { navController.back() } }
                    }
                    RouteName.EDIT_HABITS + "/{params}" -> {
                        WindowTopBar { scope.launch { navController.back() } }
                    }
                }
            },
            drawerContentColor = AppTheme.colors.colorOnPrimary,
            drawerContent = {
                MainDrawer(
                    onNavigateToToday = {
                        navController.navigateTo(
                            route = RouteName.TODAY,
                            navConfig = {
                                it.popUpTo(navController.currentBackStack.value[1].destination.route!!) {
                                    inclusive = true
                                    saveState = true
                                }
                                it.restoreState = true
                                it.launchSingleTop = true
                            }
                        )
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    },
                    onNavigateToHabits = {
                        navController.navigateTo(
                            route = RouteName.HABITS,
                            navConfig = {
                                it.popUpTo(navController.currentBackStack.value[1].destination.route!!) {
                                    inclusive = true
                                    saveState = true
                                }
                                it.restoreState = true
                                it.launchSingleTop = true
                            }
                        )
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    },
                    onNavigateToHistory = {
                        navController.navigateTo(
                            route = RouteName.HISTORY,
                            navConfig = {
                                it.popUpTo(navController.currentBackStack.value[1].destination.route!!) {
                                    inclusive = true
                                    saveState = true
                                }
                                it.restoreState = true
                                it.launchSingleTop = true
                            }
                        )
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
            content = { it ->
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
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "TODAY",
                                color = AppTheme.colors.colorOnPrimary,
                                style = typography.titleMedium
                            )
                        }
                    }
                    composable(route = RouteName.HABITS) {
                        HabitsPage(
                            onNavigateToAddHabits = {
                                navController.navigateTo(
                                    route = RouteName.ADD_HABITS,
                                )
                            },
                            onNavigateToEditHabits = {
                                navController.navigateTo(
                                    route = RouteName.EDIT_HABITS,
                                    args = EditHabitsPageParams(it, null)
                                )
                            }
                        )
                    }
                    composable(route = RouteName.HISTORY) {
                        HistoryPage()
                    }
                    composable(route = RouteName.ADD_HABITS) {
                        AddHabitPage(
                            onNavigateToTemplatesHabits = { categoryId ->
                                navController.navigateTo(
                                    route = RouteName.TEMPLATES_HABITS,
                                    args = categoryId
                                )
                            },
                            onNavigateToEditHabits = {
                                navController.navigateTo(
                                    route = RouteName.EDIT_HABITS,
                                    args = EditHabitsPageParams(
                                        null,
                                        when (it) {
                                            Habit.Companion.HabitType.REGULAR -> -1
                                            Habit.Companion.HabitType.HARMFUL -> -2
                                            Habit.Companion.HabitType.DISPOSABLE -> -3
                                        }
                                    )
                                )
                            }
                        )
                    }
                    composable(
                        route = RouteName.EDIT_HABITS + "/{params}",
                        arguments = listOf(navArgument("params") { type = NavType.StringType })
                    ) {
                        val args =
                            it.arguments?.getString("params")?.fromJson<EditHabitsPageParams>()
                        args?.let { (habitId, templateId) ->
                            EditHabitPage(habitId, templateId,
                                onOpenChooseIconModalBottomSheet = { initValue, callback ->
                                    scope.launch {
                                        modalBottomSheetType =
                                            ModalBottomSheetState.ChooseIcon(initValue, callback)
                                        bottomSheetState.show()
                                    }
                                },
                                onOpenChooseIconColorModalBottomSheet = { initValue, callback ->
                                    scope.launch {
                                        modalBottomSheetType =
                                            ModalBottomSheetState.ChooseColor(initValue, callback)
                                        bottomSheetState.show()
                                    }
                                },
                                onOpenChooseEventDayModalBottomSheet = { initValue, callback ->
                                    scope.launch {
                                        modalBottomSheetType =
                                            ModalBottomSheetState.ChooseEventDays(
                                                initValue,
                                                callback
                                            )
                                        bottomSheetState.show()
                                    }
                                },
                                onOpenChooseTimeStartModalBottomSheet = { initValue, minValue, maxValue, callback ->
                                    scope.launch {
                                        modalBottomSheetType =
                                            ModalBottomSheetState.ChooseTimeStart(initValue, minValue, maxValue, callback)
                                        bottomSheetState.show()
                                    }
                                },
                                onOpenChooseTimeEndModalBottomSheet = { initValue, minValue, maxValue, callback ->
                                    scope.launch {
                                        modalBottomSheetType =
                                            ModalBottomSheetState.ChooseTimeEnd(initValue, minValue, maxValue, callback)
                                        bottomSheetState.show()
                                    }
                                },
                                onOpenChooseFinishDateModalBottomSheet = { initValue, callback ->
                                    scope.launch {
                                        modalBottomSheetType =
                                            ModalBottomSheetState.ChooseDate(initValue, callback)
                                        bottomSheetState.show()
                                    }
                                },
                                onOpenChooseTargetType = { initValue, callback ->
                                    scope.launch {
                                        modalBottomSheetType =
                                            ModalBottomSheetState.ChooseTargetType(
                                                initValue,
                                                callback
                                            )
                                        bottomSheetState.show()
                                    }
                                },
                                onOpenChooseRepeatCount = { initValue, callback ->
                                    scope.launch {
                                        modalBottomSheetType =
                                            ModalBottomSheetState.ChooseRepeatCount(
                                                initValue,
                                                callback
                                            )
                                        bottomSheetState.show()
                                    }
                                },
                                onOpenChooseDuration = { initValue, callback ->
                                    scope.launch {
                                        modalBottomSheetType = ModalBottomSheetState.ChooseDuration(
                                            initValue,
                                            callback
                                        )
                                        bottomSheetState.show()
                                    }
                                },
                                onSaveHabit = {
                                    navController.navigateTo(
                                        route = RouteName.ADD_HABITS,
                                        navConfig = {
                                            it.popUpTo(RouteName.ADD_HABITS) {
                                                inclusive = true
                                            }
                                            it.launchSingleTop = true
                                        }
                                    )
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                    composable(
                        route = RouteName.TEMPLATES_HABITS + "/{templatesId}",
                        arguments = listOf(navArgument("templatesId") { type = NavType.IntType })
                    ) {
                        val args = it.arguments?.getInt("templatesId")
                        args?.let { templatesId ->
                            TemplatesHabitsPage(
                                categoryId = templatesId,
                                onNavigateToEditHabits = { templateId ->
                                    navController.navigateTo(
                                        route = RouteName.EDIT_HABITS,
                                        args = EditHabitsPageParams(null, templateId)
                                    )
                                }
                            )
                        }
                    }
                    composable(route = RouteName.SETTINGS) {
                        SettingsPage(onThemeChange, onLanguageChange)
                    }
                    composable(route = RouteName.SPLASH) {
                        SplashPage {
                            navController.navigateTo(
                                route = RouteName.TODAY,
                                navConfig = {
                                    navController.popBackStack()
                                },
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

}