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
package my.com.najia.wearable.najiawear.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

internal data class ThemeValues(val description: String, val colors: Colors)

internal val initialThemeValues = ThemeValues(
    "Main Theme",
    Colors(
        primary = Color(0xFFFAF8F2),
        primaryVariant = Color(0xFFFAF8F2),
        secondary = Color(0xFF7FCFFF),
        secondaryVariant = Color(0xFF3998D3)
    )
)

internal val themeValues = listOf(
    initialThemeValues,
    ThemeValues(
        "Blue Theme",
        Colors(
            primary = Color(0xFFE6F0F7),
            primaryVariant = Color(0xFF3998D3),
            secondary = Color(0xFF6DD58C),
            secondaryVariant = Color(0xFF1EA446)
        )
    ),
    ThemeValues(
        "Pink Theme",
        Colors(
            primary = Color(0xFFFCEDF2),
            primaryVariant = Color(0xFF1EA446),
            secondary = Color(0xFFFFBB29),
            secondaryVariant = Color(0xFFD68400)
        )
    )
)
