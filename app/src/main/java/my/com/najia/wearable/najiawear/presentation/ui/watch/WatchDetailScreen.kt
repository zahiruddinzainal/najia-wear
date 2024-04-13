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
@file:OptIn(ExperimentalWearFoundationApi::class)

package my.com.najia.wearable.najiawear.presentation.ui.watch

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import my.com.najia.wearable.najiawear.R
import my.com.najia.wearable.najiawear.data.PrayerTimeValue
import com.google.android.horologist.compose.rotaryinput.rotaryWithScroll

@Composable
fun WatchDetailScreen(
    watchId: Int,
    scrollState: ScrollState
) {
    val viewModel: WatchDetailViewModel =
        viewModel(factory = WatchDetailViewModel.factory(watchId))

    val watch by viewModel.watch
    WatchDetailScreen(
        watch = watch,
        scrollState = scrollState
    )
}

/**
 * Displays the icon, title, and description of the watch model.
 */
@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun WatchDetailScreen(
    watch: PrayerTimeValue?,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    val focusRequester = rememberActiveFocusRequester()
    Column(
        modifier = modifier
            .fillMaxSize()
            .rotaryWithScroll(scrollState, focusRequester)
            .padding(
                top = 26.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 26.dp
            ),
        verticalArrangement = Arrangement.Center
    ) {
        if (watch == null) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally),
                color = Color.White,
                textAlign = TextAlign.Center,
                text = stringResource(R.string.invalid_watch_label)
            )
        } else {
            Icon(
                painter = painterResource(id = watch.icon),
                tint = MaterialTheme.colors.primary,
                contentDescription = stringResource(R.string.watch_icon_content_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(24.dp)
                    .wrapContentSize(align = Alignment.Center)
            )

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                text = watch.name
            )

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                color = Color.White,
                textAlign = TextAlign.Center,
                text = watch.description
            )
        }
    }
}
