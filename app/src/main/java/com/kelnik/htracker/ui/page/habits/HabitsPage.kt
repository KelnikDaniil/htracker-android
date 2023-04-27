package com.kelnik.htracker.ui.page.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kelnik.htracker.R
import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.domain.entity.Habit.Companion.RepeatType.SPECIFIC_DAYS
import com.kelnik.htracker.ui.theme.*
import com.kelnik.htracker.ui.utils.pxToDp

data class TestHabit(
    val title: String,
    val iconId: Int,
    val type: Type,
    val colorIcon: Color,
    val stage: Int,
    val dayList: List<Day>
) {
    data class Day(
        val day: String,
        val dayInt: Int
    )

    enum class Type {
        EVERY_DAY,
    }
}

val testHabitList = listOf(
    TestHabit(
        "Пейте 8 стаканов воды",
        R.drawable.ic_glass,
        TestHabit.Type.EVERY_DAY,
        Color.Red,
        14,
        listOf(
            TestHabit.Day("Чт", 12),
            TestHabit.Day("Пт", 13),
            TestHabit.Day("Сб", 14),
            TestHabit.Day("Вс", 15),
            TestHabit.Day("Пн", 16),
            TestHabit.Day("Вт", 17),
            TestHabit.Day("Ср", 18),
        )
    ),
    TestHabit(
        "Пейте 8 стаканов воды",
        R.drawable.ic_glass,
        TestHabit.Type.EVERY_DAY,
        Color.Red,
        14,
        listOf(
            TestHabit.Day("Чт", 12),
            TestHabit.Day("Пт", 13),
            TestHabit.Day("Сб", 14),
            TestHabit.Day("Вс", 15),
            TestHabit.Day("Пн", 16),
            TestHabit.Day("Вт", 17),
            TestHabit.Day("Ср", 18),
        )
    ),
    TestHabit(
        "Пейте 8 стаканов воды",
        R.drawable.ic_glass,
        TestHabit.Type.EVERY_DAY,
        Color.Red,
        14,
        listOf(
            TestHabit.Day("Чт", 12),
            TestHabit.Day("Пт", 13),
            TestHabit.Day("Сб", 14),
            TestHabit.Day("Вс", 15),
            TestHabit.Day("Пн", 16),
            TestHabit.Day("Вт", 17),
            TestHabit.Day("Ср", 18),
        )
    ),
    TestHabit(
        "Пейте 8 стаканов воды",
        R.drawable.ic_glass,
        TestHabit.Type.EVERY_DAY,
        Color.Red,
        14,
        listOf(
            TestHabit.Day("Чт", 12),
            TestHabit.Day("Пт", 13),
            TestHabit.Day("Сб", 14),
            TestHabit.Day("Вс", 15),
            TestHabit.Day("Пн", 16),
            TestHabit.Day("Вт", 17),
            TestHabit.Day("Ср", 18),
        )
    ),
    TestHabit(
        "Пейте 8 стаканов воды",
        R.drawable.ic_glass,
        TestHabit.Type.EVERY_DAY,
        Color.Red,
        14,
        listOf(
            TestHabit.Day("Чт", 12),
            TestHabit.Day("Пт", 13),
            TestHabit.Day("Сб", 14),
            TestHabit.Day("Вс", 15),
            TestHabit.Day("Пн", 16),
            TestHabit.Day("Вт", 17),
            TestHabit.Day("Ср", 18),
        )
    ),
)


