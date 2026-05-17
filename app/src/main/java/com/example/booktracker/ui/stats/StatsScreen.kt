package com.example.booktracker.ui.stats

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.booktracker.data.model.ProgressEntry
import com.example.booktracker.ui.theme.Accent
import com.example.booktracker.ui.theme.Ink
import com.example.booktracker.ui.theme.RadiusMd
import com.example.booktracker.ui.theme.RadiusSm
import com.example.booktracker.ui.theme.Sage

private val MaxBarHeight = 120.dp

@Composable
fun StatsScreen(
    progress: List<ProgressEntry>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    // Превращаем сырые отметки в записи лога с количеством прочитанных страниц.
    val log = remember(progress) { buildLog(progress) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column(Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Статистика чтения",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "Сколько страниц прочитано по дням и история отметок.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }

        if (log.isEmpty()) {
            item {
                Text(
                    text = "Пока нет отметок. Откройте книгу «в чтении» и укажите текущую страницу.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )
            }
        } else {
            item {
                PagesPerDayChart(log, Modifier.padding(horizontal = 20.dp))
            }
            item {
                Text(
                    text = "Изменения прогресса",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
            items(log, key = { it.entry.id }) { logItem ->
                ProgressRow(logItem, Modifier.padding(horizontal = 20.dp))
            }
        }

        item { Spacer(Modifier.height(24.dp)) }
    }
}

/** Столбчатая диаграмма: за каждый день — сумма всех логов (прочитанных страниц) этого дня. */
@Composable
private fun PagesPerDayChart(log: List<LogItem>, modifier: Modifier = Modifier) {
    val perDay = remember(log) { pagesPerDay(log) }
    if (perDay.isEmpty()) return

    val maxPages = perDay.maxOf { it.second }.coerceAtLeast(1)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RadiusMd),
        colors = CardDefaults.cardColors(containerColor = Sage),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Ink.copy(alpha = 0.12f)),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Прочитано страниц по дням",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                perDay.forEach { (day, pages) ->
                    val barHeight = (MaxBarHeight.value * pages / maxPages).coerceAtLeast(6f).dp
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = pages.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(Modifier.height(4.dp))
                        Box(
                            Modifier
                                .width(28.dp)
                                .height(barHeight)
                                .clip(RoundedCornerShape(RadiusSm))
                                .background(Accent),
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = formatDay(day),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressRow(logItem: LogItem, modifier: Modifier = Modifier) {
    val entry = logItem.entry
    // Процент = сколько прочитано в этой записи от всего объёма книги.
    val percent = entry.bookPageCount
        ?.takeIf { it > 0 }
        ?.let { (logItem.pagesRead * 100 / it).coerceIn(0, 100) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(RadiusMd))
            .background(Sage)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Маркер записи в стиле таймлайна — свой цвет для каждой книги.
        Box(
            Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(bookColor(entry.bookId)),
        )
        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = entry.bookTitle.ifBlank { "Без названия" },
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = formatTimestamp(entry.recordedAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
        Spacer(Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "прочитано ${logItem.pagesRead} стр.",
                style = MaterialTheme.typography.labelLarge,
                color = Ink,
            )
            percent?.let {
                Text(
                    text = "$it% книги",
                    style = MaterialTheme.typography.labelSmall,
                    color = Accent,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
    }
}

/** Запись лога: отметка + сколько страниц прочитано с предыдущей отметки этой книги. */
private data class LogItem(
    val entry: ProgressEntry,
    val pagesRead: Int,
)

/**
 * Превращает сырые отметки в лог. Для каждой отметки считаем прирост страниц
 * относительно предыдущей отметки той же книги (это и есть «прочитано»).
 */
private fun buildLog(entries: List<ProgressEntry>): List<LogItem> {
    val items = mutableListOf<LogItem>()
    entries.groupBy { it.bookId }.forEach { (_, bookEntries) ->
        var previousPage = 0
        bookEntries.sortedBy { it.recordedAt }.forEach { entry ->
            val pagesRead = (entry.page - previousPage).coerceAtLeast(0)
            items += LogItem(entry, pagesRead)
            previousPage = entry.page
        }
    }
    // Свежие записи — сверху.
    return items.sortedByDescending { it.entry.recordedAt }
}

/** День -> сумма всех логов (прочитанных страниц) за этот день. */
private fun pagesPerDay(log: List<LogItem>): List<Pair<String, Int>> {
    val totals = sortedMapOf<String, Int>()
    log.forEach { logItem ->
        val day = logItem.entry.recordedAt.substringBefore('T')
        if (day.isNotBlank()) {
            totals[day] = (totals[day] ?: 0) + logItem.pagesRead
        }
    }
    return totals.map { it.key to it.value }
}

/**
 * Цвет маркера книги. Вычисляется из bookId, поэтому стабилен в рамках устройства
 * и не требует хранения в БД. Угол 137° (близок к золотому) даёт хорошо различимые оттенки.
 */
private fun bookColor(bookId: Long): Color {
    val hue = ((bookId * 137) % 360).toFloat()
    return Color.hsv(hue, saturation = 0.55f, value = 0.80f)
}

/** "2026-05-17" -> "17.05". */
private fun formatDay(date: String): String {
    val parts = date.split('-')
    return if (parts.size == 3) "${parts[2]}.${parts[1]}" else date
}

/**
 * "2026-05-17T19:30:45.123" -> "17.05.2026 19:30".
 * Без java.time, чтобы не зависеть от desugaring при minSdk 24.
 */
private fun formatTimestamp(iso: String): String {
    if (iso.isBlank()) return ""
    return runCatching {
        val date = iso.substringBefore('T')
        val time = iso.substringAfter('T', "").take(5)
        val (year, month, day) = date.split('-')
        if (time.isBlank()) "$day.$month.$year" else "$day.$month.$year $time"
    }.getOrDefault(iso)
}
