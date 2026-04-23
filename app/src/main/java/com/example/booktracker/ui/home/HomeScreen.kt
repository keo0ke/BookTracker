package com.example.booktracker.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.booktracker.ui.theme.RadiusSm
import com.example.booktracker.ui.theme.Sage
import com.example.booktracker.ui.theme.Slate
import com.example.booktracker.ui.theme.TealSoft

@Composable
fun HomeScreen(
    readingBooks: List<Book>,
    onOpenBook: (Book) -> Unit,
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
                    text = stringResource(R.string.home_today),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = stringResource(R.string.home_greeting),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }

        item {
            AnimatedContent(
                targetState = readingBooks.isNotEmpty(),
                label = "continue_reading",
            ) { hasBooks ->
                if (hasBooks) {
                    ContinueReadingCard(
                        book = readingBooks.first(),
                        onClick = { onOpenBook(readingBooks.first()) },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                } else {
                    ContinueReadingEmptyCard(
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }
        }

        if (readingBooks.size > 1) {
            item {
                Text(
                    text = stringResource(R.string.home_also_reading),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
            items(readingBooks.drop(1), key = { it.id }) { book ->
                ReadingBookCompactCard(
                    book = book,
                    onClick = { onOpenBook(book) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }

        item {
            StatChipsRow(
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        item {
            VocabularyPromoCard(
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        item {
            RecentSessionsEmptyCard(
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        item {
            Spacer(Modifier.height(88.dp))
        }
    }
}

@Composable
private fun ContinueReadingCard(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress = if (book.pageCount != null && book.pageCount > 0) {
        book.currentPage.toFloat() / book.pageCount
    } else {
        0f
    }
    val progressPercent = (progress * 100).toInt()

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RadiusMd),
        colors = CardDefaults.cardColors(containerColor = Sage),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Ink.copy(alpha = 0.12f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            val coverModifier = Modifier
                .width(72.dp)
                .height(108.dp)
                .clip(RoundedCornerShape(RadiusSm))

            if (book.coverUri != null) {
                CoverImage(
                    uri = book.coverUri,
                    modifier = coverModifier,
                    contentDescription = book.title,
                )
            } else {
                Box(
                    modifier = coverModifier.background(
                        Brush.linearGradient(listOf(Ink, Accent)),
                    ),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.home_continue_label),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp),
                )

                Spacer(Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Accent,
                    trackColor = TealSoft,
                    strokeCap = StrokeCap.Round,
                )

                val progressText = if (book.pageCount != null && book.pageCount > 0) {
                    stringResource(
                        R.string.home_continue_progress,
                        progressPercent,
                        book.currentPage,
                        book.pageCount,
                    )
                } else {
                    stringResource(R.string.home_continue_no_pages)
                }

                Text(
                    text = progressText,
                    style = MaterialTheme.typography.labelMedium,
                    color = Slate,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun ReadingBookCompactCard(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RadiusMd),
        colors = CardDefaults.cardColors(containerColor = Sage),
        border = BorderStroke(1.dp, Ink.copy(alpha = 0.12f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val coverModifier = Modifier
                .width(40.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(8.dp))

            if (book.coverUri != null) {
                CoverImage(
                    uri = book.coverUri,
                    modifier = coverModifier,
                    contentDescription = book.title,
                )
            } else {
                Box(
                    modifier = coverModifier.background(
                        Brush.linearGradient(listOf(Ink, Accent)),
                    ),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun ContinueReadingEmptyCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RadiusMd),
        colors = CardDefaults.cardColors(containerColor = Sage),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Ink.copy(alpha = 0.12f)),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .width(72.dp)
                        .height(108.dp)
                        .clip(RoundedCornerShape(RadiusSm))
                        .background(
                            Brush.linearGradient(listOf(Ink, Accent)),
                        ),
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.home_continue_empty_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = stringResource(R.string.home_continue_empty_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun StatChipsRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StatChip(
            value = stringResource(R.string.home_stat_placeholder),
            label = stringResource(R.string.home_stat_streak_label),
            modifier = Modifier.weight(1f),
        )
        StatChip(
            value = stringResource(R.string.home_stat_placeholder),
            label = stringResource(R.string.home_stat_pages_label),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatChip(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(RadiusMd),
        colors = CardDefaults.cardColors(containerColor = Sage),
        border = BorderStroke(1.dp, Ink.copy(alpha = 0.12f)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

@Composable
private fun VocabularyPromoCard(modifier: Modifier = Modifier) {
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
                    text = stringResource(R.string.home_vocab_title),
                    style = MaterialTheme.typography.labelSmall,
                    color = Slate,
                )
                TextButton(onClick = {}) {
                    Text(
                        text = stringResource(R.string.home_vocab_all_words),
                        style = MaterialTheme.typography.labelLarge,
                        color = Accent,
                    )
                }
            }

            Text(
                text = stringResource(R.string.home_vocab_empty_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
            )

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(RadiusMd),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Slate,
                        contentColor = Sage,
                    ),
            ) {
                Text(stringResource(R.string.home_vocab_study_cta))
            }
        }
    }
}

@Composable
private fun RecentSessionsEmptyCard(modifier: Modifier = Modifier) {
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
                    text = stringResource(R.string.home_sessions_title),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                TextButton(onClick = {}) {
                    Text(
                        text = stringResource(R.string.home_sessions_all),
                        style = MaterialTheme.typography.labelLarge,
                        color = Accent,
                    )
                }
            }

            Text(
                text = stringResource(R.string.home_sessions_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF93B4BC)
@Composable
private fun HomeScreenPreview() {
    BookTrackerTheme {
        HomeScreen(
            readingBooks = listOf(
                Book(
                    title = "Мастер и Маргарита",
                    author = "М. Булгаков",
                    pageCount = 480,
                    currentPage = 124,
                    shelf = BookShelf.READING,
                ),
            ),
            onOpenBook = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF93B4BC)
@Composable
private fun HomeScreenEmptyPreview() {
    BookTrackerTheme {
        HomeScreen(readingBooks = emptyList(), onOpenBook = {})
    }
}
