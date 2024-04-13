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
package my.com.najia.wearable.najiawear.presentation.ui.landing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import my.com.najia.wearable.najiawear.R
import my.com.najia.wearable.najiawear.presentation.ui.util.ReportFullyDrawn
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnState
import com.google.android.horologist.compose.material.Chip

/**
 * Simple landing page with three actions, view a list of watches, toggle on/off text before the
 * time or view a demo of different user input components.
 *
 * A text label indicates the screen shape and places it at the bottom of the screen.
 * If it's a round device, it will curve the text along the bottom curve. Otherwise, for a square
 * device, it's a regular Text composable.
 */
@Composable
fun LandingScreen(
    columnState: ScalingLazyColumnState,
    onClickWatchList: () -> Unit,
    onClickMap: () -> Unit,
    onClickZone: () -> Unit,
    onClickTheme: () -> Unit,
    onClickAbout: () -> Unit,
    onNavigate: (String) -> Unit,
    proceedingTimeTextEnabled: Boolean,
    onClickProceedingTimeText: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {


    Box(modifier = modifier.fillMaxSize()) {
        // Places both Chips (button and toggle) in the middle of the screen.
        ScalingLazyColumn(
            columnState = columnState,
            modifier = modifier.fillMaxSize()
        ) {
            item {
                // Signify we have drawn the content of the first screen
                ReportFullyDrawn()

                Chip(
                    onClick = onClickWatchList,
                    label = "Prayers",
                    icon = R.drawable.icon_syuruk
                )
            }
//            item {
//                // Signify we have drawn the content of the first screen
//                ReportFullyDrawn()
//
//                Chip(
//                    onClick = onClickMap,
//                    label = "Kiblat Finder",
//                    icon = R.drawable.icon_kaabah
//                )
//            }
//            item {
//                // Signify we have drawn the content of the first screen
//                ReportFullyDrawn()
//
//                Chip(
//                    onClick = onClickMap,
//                    label = "Nearest Mosques",
//                    icon = R.drawable.icon_mosque
//                )
//            }
            item {
                // Signify we have drawn the content of the first screen
                ReportFullyDrawn()

                Chip(
                    onClick = onClickZone,
                    label = "Zones",
                    icon = R.drawable.icon_sajadah
                )
            }

            item {
                // Signify we have drawn the content of the first screen
                ReportFullyDrawn()

                Chip(
                    onClick = onClickTheme,
                    label = "Theme",
                    icon = R.drawable.icon_art
                )
            }

//            item {
//                // Signify we have drawn the content of the first screen
//                ReportFullyDrawn()
//
//                Chip(
//                    onClick = onClickAbout,
//                    label = "About",
//                    icon = R.drawable.icon_art
//                )
//            }

//            item {
//                ToggleChip(
//                    modifier = Modifier.fillMaxWidth(),
//                    checked = proceedingTimeTextEnabled,
//                    onCheckedChanged = onClickProceedingTimeText,
//                    label = stringResource(R.string.proceeding_text_toggle_chip_label),
//                    toggleControl = ToggleChipToggleControl.Switch
//                )
//            }
        }

        // Places curved text at the bottom of round devices and straight text at the bottom of
        // non-round devices.
//        if (LocalConfiguration.current.isScreenRound) {
//            val watchShape = stringResource(R.string.watch_shape)
//            val primaryColor = MaterialTheme.colors.primary
//            CurvedLayout(
//                anchor = 90F,
//                anchorType = AnchorType.Center,
//                modifier = Modifier.fillMaxSize()
//            ) {
//                curvedRow {
//                    curvedText(
//                        text = watchShape,
//                        angularDirection = CurvedDirection.Angular.CounterClockwise,
//                        style = CurvedTextStyle(
//                            fontSize = 18.sp,
//                            color = primaryColor
//                        ),
//                        modifier = CurvedModifier
//                            .radialGradientBackground(
//                                0f to Color.Transparent,
//                                0.2f to Color.DarkGray.copy(alpha = 0.2f),
//                                0.6f to Color.DarkGray.copy(alpha = 0.2f),
//                                0.7f to Color.DarkGray.copy(alpha = 0.05f),
//                                1f to Color.Transparent
//                            )
//                    )
//                }
//            }
//        } else {
//            Row(
//                modifier = Modifier.fillMaxSize(),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.Bottom
//            ) {
//                Text(
//                    modifier = Modifier
//                        .padding(bottom = 2.dp)
//                        .background(
//                            Brush.verticalGradient(
//                                0f to Color.Transparent,
//                                0.3f to Color.DarkGray.copy(alpha = 0.05f),
//                                0.4f to Color.DarkGray.copy(alpha = 0.2f),
//                                0.8f to Color.DarkGray.copy(alpha = 0.2f),
//                                1f to Color.Transparent
//                            )
//                        ),
//                    textAlign = TextAlign.Center,
//                    color = MaterialTheme.colors.primary,
//                    text = stringResource(R.string.watch_shape),
//                    fontSize = 18.sp
//                )
//            }
//        }
    }
}
