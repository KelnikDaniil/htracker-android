package com.kelnik.htracker.ui.widgets.bottom_bar

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kelnik.htracker.ui.common.BottomNavRoute
import com.kelnik.htracker.ui.common.navigateTo
import com.kelnik.htracker.ui.theme.*

@Composable
fun BottomNavigateBar(navController: NavHostController) {
    val bottomNavList = listOf(
        BottomNavRoute.Today,
        BottomNavRoute.Habits,
        BottomNavRoute.History,
    )
    BottomNavigation(
        Modifier
            .height(96.dp),
        backgroundColor = AppTheme.colors.colorSecondary,
        elevation = MediumElevation
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomNavList.forEach { screen ->
            BottomNavigationItem(
                modifier = Modifier.padding(bottom = MiddlePadding),
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = screen.iconId),
                        contentDescription = stringResource(id = screen.stringId),
                        modifier = Modifier
                            .size(MiddleIconSize)
                            .padding(bottom = ExtraSmallPadding)
                    )
                },
                label = {
                    Text(
                        text = stringResource(screen.stringId),
                        style = typography.titleMedium,
                        modifier = Modifier
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.routeName } == true,
                onClick = {
                    navController.navigateTo(
                        route = screen.routeName,
                        isClearBackStack = true
                    )
                },
                selectedContentColor = AppTheme.colors.selectedContent,
                unselectedContentColor = AppTheme.colors.unselectedContent,
            )
        }
    }
}