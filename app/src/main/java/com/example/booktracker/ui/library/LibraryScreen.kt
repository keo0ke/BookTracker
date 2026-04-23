package com.example.booktracker.ui.library

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.booktracker.R
import com.example.booktracker.ui.theme.Accent
import com.example.booktracker.ui.theme.BookTrackerTheme
import com.example.booktracker.ui.theme.Ink
import com.example.booktracker.ui.theme.RadiusMd
import com.example.booktracker.ui.theme.RadiusPill
import com.example.booktracker.ui.theme.Sage

private enum class LibraryShelf {
    READING,
    FINISHED,
    WISH,
}

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onOpenBookDetail: () -> Unit = {},
    showBookDetailShortcut: Boolean = false,
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val shelf = LibraryShelf.entries[selectedIndex]

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column(Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.library_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.height(16.dp))
                LibraryTabsRow(
                    selected = shelf,
                    onSelect = { selectedIndex = it.ordinal },
                )
            }
        }

        item {
            LibraryEmptyState(
                shelf = shelf,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }

        if (showBookDetailShortcut) {
            item {
                TextButton(
                    onClick = onOpenBookDetail,
                    modifier = Modifier.padding(horizontal = 20.dp),
                ) {
                    Text(
                        text = stringResource(R.string.library_link_book_detail),
                        style = MaterialTheme.typography.labelLarge,
                        color = Accent,
                    )
                }
            }
        }

        item {
            Spacer(Modifier.height(88.dp))
        }
    }
}

@Composable
private fun LibraryTabsRow(
    selected: LibraryShelf,
    onSelect: (LibraryShelf) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LibraryShelf.entries.forEach { shelf ->
            val isActive = shelf == selected
            val label =
                when (shelf) {
                    LibraryShelf.READING -> stringResource(R.string.library_tab_reading)
                    LibraryShelf.FINISHED -> stringResource(R.string.library_tab_finished)
                    LibraryShelf.WISH -> stringResource(R.string.library_tab_wish)
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

@Composable
private fun LibraryEmptyState(
    shelf: LibraryShelf,
    modifier: Modifier = Modifier,
) {
    val (titleRes, bodyRes) =
        when (shelf) {
            LibraryShelf.READING ->
                R.string.library_empty_reading_title to R.string.library_empty_reading_body
            LibraryShelf.FINISHED ->
                R.string.library_empty_finished_title to R.string.library_empty_finished_body
            LibraryShelf.WISH ->
                R.string.library_empty_wish_title to R.string.library_empty_wish_body
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RadiusMd),
        colors = CardDefaults.cardColors(containerColor = Sage),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Ink.copy(alpha = 0.12f)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(bodyRes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF93B4BC)
@Composable
private fun LibraryScreenPreview() {
    BookTrackerTheme {
        LibraryScreen()
    }
}
