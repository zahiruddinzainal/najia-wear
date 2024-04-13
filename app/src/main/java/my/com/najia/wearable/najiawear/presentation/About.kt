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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.Text
import com.google.android.horologist.compose.rotaryinput.rotaryWithScroll

@Composable
fun AboutScreen(
    scrollState: ScalingLazyListState
) {

    AboutScreen(
        scrollState = scrollState
    )
}

/**
 * Displays the icon, title, and description of the watch model.
 */
@OptIn(ExperimentalWearFoundationApi::class)
@Composable
fun AboutScreen(
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
        Text(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally),
            color = Color.White,
            textAlign = TextAlign.Center,
            text = "Najia App Studio"
        )
    }
}
