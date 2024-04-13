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
package my.com.najia.wearable.najiawear.presentation.components

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.curvedText
import androidx.wear.compose.ui.tooling.preview.WearPreviewLargeRound
import androidx.wear.compose.ui.tooling.preview.WearPreviewSmallRound
import androidx.wear.compose.ui.tooling.preview.WearPreviewSquare

/**
 * Custom version of TimeText (Curved Text) that enables leading text (if wanted) and hides while
 * scrolling so user can just focus on the list's items.
 */
@Composable
fun CustomTimeText(
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    startText: String? = null
) {
    val textStyle = TimeTextDefaults.timeTextStyle()
    val debugWarning = remember {
        val isEmulator = Build.PRODUCT.startsWith("sdk_gwear")

        if (my.com.najia.wearable.najiawear.BuildConfig.DEBUG && !isEmulator) {
            "Debug (slower)"
        } else {
            null
        }
    }
    val showWarning = debugWarning != null
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val visibleText = startText != null
        TimeText(
            modifier = modifier,
            startCurvedContent = if (visibleText) {
                {
                    curvedText(
                        text = startText!!,
                        style = CurvedTextStyle(textStyle)
                    )
                }
            } else null,
            startLinearContent = if (visibleText) {
                {
                    Text(
                        text = startText!!,
                        style = textStyle
                    )
                }
            } else null,
            // Trailing text is against Wear UX guidance, used here just for development.
            endCurvedContent = if (showWarning) {
                {
                    curvedText(
                        text = debugWarning!!,
                        style = CurvedTextStyle(textStyle),
                        color = Color.Red
                    )
                }
            } else null,
            endLinearContent = if (showWarning) {
                {
                    Text(
                        text = debugWarning!!,
                        style = textStyle,
                        color = Color.Red
                    )
                }
            } else null
        )
    }
}

@WearPreviewLargeRound
@WearPreviewSquare
@WearPreviewSmallRound
@Composable
fun PreviewCustomTimeText() {
    CustomTimeText(
        visible = true,
        startText = "Testing Leading Text..."
    )
}
