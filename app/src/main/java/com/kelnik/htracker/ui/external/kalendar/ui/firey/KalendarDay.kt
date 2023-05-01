package com.himanshoe.kalendar.ui.firey
/*
* MIT License
*
* Copyright (c) 2022 Himanshu Singh
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.himanshoe.kalendar.common.KalendarSelector
import com.himanshoe.kalendar.common.data.KalendarEvent
import com.himanshoe.kalendar.common.theme.KalendarShape
import com.himanshoe.kalendar.common.ui.KalendarDot
import com.kelnik.htracker.ui.theme.AppTheme
import com.kelnik.htracker.ui.theme.transparent
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun KalendarDay(
    size: Dp,
    modifier: Modifier = Modifier,
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    kalendarEvents: List<KalendarEvent>,
    kalendarSelector: KalendarSelector,
    onDayClick: (LocalDate, KalendarEvent?) -> Unit,
) {
    val isDot = kalendarSelector is KalendarSelector.Dot
    val event = kalendarEvents.find { it.date == date }

    Surface(
        color = if (isSelected) AppTheme.colors.colorOnPrimary else transparent,
        shape = KalendarShape.SelectedShape
    ) {
        var localModifier = modifier
            .size(size)
            .clickable { onDayClick(date, event) }

        if (isToday && !isDot) {
            localModifier = localModifier.border(
                width = 2.dp,
                color = AppTheme.colors.colorOnPrimary,
                shape = kalendarSelector.shape
            )
        }

        Column(
            modifier = localModifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.body1,
                    color = if (isSelected) AppTheme.colors.colorPrimary else AppTheme.colors.colorOnPrimary
                )
        }

    }
}

