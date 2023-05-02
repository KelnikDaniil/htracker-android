package com.kelnik.htracker.ui.page.edit_habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kelnik.htracker.R
import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.domain.entity.Habit.Companion.HabitType.*
import com.kelnik.htracker.domain.entity.Habit.Companion.TargetType
import com.kelnik.htracker.ui.theme.*
import com.kelnik.htracker.ui.widgets.CustomTextField
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHabitPage(
    habitId: Int? = null,
    templateId: Int? = null,
    onOpenChooseIconModalBottomSheet: (Int, ((Int) -> Unit)) -> Unit,
    onOpenChooseIconColorModalBottomSheet: (Int, ((Int) -> Unit)) -> Unit,
    onOpenChooseEventDayModalBottomSheet: (Set<Habit.Companion.Day>, ((Set<Habit.Companion.Day>) -> Unit)) -> Unit,
    onOpenChooseTimeStartModalBottomSheet: (LocalTime?, LocalTime?, LocalTime?, ((LocalTime) -> Unit)) -> Unit,
    onOpenChooseTimeEndModalBottomSheet: (LocalTime?, LocalTime?, LocalTime?, ((LocalTime) -> Unit)) -> Unit,
    onOpenChooseFinishDateModalBottomSheet: (LocalDate?, ((LocalDate) -> Unit)) -> Unit,
    onOpenChooseTargetType: (TargetType, ((TargetType) -> Unit)) -> Unit,
    onOpenChooseRepeatCount: (Int?, ((Int) -> Unit)) -> Unit,
    onOpenChooseDuration: (LocalTime?, ((LocalTime) -> Unit)) -> Unit,
    onSaveHabit: () -> Unit,
    viewModel: EditHabitViewModel = hiltViewModel()
) {
    val viewStates = viewModel.viewStates
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.dispatch(EditHabitViewAction.InitParams(habitId, templateId))
    }


    when (viewStates) {
        EditHabitViewState.Failure -> TODO("Ошибка")
        is EditHabitViewState.Init, is EditHabitViewState.Loading -> {
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
        is EditHabitViewState.Loaded -> {
            val habit = viewStates.habit
            var titleTextField by remember {
                mutableStateOf(TextFieldValue(habit.title))
            }
            val focusRequester = remember {
                FocusRequester()
            }

            LaunchedEffect(Unit) {
                delay(100)
                focusRequester.requestFocus()
            }

            LazyColumn(
                state = viewStates.lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CustomTextField(
                            value = titleTextField,
                            onValueChange = { newText ->
                                titleTextField = newText
                                viewModel.dispatch(EditHabitViewAction.SetTitle(newText.text.trim()))
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.title),
                                    color = AppTheme.colors.colorOnPrimary,
                                    style = typography.labelLarge,
                                )
                            },
                            textStyle = typography.labelLarge,
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = AppTheme.colors.colorOnPrimary,
                                backgroundColor = AppTheme.colors.colorPrimary,
                                cursorColor = AppTheme.colors.colorAccent,
                                focusedIndicatorColor = AppTheme.colors.colorOnPrimary,
                                unfocusedIndicatorColor = AppTheme.colors.colorOnPrimary,
                            ),
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .padding(bottom = SmallPadding)
                                .padding(horizontal = LargePadding)
                        )
                    }

                }

                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = when (habit.habitType) {
                                REGULAR -> stringResource(id = R.string.regular_text)
                                HARMFUL -> stringResource(id = R.string.harmful_text)
                                DISPOSABLE -> stringResource(id = R.string.disposable_text)
                            },
                            color = AppTheme.colors.colorOnPrimary,
                            style = typography.labelSmall,
                            modifier = Modifier
                                .padding(top = ExtraSmallPadding)
                                .padding(horizontal = LargePadding)
                        )
                    }

                }

                item {
                    // Выбор иконки и цвета -> (Нижняя панель)
                    Row(
                        Modifier
                            .padding(top = SmallPadding)
                            .padding(horizontal = LargePadding)
                            .background(
                                AppTheme.colors.colorOnPrimary.copy(alpha = 0.1f),
                                shape = smallRoundedCornerShape
                            )
                            .fillMaxWidth(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = habit.iconId),
                            contentDescription = habit.title,
                            modifier = Modifier
                                .padding(
                                    start = LargePadding,
                                    end = MiddlePadding,
                                    top = MiddlePadding,
                                    bottom = MiddlePadding
                                )
                                .size(LargeIconSize),
                            tint = Color(habit.colorRGBA)
                        )
                        Column(
                            modifier = Modifier
                        ) {
                            Row(modifier = Modifier
                                .clickable(
                                    indication = rememberRipple(
                                        bounded = true,
                                        radius = 128.dp,
                                        color = AppTheme.colors.colorOnPrimary
                                    ),
                                    interactionSource = remember {
                                        MutableInteractionSource()
                                    }
                                ) {
                                    focusManager.clearFocus()
                                    onOpenChooseIconModalBottomSheet(habit.iconId) {
                                        viewModel.dispatch(EditHabitViewAction.SetIconId(it))
                                    }
                                }
                                .fillMaxWidth()
                                .padding(start = LargePadding)
                                .padding(vertical = SmallPadding),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(id = R.string.icon),
                                    color = AppTheme.colors.colorOnPrimary,
                                    style = typography.titleMedium
                                )
                                Icon(
                                    ImageVector.vectorResource(id = R.drawable.ic_back),
                                    stringResource(id = R.string.back),
                                    tint = AppTheme.colors.colorOnPrimary,
                                    modifier = Modifier
                                        .padding(end = SmallPadding)
                                        .size(SmallIconSize / 2, SmallIconSize)
                                        .rotate(180f)
                                )
                            }
                            Divider(modifier = Modifier.padding(start = MiddlePadding))
                            Row(modifier = Modifier
                                .clickable(
                                    indication = rememberRipple(
                                        bounded = true,
                                        radius = 128.dp,
                                        color = AppTheme.colors.colorOnPrimary
                                    ),
                                    interactionSource = remember {
                                        MutableInteractionSource()
                                    }
                                ) {
                                    focusManager.clearFocus()
                                    onOpenChooseIconColorModalBottomSheet(habit.colorRGBA) {
                                        viewModel.dispatch(EditHabitViewAction.SetColorRGBA(it))
                                    }
                                }
                                .fillMaxWidth()
                                .padding(start = LargePadding)
                                .padding(vertical = SmallPadding),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(id = R.string.color),
                                    color = AppTheme.colors.colorOnPrimary,
                                    style = typography.titleMedium
                                )
                                Icon(
                                    ImageVector.vectorResource(id = R.drawable.ic_back),
                                    stringResource(id = R.string.back),
                                    tint = AppTheme.colors.colorOnPrimary,
                                    modifier = Modifier
                                        .padding(end = SmallPadding)
                                        .size(SmallIconSize / 2, SmallIconSize)
                                        .rotate(180f)
                                )
                            }
                        }
                    }
                }

                item {
                    when (habit.habitType) {
                        DISPOSABLE -> {}
                        REGULAR, HARMFUL -> {
                            // Повторение
                            Column(
                                modifier = Modifier
                                    .padding(top = MiddlePadding)
                                    .padding(horizontal = LargePadding)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(id = R.string.repeat).toUpperCase(),
                                    color = AppTheme.colors.colorOnPrimary,
                                    style = typography.titleMedium
                                )
                                Row(
                                    modifier = Modifier
                                        .padding(top = MiddlePadding)
                                        .clickable(
                                            indication = rememberRipple(
                                                bounded = true,
                                                radius = (128 + 32 + 16).dp,
                                                color = AppTheme.colors.colorOnPrimary
                                            ),
                                            interactionSource = remember {
                                                MutableInteractionSource()
                                            }
                                        ) {
                                            focusManager.clearFocus()
                                            onOpenChooseEventDayModalBottomSheet(habit.daysOfRepeat) {
                                                viewModel.dispatch(
                                                    EditHabitViewAction.SetDaysOfRepeat(
                                                        it
                                                    )
                                                )
                                            }
                                        }
                                        .background(
                                            AppTheme.colors.colorOnPrimary.copy(alpha = 0.1f),
                                            shape = smallRoundedCornerShape
                                        )
                                        .padding(
                                            vertical = MiddlePadding
                                        )
                                        .padding(start = MiddlePadding)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.habits_day),
                                        color = AppTheme.colors.colorOnPrimary,
                                        style = typography.titleMedium
                                    )
                                    Text(
                                        text = habit.daysOfRepeat.sorted().map {
                                            when (it) {
                                                Habit.Companion.Day.MONDAY -> stringResource(id = R.string.weekday_mon).toLowerCase()
                                                Habit.Companion.Day.TUESDAY -> stringResource(id = R.string.weekday_tue).toLowerCase()
                                                Habit.Companion.Day.WEDNESDAY -> stringResource(id = R.string.weekday_wed).toLowerCase()
                                                Habit.Companion.Day.THURSDAY -> stringResource(id = R.string.weekday_thu).toLowerCase()
                                                Habit.Companion.Day.FRIDAY -> stringResource(id = R.string.weekday_fri).toLowerCase()
                                                Habit.Companion.Day.SATURDAY -> stringResource(id = R.string.weekday_sat).toLowerCase()
                                                Habit.Companion.Day.SUNDAY -> stringResource(id = R.string.weekday_sun).toLowerCase()
                                            }
                                        }.joinToString(", "),
                                        color = AppTheme.colors.colorOnPrimary,
                                        style = typography.labelSmall
                                    )
                                    Icon(
                                        ImageVector.vectorResource(id = R.drawable.ic_back),
                                        stringResource(id = R.string.back),
                                        tint = AppTheme.colors.colorOnPrimary,
                                        modifier = Modifier
                                            .padding(end = SmallPadding)
                                            .size(SmallIconSize / 2, SmallIconSize)
                                            .rotate(180f)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    when (habit.habitType) {
                        HARMFUL -> {}
                        REGULAR, DISPOSABLE -> {

                            // Выполнить в интервале
                            Column(
                                modifier = Modifier
                                    .padding(top = MiddlePadding)
                                    .padding(horizontal = LargePadding)
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    text = stringResource(id = R.string.execute_in_interval).toUpperCase(),
                                    color = AppTheme.colors.colorOnPrimary,
                                    style = typography.titleMedium
                                )
                                Row(
                                    modifier = Modifier
                                        .padding(top = MiddlePadding)
                                        .background(
                                            AppTheme.colors.colorOnPrimary.copy(alpha = 0.1f),
                                            shape = smallRoundedCornerShape
                                        )
                                        .padding(
                                            vertical = MiddlePadding
                                        )
                                        .padding(start = MiddlePadding)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.between) + " ",
                                        color = AppTheme.colors.colorOnPrimary,
                                        style = typography.titleMedium
                                    )
                                    Card(
                                        onClick = {
                                            focusManager.clearFocus()
                                            onOpenChooseTimeStartModalBottomSheet(
                                                habit.startExecutionInterval,
                                                null,
                                                habit.endExecutionInterval
                                            ) {
                                                viewModel.dispatch(
                                                    EditHabitViewAction.SetStartExecutionInterval(
                                                        it
                                                    )
                                                )
                                            }
                                        },
                                        colors = CardDefaults.cardColors(
                                            containerColor = AppTheme.colors.colorOnPrimary,
                                            contentColor = AppTheme.colors.colorPrimary
                                        ),
                                        modifier = Modifier.padding(horizontal = SmallPadding)
                                    ) {
                                        Text(
                                            text = habit.startExecutionInterval.toString(),
                                            style = typography.titleLarge,
                                            color = AppTheme.colors.colorPrimary,
                                            modifier = Modifier.padding(ExtraSmallPadding)
                                        )
                                    }

                                    Text(
                                        text = " " + stringResource(id = R.string.and) + " ",
                                        color = AppTheme.colors.colorOnPrimary,
                                        style = typography.titleMedium
                                    )
                                    Card(
                                        onClick = {
                                            focusManager.clearFocus()
                                            onOpenChooseTimeEndModalBottomSheet(
                                                habit.endExecutionInterval,
                                                habit.startExecutionInterval,
                                                null
                                            ) {
                                                viewModel.dispatch(
                                                    EditHabitViewAction.SetEndExecutionInterval(
                                                        it
                                                    )
                                                )
                                            }
                                        },
                                        colors = CardDefaults.cardColors(
                                            containerColor = AppTheme.colors.colorOnPrimary,
                                            contentColor = AppTheme.colors.colorPrimary
                                        ),
                                        modifier = Modifier.padding(horizontal = SmallPadding)
                                    ) {
                                        Text(
                                            text = habit.endExecutionInterval.toString(),
                                            style = typography.titleLarge,
                                            color = AppTheme.colors.colorPrimary,
                                            modifier = Modifier.padding(ExtraSmallPadding)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    when (habit.habitType) {
                        REGULAR, HARMFUL, DISPOSABLE -> {
                            // Окончание
                            Column(
                                modifier = Modifier
                                    .padding(top = MiddlePadding)
                                    .padding(horizontal = LargePadding)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(id = R.string.deadline).toUpperCase(),
                                    color = AppTheme.colors.colorOnPrimary,
                                    style = typography.titleMedium
                                )
                                Row(
                                    modifier = Modifier
                                        .padding(top = MiddlePadding)
                                        .clickable(
                                            indication = rememberRipple(
                                                bounded = true,
                                                radius = (128 + 32 + 16).dp,
                                                color = AppTheme.colors.colorOnPrimary
                                            ),
                                            interactionSource = remember {
                                                MutableInteractionSource()
                                            }
                                        ) {
                                            focusManager.clearFocus()
                                            onOpenChooseFinishDateModalBottomSheet(habit.deadline) {
                                                viewModel.dispatch(
                                                    EditHabitViewAction.SetDeadline(
                                                        it
                                                    )
                                                )
                                            }
                                        }
                                        .background(
                                            AppTheme.colors.colorOnPrimary.copy(alpha = 0.1f),
                                            shape = smallRoundedCornerShape
                                        )
                                        .padding(
                                            vertical = MiddlePadding
                                        )
                                        .padding(start = MiddlePadding)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.deadline_title),
                                        color = AppTheme.colors.colorOnPrimary,
                                        style = typography.titleMedium
                                    )
                                    habit.deadline?.let {
                                        Text(
                                            text = it.format(
                                                DateTimeFormatter.ofPattern(
                                                    stringResource(id = R.string.date_2_pattern)
                                                )
                                            )
                                                .toString(),
                                            color = AppTheme.colors.colorOnPrimary,
                                            style = typography.titleSmall
                                        )
                                    }

                                    Icon(
                                        ImageVector.vectorResource(id = R.drawable.ic_back),
                                        stringResource(id = R.string.back),
                                        tint = AppTheme.colors.colorOnPrimary,
                                        modifier = Modifier
                                            .padding(end = SmallPadding)
                                            .size(SmallIconSize / 2, SmallIconSize)
                                            .rotate(180f)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    when (habit.habitType) {
                        DISPOSABLE -> {}
                        REGULAR, HARMFUL -> {
                            //    val targetType: TargetType = TargetType.OFF,
                            //    val repeatCount: Int?,
                            //    val duration: LocalTime?,
                            // Цель на день
                            Column(
                                modifier = Modifier
                                    .padding(top = MiddlePadding)
                                    .padding(horizontal = LargePadding)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(id = R.string.daily_goal).toUpperCase(),
                                    color = AppTheme.colors.colorOnPrimary,
                                    style = typography.titleMedium
                                )
                                Row(
                                    modifier = Modifier
                                        .padding(top = MiddlePadding)
                                        .clickable(
                                            indication = rememberRipple(
                                                bounded = true,
                                                radius = (128 + 32 + 16).dp,
                                                color = AppTheme.colors.colorOnPrimary
                                            ),
                                            interactionSource = remember {
                                                MutableInteractionSource()
                                            }
                                        ) {
                                            focusManager.clearFocus()
                                            onOpenChooseTargetType(habit.targetType) {
                                                viewModel.dispatch(
                                                    EditHabitViewAction.SetTargetType(
                                                        it
                                                    )
                                                )
                                            }
                                        }
                                        .background(
                                            AppTheme.colors.colorOnPrimary.copy(alpha = 0.1f),
                                            shape = smallRoundedCornerShape
                                        )
                                        .padding(
                                            vertical = MiddlePadding
                                        )
                                        .padding(start = MiddlePadding)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.target),
                                        color = AppTheme.colors.colorOnPrimary,
                                        style = typography.titleMedium
                                    )
                                    Text(
                                        text = when (habit.targetType) {
                                            TargetType.OFF -> stringResource(id = R.string.target_type_off)
                                            TargetType.REPEAT -> stringResource(id = R.string.target_type_repeat)
                                            TargetType.DURATION -> stringResource(id = R.string.target_type_duration)
                                        },
                                        color = AppTheme.colors.colorOnPrimary,
                                        style = typography.titleSmall,
                                        modifier = Modifier.padding(start = LargePadding * 3)
                                    )
                                    Icon(
                                        ImageVector.vectorResource(id = R.drawable.ic_back),
                                        stringResource(id = R.string.back),
                                        tint = AppTheme.colors.colorOnPrimary,
                                        modifier = Modifier
                                            .padding(end = SmallPadding)
                                            .size(SmallIconSize / 2, SmallIconSize)
                                            .rotate(180f)
                                    )
                                }

                                when (habit.targetType) {
                                    TargetType.OFF -> {}
                                    TargetType.REPEAT -> {
                                        Row(
                                            modifier = Modifier
                                                .padding(top = MiddlePadding)
                                                .clickable(
                                                    indication = rememberRipple(
                                                        bounded = true,
                                                        radius = (128 + 32 + 16).dp,
                                                        color = AppTheme.colors.colorOnPrimary
                                                    ),
                                                    interactionSource = remember {
                                                        MutableInteractionSource()
                                                    }
                                                ) {
                                                    focusManager.clearFocus()
                                                    onOpenChooseRepeatCount(habit.repeatCount) {
                                                        viewModel.dispatch(
                                                            EditHabitViewAction.SetRepeatCount(
                                                                it
                                                            )
                                                        )
                                                    }
                                                }
                                                .background(
                                                    AppTheme.colors.colorOnPrimary.copy(alpha = 0.1f),
                                                    shape = smallRoundedCornerShape
                                                )
                                                .padding(
                                                    vertical = MiddlePadding
                                                )
                                                .padding(start = MiddlePadding)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.choose_repeat_count),
                                                color = AppTheme.colors.colorOnPrimary,
                                                style = typography.titleMedium
                                            )
                                            habit.repeatCount?.let {
                                                Text(
                                                    text = it.toString(),
                                                    color = AppTheme.colors.colorOnPrimary,
                                                    style = typography.titleSmall,
                                                    modifier = Modifier.padding(start = MiddlePadding)
                                                )
                                            }

                                            Icon(
                                                ImageVector.vectorResource(id = R.drawable.ic_back),
                                                stringResource(id = R.string.back),
                                                tint = AppTheme.colors.colorOnPrimary,
                                                modifier = Modifier
                                                    .padding(end = SmallPadding)
                                                    .size(SmallIconSize / 2, SmallIconSize)
                                                    .rotate(180f)
                                            )
                                        }
                                    }
                                    TargetType.DURATION -> {
                                        Row(
                                            modifier = Modifier
                                                .padding(top = MiddlePadding)
                                                .clickable(
                                                    indication = rememberRipple(
                                                        bounded = true,
                                                        radius = (128 + 32 + 16).dp,
                                                        color = AppTheme.colors.colorOnPrimary
                                                    ),
                                                    interactionSource = remember {
                                                        MutableInteractionSource()
                                                    }
                                                ) {
                                                    focusManager.clearFocus()
                                                    onOpenChooseDuration(habit.duration) {
                                                        viewModel.dispatch(
                                                            EditHabitViewAction.SetDuration(
                                                                it
                                                            )
                                                        )
                                                    }
                                                }
                                                .background(
                                                    AppTheme.colors.colorOnPrimary.copy(alpha = 0.1f),
                                                    shape = smallRoundedCornerShape
                                                )
                                                .padding(
                                                    vertical = MiddlePadding
                                                )
                                                .padding(start = MiddlePadding)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.target_type_duration),
                                                color = AppTheme.colors.colorOnPrimary,
                                                style = typography.titleMedium
                                            )
                                            habit.duration?.let {
                                                Text(
                                                    text = it.format(DateTimeFormatter.ofPattern("hh ч. mm м.")),
                                                    color = AppTheme.colors.colorOnPrimary,
                                                    style = typography.titleSmall,
                                                    modifier = Modifier.padding(start = LargePadding)
                                                )
                                            }

                                            Icon(
                                                ImageVector.vectorResource(id = R.drawable.ic_back),
                                                stringResource(id = R.string.back),
                                                tint = AppTheme.colors.colorOnPrimary,
                                                modifier = Modifier
                                                    .padding(end = SmallPadding)
                                                    .size(SmallIconSize / 2, SmallIconSize)
                                                    .rotate(180f)
                                            )
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

                item {
                    // Создать привычку
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (habitId != null) {
                            Button(
                                onClick = {
                                    viewModel.dispatch(EditHabitViewAction.RemoveHabit)
                                    onSaveHabit()
                                },
                                modifier = Modifier
                                    .padding(horizontal = LargePadding)
                                    .padding(top = MiddlePadding),
                                shape = mediumRoundedCornerShape,
                                contentPadding = PaddingValues(
                                    vertical = SmallPadding,
                                    horizontal = LargePadding
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = red500,
                                    contentColor = AppTheme.colors.colorPrimary
                                )
                            ) {
                                Text(
                                    text = stringResource(id = R.string.remove).toUpperCase(),
                                    style = typography.titleSmall,
                                    color = AppTheme.colors.colorPrimary
                                )
                            }
                        }

                        Button(
                            enabled = !viewStates.habit.title.isEmpty(),
                            onClick = {
                                viewModel.dispatch(EditHabitViewAction.SaveHabit)
                                onSaveHabit()
                            },
                            modifier = Modifier
                                .padding(horizontal = LargePadding)
                                .padding(top = MiddlePadding),
                            shape = mediumRoundedCornerShape,
                            contentPadding = PaddingValues(
                                vertical = SmallPadding,
                                horizontal = LargePadding
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppTheme.colors.colorOnPrimary,
                                contentColor = AppTheme.colors.colorPrimary,
                                disabledContentColor = AppTheme.colors.colorPrimary.copy(alpha = 0.2f),
                                disabledContainerColor = AppTheme.colors.colorOnPrimary.copy(alpha = 0.2f),
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.save).toUpperCase(),
                                style = typography.titleSmall,
                                color = AppTheme.colors.colorPrimary
                            )
                        }
                    }

                }

                item {
                    Row(modifier = Modifier.padding(vertical = SmallPadding)) {}
                }
            }
        }
    }
}