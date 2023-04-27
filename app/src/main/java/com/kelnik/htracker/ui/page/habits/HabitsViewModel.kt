package com.kelnik.htracker.ui.page.habits

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelnik.htracker.domain.entity.EventNotification
import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.domain.interactor.EventNotificationUseCase
import com.kelnik.htracker.domain.interactor.HabitUseCase
import com.kelnik.htracker.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitUseCase: HabitUseCase,
    private val eventNotificationUseCase: EventNotificationUseCase
) : ViewModel() {
    var viewStates by mutableStateOf<HabitsViewState>(
        HabitsViewState.Loading
    )
        private set

    fun dispatch(action: HabitsViewAction) {
        when (action) {
            is HabitsViewAction.InitHabits -> initHabits()
        }
    }

    private fun initHabits() {
        viewModelScope.launch {
            when (val habits = habitUseCase.getHabitList()) {
                is Resource.Failure -> viewStates = HabitsViewState.Failure
                is Resource.Success -> {
                    viewStates = HabitsViewState.Loaded(
                        habits.data.map {
                            HabitUI(
                                habit = it,
                                eventNotificationList = when (val eventNotificationList =
                                    eventNotificationUseCase.getEventNotificationListForHabit(it.id)) {
                                    is Resource.Failure -> listOf()
                                    is Resource.Success -> eventNotificationList.data
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

data class HabitUI(
    val habit: Habit,
    val eventNotificationList: List<EventNotification>
)

sealed class HabitsViewState {
    object Loading : HabitsViewState()
    object Failure : HabitsViewState()
    data class Loaded(val habitList: List<HabitUI>) : HabitsViewState()
}

sealed class HabitsViewAction {
    object InitHabits : HabitsViewAction()
}