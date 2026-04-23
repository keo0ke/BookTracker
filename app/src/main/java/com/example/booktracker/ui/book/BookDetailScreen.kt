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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
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
import com.example.booktracker.ui.theme.Slate
import com.example.booktracker.ui.theme.TealSoft

private val CoverWidth = 120.dp
private val CoverHeight = 180.dp

@Composable
fun BookDetailScreen(
    book: Book,
    onBack: () -> Unit,
    onStartReading: (Book) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val isReading = book.bookShelf == BookShelf.READING

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
                    modifier = Modifier
                        .clickable(role = Role.Button) { onBack() }
                        .padding(vertical = 4.dp),
                )
            }
        }

        item { BookHeroSection(book, Modifier.padding(horizontal = 20.dp)) }

        item {
            Button(
                onClick = { onStartReading(book) },
                enabled = !isReading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(RadiusMd),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent,
                    contentColor = Sage,
                    disabledContainerColor = TealSoft,
                    disabledContentColor = Slate,
                ),
            ) {
                Text(
                    text = stringResource(
                        if (isReading) R.string.book_already_reading
                        else R.string.book_start_reading,
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
        }

        if (!book.description.isNullOrBlank()) {
            item { DescriptionCard(book.description, Modifier.padding(horizontal = 20.dp)) }
        }

        item { ProgressCard(book.pageCount, Modifier.padding(horizontal = 20.dp)) }

        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun BookHeroSection(book: Book, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val coverModifier = Modifier
            .width(CoverWidth)
            .height(CoverHeight)
            .clip(RoundedCornerShape(RadiusSm))

        if (book.coverUri != null) {
            CoverImage(uri = book.coverUri, modifier = coverModifier, contentDescription = book.title)
        } else {
            Box(modifier = coverModifier.background(Brush.linearGradient(listOf(Ink, Accent))))
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = book.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Text(
            text = book.author,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp),
            textAlign = TextAlign.Center,
        )

        val pagesText = book.pageCount?.let { stringResource(R.string.library_book_pages, it) }
        val isbnText = book.isbn?.let { "ISBN: $it" }
        val metaParts = listOfNotNull(pagesText, isbnText)

        if (metaParts.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(RadiusPill))
                    .background(TealSoft)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            ) {
                Text(
                    text = metaParts.joinToString(" · "),
                    style = MaterialTheme.typography.labelMedium,
                    color = Ink,
                )
            }
        }
    }
}

@Composable
private fun DescriptionCard(description: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RadiusMd),
        colors = CardDefaults.cardColors(containerColor = Sage),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Ink.copy(alpha = 0.12f)),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.book_description_section),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun ProgressCard(pageCount: Int?, modifier: Modifier = Modifier) {
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
            }

            val hint = if (pageCount != null) {
                stringResource(R.string.book_progress_hint_with_pages, pageCount)
            } else {
                stringResource(R.string.book_progress_empty_hint)
            }

            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF93B4BC)
@Composable
private fun BookDetailScreenPreview() {
    BookTrackerTheme {
        BookDetailScreen(
            book = Book(
                title = "Мастер и Маргарита",
                author = "М. Булгаков",
                pageCount = 480,
                isbn = "978-5-389-07455-6",
                description = "Роман о визите дьявола в Москву.",
            ),
            onBack = {},
            onStartReading = {},
        )
    }
}
