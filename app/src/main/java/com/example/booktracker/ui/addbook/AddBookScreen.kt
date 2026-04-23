package com.example.booktracker.ui.addbook

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.booktracker.R
import com.example.booktracker.data.model.Book
import com.example.booktracker.data.model.BookShelf
import com.example.booktracker.ui.common.CoverImage
import com.example.booktracker.ui.theme.Accent
import com.example.booktracker.ui.theme.BookTrackerTheme
import com.example.booktracker.ui.theme.Ink
import com.example.booktracker.ui.theme.RadiusMd
import com.example.booktracker.ui.theme.RadiusPill
import com.example.booktracker.ui.theme.RadiusSm
import com.example.booktracker.ui.theme.Sage

private val CoverWidth = 120.dp
private val CoverHeight = 180.dp

@Composable
fun AddBookScreen(
    onBack: () -> Unit,
    onSave: (Book) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    var title by rememberSaveable { mutableStateOf("") }
    var author by rememberSaveable { mutableStateOf("") }
    var isbn by rememberSaveable { mutableStateOf("") }
    var pageCount by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var coverUri by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedShelf by rememberSaveable { mutableIntStateOf(BookShelf.WISH.ordinal) }
    var titleError by rememberSaveable { mutableStateOf(false) }
    var authorError by rememberSaveable { mutableStateOf(false) }

    val canSave = title.isNotBlank() && author.isNotBlank()

    val pickCoverLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
        ) { uri: Uri? ->
            uri?.let { coverUri = it.toString() }
        }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column(Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.book_detail_back),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier =
                        Modifier
                            .clickable(role = Role.Button) { onBack() }
                            .padding(vertical = 4.dp),
                )
            }
        }

        item {
            Text(
                text = stringResource(R.string.add_book_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        item {
            CoverPicker(
                coverUri = coverUri,
                onPickCover = {
                    pickCoverLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                },
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        item {
            val fieldShape = RoundedCornerShape(RadiusSm)
            val fieldColors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Accent,
                    unfocusedBorderColor = Ink.copy(alpha = 0.18f),
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    focusedLabelColor = Accent,
                    unfocusedLabelColor = Ink.copy(alpha = 0.45f),
                    cursorColor = Accent,
                    focusedContainerColor = Sage,
                    unfocusedContainerColor = Sage,
                )

            Column(
                Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = false
                    },
                    label = { Text(stringResource(R.string.add_book_field_title)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = fieldShape,
                    colors = fieldColors,
                    isError = titleError,
                    supportingText =
                        if (titleError) {
                            { Text(stringResource(R.string.add_book_field_required)) }
                        } else {
                            null
                        },
                )

                OutlinedTextField(
                    value = author,
                    onValueChange = {
                        author = it
                        authorError = false
                    },
                    label = { Text(stringResource(R.string.add_book_field_author)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = fieldShape,
                    colors = fieldColors,
                    isError = authorError,
                    supportingText =
                        if (authorError) {
                            { Text(stringResource(R.string.add_book_field_required)) }
                        } else {
                            null
                        },
                )

                OutlinedTextField(
                    value = isbn,
                    onValueChange = { isbn = it },
                    label = { Text(stringResource(R.string.add_book_field_isbn)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = fieldShape,
                    colors = fieldColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )

                OutlinedTextField(
                    value = pageCount,
                    onValueChange = { pageCount = it.filter { ch -> ch.isDigit() } },
                    label = { Text(stringResource(R.string.add_book_field_pages)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = fieldShape,
                    colors = fieldColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.add_book_field_description)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    shape = fieldShape,
                    colors = fieldColors,
                )
            }
        }

        item {
            Column(Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = stringResource(R.string.add_book_shelf_label),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                ShelfSelector(
                    selected = BookShelf.entries[selectedShelf],
                    onSelect = { selectedShelf = it.ordinal },
                )
            }
        }

        item {
            Button(
                onClick = {
                    titleError = title.isBlank()
                    authorError = author.isBlank()
                    if (canSave) {
                        onSave(
                            Book(
                                title = title.trim(),
                                author = author.trim(),
                                isbn = isbn.trim().ifBlank { null },
                                coverUri = coverUri,
                                pageCount = pageCount.toIntOrNull(),
                                description = description.trim().ifBlank { null },
                                shelf = BookShelf.entries[selectedShelf].name,
                            ),
                        )
                    }
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(RadiusMd),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Accent,
                        contentColor = Sage,
                    ),
            ) {
                Text(
                    text = stringResource(R.string.add_book_save),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CoverPicker(
    coverUri: String?,
    onPickCover: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        val shape = RoundedCornerShape(RadiusSm)

        if (coverUri != null) {
            CoverImage(
                uri = coverUri,
                contentDescription = stringResource(R.string.add_book_cover_description),
                modifier =
                    Modifier
                        .width(CoverWidth)
                        .height(CoverHeight)
                        .clip(shape)
                        .clickable(role = Role.Image) { onPickCover() },
            )
        } else {
            Box(
                modifier =
                    Modifier
                        .width(CoverWidth)
                        .height(CoverHeight)
                        .clip(shape)
                        .background(Sage)
                        .border(
                            BorderStroke(1.5.dp, Ink.copy(alpha = 0.18f)),
                            shape,
                        )
                        .clickable(role = Role.Button) { onPickCover() },
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.titleLarge,
                        color = Accent,
                    )
                    Text(
                        text = stringResource(R.string.add_book_cover_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = Accent,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ShelfSelector(
    selected: BookShelf,
    onSelect: (BookShelf) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BookShelf.entries.forEach { shelf ->
            val isActive = shelf == selected
            val label =
                when (shelf) {
                    BookShelf.READING -> stringResource(R.string.library_tab_reading)
                    BookShelf.FINISHED -> stringResource(R.string.library_tab_finished)
                    BookShelf.WISH -> stringResource(R.string.library_tab_wish)
                }

            val shape = RoundedCornerShape(RadiusPill)
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .clip(shape)
                        .background(if (isActive) Ink else Sage.copy(alpha = 0.65f))
                        .then(
                            if (isActive) {
                                Modifier.border(1.dp, Ink, shape)
                            } else {
                                Modifier.border(1.dp, Ink.copy(alpha = 0.1f), shape)
                            },
                        )
                        .clickable(role = Role.Button) { onSelect(shelf) }
                        .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isActive) Sage else Accent,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF93B4BC)
@Composable
private fun AddBookScreenPreview() {
    BookTrackerTheme {
        AddBookScreen(onBack = {}, onSave = {})
    }
}
