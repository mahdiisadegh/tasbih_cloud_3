package ir.zahra.tasbihat.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ir.zahra.tasbihat.ui.theme.DotActive
import ir.zahra.tasbihat.ui.theme.DotInactive

/**
 * Delicate, hollow progress dots. Only the dot for the currently shown
 * image is filled/bright; the change animates smoothly as [activeIndex]
 * changes. Uses a LazyRow so long stages (34 dots) stay lightweight and
 * the active dot can be scrolled into view.
 */
@Composable
fun ProgressDots(
    count: Int,
    activeIndex: Int,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    LaunchedEffect(activeIndex) {
        listState.animateScrollToItem(maxOf(0, activeIndex - 4))
    }
    LazyRow(modifier = modifier, state = listState) {
        items(count) { index ->
            Dot(isActive = index == activeIndex)
        }
    }
}

@Composable
private fun Dot(isActive: Boolean) {
    val size by animateDpAsState(
        targetValue = if (isActive) 8.dp else 6.dp,
        animationSpec = tween(220),
        label = "dotSize"
    )
    Row(modifier = Modifier.padding(horizontal = 3.dp)) {
        Box(
            modifier = if (isActive) {
                Modifier.size(size).clip(CircleShape).background(DotActive)
            } else {
                Modifier.size(size).clip(CircleShape).border(1.dp, DotInactive, CircleShape)
            }
        )
    }
}
