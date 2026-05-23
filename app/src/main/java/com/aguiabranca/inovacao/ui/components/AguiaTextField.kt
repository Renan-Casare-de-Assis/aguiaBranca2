package com.aguiabranca.inovacao.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.aguiabranca.inovacao.ui.theme.*

@Composable
fun AguiaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    helperText: String? = null,
    errorText: String? = null,
    isPassword: Boolean = false,
    maxLength: Int? = null,
    showCharCounter: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val isError = errorText != null

    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Gray700,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = {
                if (maxLength == null || it.length <= maxLength) onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Gray400
                )
            },
            visualTransformation = if (isPassword && !passwordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            minLines = minLines,
            maxLines = if (minLines > 1) 10 else maxLines,
            enabled = enabled,
            isError = isError,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Brand500,
                unfocusedBorderColor = Gray300,
                errorBorderColor = Danger500,
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                errorContainerColor = White,
                cursorColor = Brand700
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Gray900)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when {
                isError -> Text(
                    text = errorText!!,
                    style = MaterialTheme.typography.labelSmall,
                    color = Danger500
                )
                helperText != null -> Text(
                    text = helperText,
                    style = MaterialTheme.typography.labelSmall,
                    color = Gray500
                )
                else -> Spacer(modifier = Modifier.weight(1f))
            }

            if (showCharCounter && maxLength != null) {
                Text(
                    text = "${value.length} / $maxLength",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (value.length >= maxLength) Danger500 else Gray400
                )
            }
        }
    }
}

