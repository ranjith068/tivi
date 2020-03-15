/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.tivi.common.compose

import androidx.animation.AnimatedFloat
import androidx.compose.Composable
import androidx.ui.core.Layout
import androidx.ui.core.LayoutCoordinates
import androidx.ui.core.RepaintBoundary
import androidx.ui.unit.IntPx
import androidx.ui.unit.PxBounds
import androidx.ui.unit.PxPosition
import androidx.ui.unit.min
import androidx.ui.unit.px
import androidx.ui.unit.toPxSize

/**
 * This is copied from `ui/ui-material/src/main/java/androidx/ui/material/Drawer.kt`
 */
@Composable
fun WithOffset(
    xOffset: AnimatedFloat? = null,
    yOffset: AnimatedFloat? = null,
    child: @Composable() () -> Unit
) {
    Layout(children = {
        RepaintBoundary(children = child)
    }) { measurables, constraints ->
        if (measurables.size > 1) {
            throw IllegalStateException("Only one child is allowed")
        }
        val childMeasurable = measurables.firstOrNull()
        val placeable = childMeasurable?.measure(constraints)
        val width: IntPx
        val height: IntPx
        if (placeable == null) {
            width = constraints.minWidth
            height = constraints.minHeight
        } else {
            width = min(placeable.width, constraints.maxWidth)
            height = min(placeable.height, constraints.maxHeight)
        }
        layout(width, height) {
            val offX = xOffset?.value?.px ?: 0.px
            val offY = yOffset?.value?.px ?: 0.px
            placeable?.place(offX, offY)
        }
    }
}

inline val PxBounds.center: PxPosition
    get() = PxPosition((left + right) / 2, (top + bottom) / 2)

inline val LayoutCoordinates.positionInParent: PxPosition
    get() = parentCoordinates?.childToLocal(this, PxPosition.Origin) ?: PxPosition.Origin

inline val LayoutCoordinates.boundsInParent: PxBounds
    get() = PxBounds(positionInParent, size.toPxSize())