@Composable
fun HabitsPage(
    onNavigateToAddHabits: () -> Unit,
    onNavigateToEditHabits: (Int) -> Unit,
    viewModel: HabitsViewModel = hiltViewModel()
) {
    val viewStates = viewModel.viewStates

    LaunchedEffect(Unit) {
        viewModel.dispatch(HabitsViewAction.InitHabits)
    }

    when (viewStates) {
        HabitsViewState.Failure -> TODO("Неизвестная ошибка")
        HabitsViewState.Loading -> TODO("Загрузка")
        is HabitsViewState.Loaded -> {

            LazyColumn(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(viewStates.habitList) {
                    var elementHeight by remember {
                        mutableStateOf(0)
                    }
                    Column(
                        Modifier
                            .padding(MiddlePadding)
                            .background(
                                AppTheme.colors.colorOnPrimary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(SmallPadding)
                            )
                            .padding(SmallPadding)
                            .fillMaxWidth()
                    ) {
                        Row(
                            Modifier
                                .padding(SmallPadding)
                                .fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(Modifier.height(elementHeight.pxToDp())) {
                                Box(
                                    modifier = Modifier
                                        .padding(end = MiddlePadding)
                                        .background(Color.Green)
                                        .width(ExtraSmallPadding)
                                        .fillMaxHeight()
                                )
                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxHeight()
                                ) {
                                    Text(
                                        text = it.habit.title,
                                        style = typography.headlineMedium,
                                        color = AppTheme.colors.colorOnPrimary
                                    )
                                    when (it.habit.repeatType) {
                                        SPECIFIC_DAYS -> {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .background(
                                                        Color.Green.copy(alpha = 0.2f),
                                                        shape = RoundedCornerShape(ExtraSmallPadding),
                                                    )
                                                    .padding(ExtraSmallPadding)
                                            ) {
                                                Text(
                                                    text = "Каждый день",
                                                    color = Color.Green,
                                                    style = typography.titleSmall
                                                )
                                            }
                                        }
                                        else ->{}
                                    }
                                }
                            }

                            Box(
                                Modifier
                                    .background(
                                        Color(it.habit.colorRGBA).copy(alpha = 0.25f),
                                        shape = RoundedCornerShape(SmallPadding)
                                    )
                                    .onSizeChanged {
                                        if (elementHeight != it.height) {
                                            elementHeight = it.height
                                        }
                                    }
                                    .padding(MiddlePadding),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = it.habit.iconId),
                                    contentDescription = it.habit.title,
                                    tint = Color(it.habit.colorRGBA),
                                    modifier = Modifier.size(SmallIconSize)
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TODO("Парсить и выводить")
//                            it.dayList.forEach {
//                                Column(
//                                    verticalArrangement = Arrangement.Center,
//                                    horizontalAlignment = Alignment.CenterHorizontally,
//                                    modifier = Modifier.padding(horizontal = 3.dp)
//                                ) {
//                                    Text(
//                                        text = it.day,
//                                        color = AppTheme.colors.colorOnPrimary,
//                                        modifier = Modifier.padding(bottom = SmallPadding)
//                                    )
//                                    Box(
//                                        modifier = Modifier
//                                            .background(
//                                                AppTheme.colors.colorOnPrimary.copy(alpha = 0.2f),
//                                                shape = RoundedCornerShape(100.dp)
//                                            )
//                                            .padding(SmallPadding),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Text(
//                                            text = "${it.dayInt}",
//                                            color = AppTheme.colors.colorOnPrimary
//                                        )
//                                    }
//                                }
//                            }
                        }

                        Divider(
                            modifier = Modifier.padding(vertical = SmallPadding),
                            thickness = 2.dp,
                            color = AppTheme.colors.divider
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = SmallPadding)
                        ) {
                            Row() {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_progress_check),
                                    contentDescription = null,
                                    tint = Color.Green
                                )
                                Text(
//                                    text = "${it.stage}%",
                                    text = "11%",
                                    color = Color.Green,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                            Row() {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                                    contentDescription = "Редактировать",
                                    modifier = Modifier
                                        .clickable(
                                            indication = rememberRipple(
                                                bounded = true,
                                                color = AppTheme.colors.colorOnPrimary
                                            ),
                                            interactionSource = remember {
                                                MutableInteractionSource()
                                            }
                                        ) {
                                            onNavigateToEditHabits(1111)
                                        }
                                        .padding(horizontal = 12.dp),
                                    tint = AppTheme.colors.colorOnPrimary
                                )
                            }
                        }

                    }
                }

                item() {
                    Row(Modifier.padding(vertical = MiddlePadding)) {
                        Button(
                            onClick = onNavigateToAddHabits,
                            modifier = Modifier,
                            contentPadding = PaddingValues(
                                vertical = MiddlePadding,
                                horizontal = LargePadding
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppTheme.colors.colorOnPrimary,
                                contentColor = AppTheme.colors.colorPrimary
                            )
                        ) {
                            Text(
                                text = "Создайте новую привычку".toUpperCase(),
                                style = typography.titleMedium,
                                color = AppTheme.colors.colorPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}