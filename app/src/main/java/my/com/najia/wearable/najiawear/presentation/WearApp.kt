/*
 * Copyright 2022 The Android Open Source Project
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
package my.com.najia.wearable.najiawear.presentation

import Dialogs
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.navscaffold.NavScaffoldViewModel.VignetteMode
import com.google.android.horologist.compose.navscaffold.WearNavScaffold
import com.google.android.horologist.compose.navscaffold.composable
import com.google.android.horologist.compose.navscaffold.scrollStateComposable
import com.google.android.horologist.compose.navscaffold.scrollable
import my.com.najia.wearable.najiawear.R
import my.com.najia.wearable.najiawear.presentation.components.CustomTimeText
import my.com.najia.wearable.najiawear.presentation.navigation.Screen
import my.com.najia.wearable.najiawear.presentation.navigation.WATCH_ID_NAV_ARGUMENT
import my.com.najia.wearable.najiawear.presentation.theme.WearAppTheme
import my.com.najia.wearable.najiawear.presentation.theme.initialThemeValues
import my.com.najia.wearable.najiawear.presentation.theme.themeValues
import my.com.najia.wearable.najiawear.presentation.ui.landing.LandingScreen
import my.com.najia.wearable.najiawear.presentation.ui.map.MapScreen
import my.com.najia.wearable.najiawear.presentation.ui.theme.ThemeScreen
import my.com.najia.wearable.najiawear.presentation.ui.watch.AboutScreen
import my.com.najia.wearable.najiawear.presentation.ui.watch.WatchDetailScreen
import my.com.najia.wearable.najiawear.presentation.ui.watchlist.WatchListScreen

@Composable
fun WearApp(
    modifier: Modifier = Modifier,
    swipeDismissableNavController: NavHostController = rememberSwipeDismissableNavController()
) {
    var themeColors by remember { mutableStateOf(initialThemeValues.colors) }
    WearAppTheme(colors = themeColors) {
        // Allows user to disable the text before the time.
        var showProceedingTextBeforeTime by rememberSaveable { mutableStateOf(false) }

        WearNavScaffold(
            modifier = modifier,
            navController = swipeDismissableNavController,
            startDestination = Screen.Landing.route,
            timeText = {
                CustomTimeText(
                    modifier = it,
                    startText = if (showProceedingTextBeforeTime) {
                        stringResource(R.string.leading_time_text)
                    } else {
                        null
                    }
                )
            }
        ) {
            // Main Window
            scrollable(
                route = Screen.Landing.route,
                columnStateFactory = ScalingLazyColumnDefaults.belowTimeText(
                    firstItemIsFullWidth = true
                )
            ) {
                LandingScreen(
                    columnState = it.columnState,
                    onClickWatchList = {
                        swipeDismissableNavController.navigate(Screen.WatchList.route)
                    },
                    onClickMap = {
                        swipeDismissableNavController.navigate(Screen.Map.route)
                    },
                    onClickZone = {
                        swipeDismissableNavController.navigate(Screen.Dialogs.route)
                    },
                    onClickTheme = {
                        swipeDismissableNavController.navigate(Screen.Theme.route)
                    },
                    onClickAbout = {
                        swipeDismissableNavController.navigate(Screen.About.route)
                    },
                    proceedingTimeTextEnabled = showProceedingTextBeforeTime,
                    onClickProceedingTimeText = {
                        showProceedingTextBeforeTime = !showProceedingTextBeforeTime
                    },
                    onNavigate = { swipeDismissableNavController.navigate(it) }
                )
            }

            scrollable(
                route = Screen.WatchList.route
            ) {
                val vignetteVisible = rememberSaveable { mutableStateOf(true) }

                it.viewModel.vignettePosition = if (vignetteVisible.value) {
                    VignetteMode.On(
                        VignettePosition.TopAndBottom
                    )
                } else {
                    VignetteMode.Off
                }

                WatchListScreen(
                    columnState = it.columnState,
                    showVignette = vignetteVisible.value,
                    onClickVignetteToggle = { showVignette ->
                        vignetteVisible.value = showVignette
                    },
                    onClickWatch = { id ->
                        swipeDismissableNavController.navigate(
                            route = Screen.WatchDetail.route + "/" + id
                        )
                    }
                )
            }

            scrollStateComposable(
                route = Screen.WatchDetail.route + "/{$WATCH_ID_NAV_ARGUMENT}",
                arguments = listOf(
                    navArgument(WATCH_ID_NAV_ARGUMENT) {
                        type = NavType.IntType
                    }
                )
            ) {
                val watchId: Int = it.arguments!!.getInt(WATCH_ID_NAV_ARGUMENT)

                WatchDetailScreen(
                    watchId = watchId,
                    scrollState = it.scrollableState
                )
            }

            scrollable(
                route = Screen.Dialogs.route
            ) {
                val vignetteVisible = rememberSaveable { mutableStateOf(true) }

                it.viewModel.vignettePosition = if (vignetteVisible.value) {
                    VignetteMode.On(
                        VignettePosition.TopAndBottom
                    )
                } else {
                    VignetteMode.Off
                }

                Dialogs(
                    columnState = it.columnState,
                    showVignette = vignetteVisible.value,
                    onClickVignetteToggle = { showVignette ->
                        vignetteVisible.value = showVignette
                    },
                    onClickWatch = { id ->
                        swipeDismissableNavController.navigate(
                            route = Screen.Dialogs.route + "/" + id
                        )
                    }
                )
            }

            scrollable(
                route = Screen.Theme.route
            ) { it ->
                ThemeScreen(
                    columnState = it.columnState,
                    currentlySelectedColors = themeColors,
                    availableThemes = themeValues
                ) { colors -> themeColors = colors }
            }

            scrollable(
                route = Screen.About.route
            ) {
                AboutScreen(
                    scrollState = it.scrollableState
                )
            }


            composable(
                route = Screen.Map.route
            ) {
               MapScreen()
            }


        }
    }
}

@Composable
internal fun menuNameAndCallback(
    onNavigate: (String) -> Unit,
    menuNameResource: Int,
    screen: Screen,
) = MenuItem(stringResource(menuNameResource)) { onNavigate(screen.route) }

data class MenuItem(val name: String, val clickHander: () -> Unit)
