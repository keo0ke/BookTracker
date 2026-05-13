package com.example.booktracker.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.booktracker.ui.theme.Accent
import com.example.booktracker.ui.theme.Ink
import com.example.booktracker.ui.theme.RadiusMd
import com.example.booktracker.ui.theme.RadiusPill
import com.example.booktracker.ui.theme.RadiusSm
import com.example.booktracker.ui.theme.Sage
import com.example.booktracker.ui.theme.Slate
import com.example.booktracker.ui.theme.TealSoft

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    vm: AuthViewModel = viewModel(),
) {
    val state by vm.state.collectAsState()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val isLogin = state.mode == AuthMode.LOGIN
    val title = if (isLogin) "Вход" else "Регистрация"
    val submitLabel = if (isLogin) "Войти" else "Зарегистрироваться"
    val switchHint = if (isLogin) "Нет аккаунта? Зарегистрироваться" else "Уже есть аккаунт? Войти"

    val fieldShape = RoundedCornerShape(RadiusSm)
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Accent,
        unfocusedBorderColor = Ink.copy(alpha = 0.18f),
        focusedLabelColor = Accent,
        unfocusedLabelColor = Ink.copy(alpha = 0.45f),
        cursorColor = Accent,
        focusedContainerColor = Sage,
        unfocusedContainerColor = Sage,
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = fieldShape,
            colors = fieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = fieldShape,
            colors = fieldColors,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        if (state.errorMessage != null) {
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(RadiusSm))
                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f))
                    .padding(12.dp),
            ) {
                Text(
                    text = state.errorMessage!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { vm.submit(email, password) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(RadiusMd),
            colors = ButtonDefaults.buttonColors(
                containerColor = Accent,
                contentColor = Sage,
                disabledContainerColor = TealSoft,
                disabledContentColor = Slate,
            ),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.height(20.dp),
                    color = Sage,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = submitLabel,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = switchHint,
                style = MaterialTheme.typography.bodyMedium,
                color = Accent,
                modifier = Modifier
                    .clip(RoundedCornerShape(RadiusPill))
                    .clickable(role = Role.Button) {
                        vm.switchMode(if (isLogin) AuthMode.REGISTER else AuthMode.LOGIN)
                    }
                    .padding(8.dp),
            )
        }
    }
}
