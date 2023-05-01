package com.kelnik.htracker.ui.page.add_habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.kelnik.htracker.domain.entity.Habit.Companion.HabitType
import com.kelnik.htracker.domain.entity.Habit.Companion.HabitType.*
import com.kelnik.htracker.ui.theme.*
import com.kelnik.htracker.ui.utils.pxToDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitPage(
    onNavigateToTemplatesHabits: (Int) -> Unit,
    onNavigateToEditHabits: (HabitType) -> Unit,
    viewModel: AddHabitViewModel = hiltViewModel(),
) {
    val viewStates = viewModel.viewStates


    val habitsTypeMap = mapOf(
        REGULAR to Triple(
            stringResource(id = R.string.regular),
            ImageVector.vectorResource(id = R.drawable.ic_regular),
            "Часть ваших повседневных привычек.\nРегулярно помечайте их как выполненные.\nНапример: йога три раза в неделю."
        ),
        HARMFUL to Triple(
            stringResource(id = R.string.harmful),
            ImageVector.vectorResource(id = R.drawable.ic_harmful),
            "Каждый день по умолчанию выполнено.\nНо если вы сорвались, снимите отметку о выполнении.\nПример: бросить курить и пить."
        ),
        DISPOSABLE to Triple(
            stringResource(id = R.string.disposable),
            ImageVector.vectorResource(id = R.drawable.ic_disposable),
            "Напомнит вам о важном разовом событии в выбранную дату.\nПример: сходить к врачу в пятницу."
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        state = viewStates.lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                Modifier
                    .padding(top = LargePadding)
                    .padding(horizontal = LargePadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var widthRow by rememberSaveable {
                    mutableStateOf(0)
                }

                Row(
                    modifier = Modifier
                        .onSizeChanged {
                            widthRow = it.width
                        }
                ) {
                    habitsTypeMap.forEach {
                        val (title, icon, _) = it.value
                        val (contentColor, backgroundColor) = if (viewStates.habitType == it.key)
                            Pair(
                                AppTheme.colors.colorPrimary,
                                AppTheme.colors.colorOnPrimary
                            )
                        else Pair(
                            AppTheme.colors.colorOnPrimary,
                            AppTheme.colors.colorOnPrimary.copy(alpha = 0.1f)
                        )

                        Card(
                            onClick = {
                                viewModel.dispatch(AddHabitViewAction.SetHabitType(it.key))
                            },
                            modifier = if (it.key == HARMFUL) Modifier.padding(horizontal = ExtraSmallPadding) else Modifier,
                            colors = CardDefaults.cardColors(
                                contentColor = contentColor,
                                containerColor = backgroundColor
                            )
                        ) {
                            Column(
                                Modifier
                                    .background(
                                        backgroundColor,
                                        shape = smallRoundedCornerShape
                                    )
                                    .padding(SmallPadding),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = title,
                                    modifier = Modifier
                                        .padding(vertical = MiddlePadding)
                                        .size(LargeIconSize),
                                    tint = contentColor
                                )
                                Text(
                                    text = title.toUpperCase(),
                                    color = contentColor,
                                    style = typography.titleSmall
                                )
                            }
                        }
                    }
                }



                val (title, _, text) = habitsTypeMap[viewStates.habitType]!!

                Column(
                    Modifier
                        .padding(top = SmallPadding)
//                        .padding(horizontal = LargePadding)
                        .background(
                            AppTheme.colors.colorOnPrimary.copy(alpha = 0.1f),
                            shape = smallRoundedCornerShape
                        )
                        .width(widthRow.pxToDp())
                        .padding(MiddlePadding)
                ) {
                    Text(
                        text = title.toUpperCase(),
                        color = AppTheme.colors.colorOnPrimary,
                        style = typography.titleSmall
                    )
                    Text(
                        text = text,
                        color = AppTheme.colors.colorOnPrimary,
                        style = typography.bodySmall,
                        modifier = Modifier.padding(top = SmallPadding)
                    )
                }
            }

        }



        item {
            Button(
                onClick = {
                    onNavigateToEditHabits(viewStates.habitType)
                },
                modifier = Modifier
                    .padding(horizontal = LargePadding)
                    .padding(top = SmallPadding),
                shape = mediumRoundedCornerShape,
                contentPadding = PaddingValues(
                    vertical = SmallPadding,
                    horizontal = LargePadding
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.colorOnPrimary,
                    contentColor = AppTheme.colors.colorPrimary
                )
            ) {
                Text(
                    text = "Создайте привычку".toUpperCase(),
                    style = typography.titleSmall,
                    color = AppTheme.colors.colorPrimary
                )
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = LargePadding)
                    .padding(top = SmallPadding),
                text = stringResource(id = R.string.templates_label).toUpperCase(),
                style = typography.titleSmall,
                color = AppTheme.colors.colorOnPrimary
            )
        }

        items(viewStates.categoryList) {
            Row(
                Modifier
                    .padding(top = SmallPadding)
                    .padding(horizontal = LargePadding)
                    .clickable(
                        indication = rememberRipple(
                            bounded = true,
                            color = AppTheme.colors.colorOnPrimary
                        ),
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) {
                        onNavigateToTemplatesHabits(it.id)
                    }
                    .background(
                        AppTheme.colors.colorOnPrimary.copy(alpha = 0.1f),
                        shape = smallRoundedCornerShape
                    )
                    .padding(
                        start = LargePadding,
                        end = MiddlePadding,
                        top = MiddlePadding,
                        bottom = MiddlePadding
                    )
                    .fillMaxWidth(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = it.iconId),
                    contentDescription = stringResource(id = it.titleStringId),
                    modifier = Modifier
                        .size(LargeIconSize),
                    tint = Color(it.colorRGBA)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(start = MiddlePadding)
                ) {
                    Text(
                        text = stringResource(id = it.titleStringId),
                        color = AppTheme.colors.colorOnPrimary,
                        style = typography.titleSmall
                    )
                    Text(
                        text = stringResource(id = it.descriptionStringId),
                        color = AppTheme.colors.colorOnPrimary,
                        style = typography.bodySmall,
                        modifier = Modifier
                    )
                }
            }
        }

        item {
            Row(modifier = Modifier.padding(vertical = SmallPadding)) {}
        }
    }
}

