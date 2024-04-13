/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package my.com.najia.wearable.najiawear.presentation.ui.watchlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Text
import my.com.najia.wearable.najiawear.data.PrayerTimeValue
import my.com.najia.wearable.najiawear.presentation.components.WatchAppChip
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnState
import com.google.android.horologist.compose.material.ToggleChip

@Composable
fun WatchListScreen(
    columnState: ScalingLazyColumnState,
    showVignette: Boolean,
    onClickVignetteToggle: (Boolean) -> Unit,
    onClickWatch: (Int) -> Unit
) {
    val viewModel: WatchListViewModel = viewModel(
        factory = WatchListViewModel.Factory
    )
    val watches by viewModel.watches
    WatchListScreen(
        watches = watches,
        columnState = columnState,
        showVignette = showVignette,
        onClickVignetteToggle = onClickVignetteToggle,
        onClickWatch = onClickWatch
    )
}

/**
 * Displays a list of watches plus a [ToggleChip] at the top to display/hide the Vignette around
 * the screen. The list is powered using a [ScalingLazyColumn].
 */
@Composable
fun WatchListScreen(
    watches: List<PrayerTimeValue>,
    columnState: ScalingLazyColumnState,
    showVignette: Boolean,
    onClickVignetteToggle: (Boolean) -> Unit,
    onClickWatch: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    ScalingLazyColumn(
        modifier = modifier.fillMaxSize(),
        columnState = columnState
    ) {

        item {
            Text(
                text = "Prayers", // Your text here
                fontWeight = FontWeight.Bold,
            )
        }

        // Displays all watches.
        items(watches) { watch ->
            WatchAppChip(
                watchModelNumber = watch.modelId,
                watchName = watch.name,
                watchTime = watch.time,
                watchIcon = watch.icon,
                onClickWatch = onClickWatch
            )
        }
    }
}
