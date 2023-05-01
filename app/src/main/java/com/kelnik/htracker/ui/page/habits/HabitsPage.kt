package com.kelnik.htracker.ui.page.habits

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kelnik.htracker.R
import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.domain.entity.Habit.Companion.HabitType.*
import com.kelnik.htracker.ui.theme.*
import com.kelnik.htracker.ui.utils.pxToDp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.max


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HabitsPage(
    onNavigateToAddHabits: () -> Unit,
    onNavigateToEditHabits: (Int) -> Unit,
    viewModel: HabitsViewModel = hiltViewModel()
) {
    val viewStates = viewModel.viewStates

    LaunchedEffect(Unit) {
        if (viewStates is HabitsViewState.Init) {
            viewModel.dispatch(HabitsViewAction.InitHabits)
        }
    }

    when (viewStates) {
        HabitsViewState.Failure -> {
            TODO("Ошибка")
        }
        is HabitsViewState.Loading, is HabitsViewState.Init -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.15f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                CircularProgressIndicator(
                    color = AppTheme.colors.colorOnPrimary,
                    strokeWidth = 2.dp
                )
            }
        }
        is HabitsViewState.Loaded -> {
            LazyColumn(
                state = viewStates.lazyListState,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(viewStates.habitList) { habitUI ->
                    var elementHeight by remember {
                        mutableStateOf(0)
                    }

                    Card(
                        modifier = Modifier
                            .padding(horizontal = MiddlePadding, vertical = ExtraSmallPadding)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = AppTheme.colors.colorOnPrimary.copy(alpha = 0.1f),
                            contentColor = AppTheme.colors.colorOnPrimary
                        ),
                        shape = RoundedCornerShape(ExtraSmallPadding),
                        elevation = CardDefaults.cardElevation(
                        ),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = MiddlePadding)
                                .padding(top = MiddlePadding)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {


                            Row(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                when (habitUI.habit.habitType) {
                                                    REGULAR -> green500
                                                    HARMFUL -> red500
                                                    DISPOSABLE -> blue500
                                                }
                                            )
                                            .width(ExtraSmallPadding / 2)
                                            .height(elementHeight.pxToDp())
                                    ) {}
                                    Column(modifier = Modifier
                                        .padding(horizontal = MiddlePadding)
                                        .onSizeChanged {
                                            if (elementHeight != it.height) {
                                                elementHeight = max(it.height, elementHeight)
                                            }
                                        }
                                    ) {
                                        Text(
                                            text = habitUI.habit.title + when (habitUI.habit.targetType) {
                                                Habit.Companion.TargetType.OFF -> ""
                                                Habit.Companion.TargetType.REPEAT -> " — ${habitUI.habit.repeatCount} раз"
                                                Habit.Companion.TargetType.DURATION -> " — ${
                                                    habitUI.habit.duration!!.format(
                                                        DateTimeFormatter.ofPattern("h ч m мин")
                                                    )
                                                }"
                                            },
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(bottom = ExtraSmallPadding),
                                            style = typography.titleMedium
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {
                                            Card(
                                                shape = RoundedCornerShape(ExtraSmallPadding / 2),
                                                colors = when (habitUI.habit.habitType) {
                                                    REGULAR -> CardDefaults.cardColors(
                                                        contentColor = green500,
                                                        containerColor = green500.copy(alpha = 0.1f)
                                                    )
                                                    HARMFUL -> CardDefaults.cardColors(
                                                        contentColor = red500,
                                                        containerColor = red500.copy(alpha = 0.1f)
                                                    )
                                                    DISPOSABLE -> CardDefaults.cardColors(
                                                        contentColor = blue500,
                                                        containerColor = blue500.copy(alpha = 0.1f)
                                                    )
                                                },
                                                modifier = Modifier.padding(end = SmallPadding)
                                            ) {
                                                Text(
                                                    text = when (habitUI.habit.habitType) {
                                                        REGULAR -> "Регулярная"
                                                        HARMFUL -> "Вредная"
                                                        DISPOSABLE -> "Одноразовая"
                                                    },
                                                    style = typography.labelSmall,
                                                    modifier = Modifier.padding(
                                                        horizontal = ExtraSmallPadding,
                                                        vertical = ExtraSmallPadding / 2
                                                    )
                                                )
                                            }



                                            Card(
                                                shape = RoundedCornerShape(ExtraSmallPadding / 2),
                                                colors = when (habitUI.habit.habitType) {
                                                    REGULAR -> CardDefaults.cardColors(
                                                        contentColor = green500,
                                                        containerColor = green500.copy(alpha = 0.1f)
                                                    )
                                                    HARMFUL -> CardDefaults.cardColors(
                                                        contentColor = red500,
                                                        containerColor = red500.copy(alpha = 0.1f)
                                                    )
                                                    DISPOSABLE -> CardDefaults.cardColors(
                                                        contentColor = blue500,
                                                        containerColor = blue500.copy(alpha = 0.1f)
                                                    )
                                                },
                                                modifier = Modifier.padding(end = SmallPadding)
                                            ) {
                                                Text(
                                                    text = "${habitUI.habit.startExecutionInterval.toString()} - ${habitUI.habit.endExecutionInterval.toString()}",
                                                    style = typography.labelSmall,
                                                    modifier = Modifier.padding(
                                                        horizontal = ExtraSmallPadding,
                                                        vertical = ExtraSmallPadding / 2
                                                    )
                                                )
                                            }

                                            Card(
                                                shape = RoundedCornerShape(ExtraSmallPadding / 2),
                                                colors = when (habitUI.habit.habitType) {
                                                    REGULAR -> CardDefaults.cardColors(
                                                        contentColor = green500,
                                                        containerColor = green500.copy(alpha = 0.1f)
                                                    )
                                                    HARMFUL -> CardDefaults.cardColors(
                                                        contentColor = red500,
                                                        containerColor = red500.copy(alpha = 0.1f)
                                                    )
                                                    DISPOSABLE -> CardDefaults.cardColors(
                                                        contentColor = blue500,
                                                        containerColor = blue500.copy(alpha = 0.1f)
                                                    )
                                                },
                                            ) {
                                                Text(
                                                    text = habitUI.habit.deadline!!.format(
                                                        DateTimeFormatter.ofPattern("d MMMM")
                                                    ),
                                                    style = typography.labelSmall,
                                                    modifier = Modifier.padding(
                                                        horizontal = ExtraSmallPadding,
                                                        vertical = ExtraSmallPadding / 2
                                                    )
                                                )
                                            }

                                        }

                                    }
                                }

                                Card(
                                    shape = RoundedCornerShape(SmallPadding),
//                                    border = BorderStroke(2.dp, Color(habitUI.habit.colorRGBA)),
                                    colors = CardDefaults.cardColors(
                                        contentColor = Color(habitUI.habit.colorRGBA),
                                        containerColor = Color(habitUI.habit.colorRGBA).copy(alpha = 0.2f)
                                    ),
                                    modifier = Modifier
                                        .onSizeChanged {
                                            if (elementHeight != it.height) {
                                                elementHeight = max(it.height, elementHeight)
                                            }
                                        }
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = habitUI.habit.iconId),
                                        contentDescription = habitUI.habit.title,
                                        tint = Color(habitUI.habit.colorRGBA),
                                        modifier = Modifier
                                            .padding(ExtraSmallPadding)
                                            .size(
                                                MiddleIconSize
                                            )
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .padding(ExtraSmallPadding)
                                    .padding(top = MiddlePadding)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val currentDate = LocalDate.now()
                                val list = listOf(
                                    currentDate.minusDays(3),
                                    currentDate.minusDays(2),
                                    currentDate.minusDays(1),
                                    currentDate,
                                    currentDate.plusDays(1),
                                    currentDate.plusDays(2),
                                    currentDate.plusDays(3)
                                )

                                list.forEach {
                                    val date = it
                                    val eventNotification =
                                        habitUI.eventNotificationList.find { it.date.toLocalDate() == date }

                                    val isBad = date.toEpochDay() < currentDate.toEpochDay()
                                    val isDone = eventNotification?.isDone ?: false
                                    val isTarget = eventNotification != null
                                    val isDeadline = date == habitUI.habit.deadline
                                    val isCurrentDate = date == currentDate

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Card(
                                            shape = RoundedCornerShape(ExtraSmallPadding),
                                            colors = CardDefaults.cardColors(
                                                containerColor = when {
                                                    isCurrentDate -> AppTheme.colors.colorOnPrimary
                                                    isDeadline -> Color.Black
                                                    else -> transparent
                                                },
                                                contentColor = when {
                                                    isCurrentDate -> AppTheme.colors.colorPrimary
                                                    isDeadline -> AppTheme.colors.colorPrimary
                                                    else -> AppTheme.colors.colorOnPrimary
                                                },
                                            ),
                                            modifier = Modifier
                                                .size(LargePadding)
                                                .padding(bottom = ExtraSmallPadding),
                                        ) {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = when (it.dayOfWeek!!) {
                                                        DayOfWeek.MONDAY -> "Пн"
                                                        DayOfWeek.TUESDAY -> "Вт"
                                                        DayOfWeek.WEDNESDAY -> "Ср"
                                                        DayOfWeek.THURSDAY -> "Чт"
                                                        DayOfWeek.FRIDAY -> "Пт"
                                                        DayOfWeek.SATURDAY -> "Сб"
                                                        DayOfWeek.SUNDAY -> "Вс"
                                                    },
                                                    style = typography.labelMedium,
                                                    textDecoration = if (isTarget) TextDecoration.Underline else null
                                                )
                                            }
                                        }


                                        Card(
                                            onClick = {
                                                viewModel.dispatch(
                                                    HabitsViewAction.ToggleIsDoneEventNotification(
                                                        eventNotification!!.id
                                                    )
                                                )
                                            },
                                            enabled = isTarget && isBad || isTarget && isCurrentDate,
                                            shape = RoundedCornerShape(SmallPadding),
                                            colors = CardDefaults.cardColors(
                                                containerColor = when {
                                                    isDone -> green500.copy(alpha = 0.1f)
                                                    isTarget && isBad -> red500.copy(alpha = 0.1f)
                                                    else -> AppTheme.colors.colorPrimary
                                                },
                                                contentColor = when {
                                                    isDeadline -> if (isBad) red500 else Color.Black
                                                    else -> AppTheme.colors.colorOnPrimary
                                                },
                                                disabledContainerColor = when {
                                                    isDone -> green500.copy(alpha = 0.1f)
                                                    isTarget && isBad -> red500.copy(alpha = 0.1f)
                                                    else -> AppTheme.colors.colorPrimary
                                                },
                                                disabledContentColor = when {
                                                    isDeadline -> if (isBad) red500 else Color.Black
                                                    else -> AppTheme.colors.colorOnPrimary
                                                },
                                            ),
                                            border = if (isTarget) BorderStroke(
                                                1.dp,
                                                AppTheme.colors.colorOnPrimary
                                            ) else null,
                                            modifier = Modifier
                                                .size(LargePadding + SmallPadding)
                                        ) {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = it.dayOfMonth.toString(),
                                                    style = typography.titleMedium,
                                                    modifier = Modifier
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Divider(
                                thickness = 2.dp,
                                color = AppTheme.colors.colorOnPrimary.copy(alpha = 0.2f),
                                modifier = Modifier.padding(top = ExtraSmallPadding / 2)
                            )

                            // Прогресс (% 1/10), Редактирование
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Row(
                                    modifier = Modifier
                                        .weight(1f),
                                    verticalAlignment = Alignment.CenterVertically

                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_progress_check),
                                        contentDescription = "Прогресс",
                                        tint = green500,
                                        modifier = Modifier
                                            .size(SmallIconSize)
                                    )
                                    val countIsDone =
                                        habitUI.eventNotificationList.filter { it.isDone }.size
                                    val all = habitUI.eventNotificationList.size
                                    val percent =
                                        (if (countIsDone == 0) 0 else countIsDone / all.toDouble() * 100).toInt()
                                    Text(
                                        text = "$percent%",
                                        color = green500,
                                        style = typography.labelMedium,
                                        modifier = Modifier.padding(start = SmallPadding)
                                    )
                                    Text(
                                        text = "$countIsDone/$all",
                                        color = green500,
                                        style = typography.labelSmall,
                                        modifier = Modifier.padding(start = SmallPadding)
                                    )

                                }

                                IconButton(onClick = { onNavigateToEditHabits(habitUI.habit.id) }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                                        contentDescription = "Редактирование",
                                        tint = AppTheme.colors.colorOnPrimary,
                                        modifier = Modifier
                                            .size(SmallIconSize)
                                    )
                                }


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