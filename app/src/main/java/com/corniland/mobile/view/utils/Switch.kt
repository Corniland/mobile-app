package com.corniland.mobile.view.utils

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.corniland.mobile.view.theme.CornilandTheme

@Composable
fun SwitchWithLabel(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(start = 48.dp, end = 48.dp, top = 16.dp, bottom = 16.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.weight(1f))
        Switch(
            color = CornilandTheme.colors.primary,
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}