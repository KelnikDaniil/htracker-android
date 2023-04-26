package com.kelnik.htracker.ui.page.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kelnik.htracker.ui.theme.AppTheme
import com.kelnik.htracker.ui.theme.MiddlePadding
import com.kelnik.htracker.ui.theme.SmallPadding
import com.kelnik.htracker.ui.theme.typography

@Composable
fun HistoryPage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MiddlePadding)
    ) {
        LazyRow {
            item {
                Column(modifier = Modifier
                    .padding(SmallPadding)
                    .background(Color.Blue, shape = RoundedCornerShape(SmallPadding))
                    .padding(
                        SmallPadding
                    )) {
                    Text(text = "Текущая серия", color = AppTheme.colors.colorPrimary, style = typography.titleMedium)
                    Text(text = "1", color = AppTheme.colors.colorPrimary, style = typography.headlineLarge)
                    Text(text = "Лучшая серия успехов", color = AppTheme.colors.colorPrimary, style = typography.titleMedium)
                }
            }
        }
    }
}