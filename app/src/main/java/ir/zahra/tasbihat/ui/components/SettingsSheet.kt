package ir.zahra.tasbihat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.zahra.tasbihat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    musicEnabled: Boolean,
    dhikrEnabled: Boolean,
    onMusicToggle: (Boolean) -> Unit,
    onDhikrToggle: (Boolean) -> Unit,
    onRestart: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(text = stringResource(R.string.settings_title), style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

            SettingsRow(
                label = stringResource(R.string.settings_music),
                checked = musicEnabled,
                onCheckedChange = onMusicToggle
            )
            SettingsRow(
                label = stringResource(R.string.settings_dhikr),
                checked = dhikrEnabled,
                onCheckedChange = onDhikrToggle
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            TextButton(onClick = onRestart, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.settings_restart))
            }

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}

@Composable
private fun SettingsRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
