package ir.zahra.tasbihat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.zahra.tasbihat.R
import ir.zahra.tasbihat.ui.theme.OverlayDark
import ir.zahra.tasbihat.ui.theme.WarmCream

/**
 * The single horizontal bar that sits at the bottom of the image during
 * the three dhikr stages. About 25% opaque so a soft hint of the photo
 * shows through behind it, and deliberately short so it never competes
 * with the artwork above it.
 */
@Composable
fun BottomInfoBar(
    dhikrText: String,
    currentImageNumber: Int,
    totalImages: Int,
    dotsCount: Int,
    activeDotIndex: Int,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
            .background(OverlayDark)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = dhikrText, color = WarmCream, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            Text(
                text = stringResource(R.string.counter_format, currentImageNumber, totalImages),
                color = WarmCream,
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall
            )
            ProgressDots(
                count = dotsCount,
                activeIndex = activeDotIndex,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = stringResource(R.string.cd_settings_button),
                tint = WarmCream
            )
        }
    }
}
