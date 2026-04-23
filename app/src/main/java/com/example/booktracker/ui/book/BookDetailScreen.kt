package com.example.booktracker.ui.book

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.booktracker.R
import com.example.booktracker.ui.theme.Accent
import com.example.booktracker.ui.theme.BookTrackerTheme
import com.example.booktracker.ui.theme.Ink
import com.example.booktracker.ui.theme.RadiusMd
import com.example.booktracker.ui.theme.RadiusPill
import com.example.booktracker.ui.theme.RadiusSm
import com.example.booktracker.ui.theme.Sage
import com.example.booktracker.ui.theme.Slate
import com.example.booktracker.ui.theme.TealSoft

private val CoverWidth = 120.dp
private val CoverHeight = 180.dp

@Composable
fun BookDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
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
            BookHeroSection(modifier = Modifier.padding(horizontal = 20.dp))
        }

        item {
            ProgressCard(modifier = Modifier.padding(horizontal = 20.dp))
        }

        item {
            VocabularyCard(modifier = Modifier.padding(horizontal = 20.dp))
        }

        item {
            NotesCard(modifier = Modifier.padding(horizontal = 20.dp))
        }

        item {
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun BookHeroSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .width(CoverWidth)
                    .height(CoverHeight)
                    .clip(RoundedCornerShape(RadiusSm))
                    .background(Brush.linearGradient(listOf(Ink, Accent))),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.book_detail_title_placeholder),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.book_detail_author_placeholder),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp),
            textAlign = TextAlign.Center,
        )
        Box(
            modifier =
                Modifier
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(RadiusPill))
                    .background(TealSoft)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
        ) {
            Text(
                text = stringResource(R.string.book_detail_meta_placeholder),
                style = MaterialTheme.typography.labelMedium,
                color = Ink,
            )
        }
    }
}

@Composable
private fun ProgressCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RadiusMd),
        colors = CardDefaults.cardColors(containerColor = Sage),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Ink.copy(alpha = 0.12f)),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.book_progress_section),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                TextButton(onClick = { /* лог сессии — позже */ }) {
                    Text(
                        text = stringResource(R.string.book_progress_log),
                        style = MaterialTheme.typography.labelLarge,
                        color = Accent,
                    )
                }
            }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(RadiusPill))
                            .background(TealSoft),
                )
                Text(
                    text = stringResource(R.string.book_progress_pct_placeholder),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Accent,
                )
            }

            Text(
                text = stringResource(R.string.book_progress_empty_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun VocabularyCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RadiusMd),
        colors = CardDefaults.cardColors(containerColor = Sage),
        border = BorderStroke(1.dp, Ink.copy(alpha = 0.12f)),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.book_vocab_section),
                    style = MaterialTheme.typography.labelSmall,
                    color = Slate,
                )
                TextButton(onClick = { /* открыть словарь книги — позже */ }) {
                    Text(
                        text = stringResource(R.string.book_vocab_open),
                        style = MaterialTheme.typography.labelLarge,
                        color = Accent,
                    )
                }
            }

            Text(
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                text = stringResource(R.string.book_vocab_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Text(
                text = stringResource(R.string.book_vocab_empty_words),
                style = MaterialTheme.typography.labelMedium,
                color = Slate,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(RadiusPill))
                        .background(TealSoft.copy(alpha = 0.55f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                textAlign = TextAlign.Center,
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = { /* добавить слово — позже */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(RadiusMd),
                    border = BorderStroke(1.dp, Ink.copy(alpha = 0.18f)),
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            contentColor = Ink,
                        ),
                ) {
                    Text(
                        stringResource(R.string.book_vocab_add_word),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                Button(
                    onClick = { /* учить набор — позже */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(RadiusMd),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Slate,
                            contentColor = Sage,
                        ),
                ) {
                    Text(
                        stringResource(R.string.book_vocab_study_set),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun NotesCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RadiusMd),
        colors = CardDefaults.cardColors(containerColor = Sage),
        border = BorderStroke(1.dp, Ink.copy(alpha = 0.12f)),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.book_notes_section),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                TextButton(onClick = { /* новая заметка — позже */ }) {
                    Text(
                        text = stringResource(R.string.book_notes_add),
                        style = MaterialTheme.typography.titleMedium,
                        color = Accent,
                    )
                }
            }

            Text(
                text = stringResource(R.string.book_notes_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF93B4BC)
@Composable
private fun BookDetailScreenPreview() {
    BookTrackerTheme {
        BookDetailScreen(onBack = {})
    }
}
