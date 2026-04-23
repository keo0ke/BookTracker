package com.example.booktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.booktracker.data.model.Book
import com.example.booktracker.ui.MainViewModel
import com.example.booktracker.ui.addbook.AddBookScreen
import com.example.booktracker.ui.book.BookDetailScreen
import com.example.booktracker.ui.home.HomeScreen
import com.example.booktracker.ui.library.LibraryScreen
import com.example.booktracker.ui.theme.Accent
import com.example.booktracker.ui.theme.BookTrackerTheme
import com.example.booktracker.ui.theme.Sage
import com.example.booktracker.ui.theme.TealSoft

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookTrackerTheme {
                val vm: MainViewModel = viewModel()
                val books by vm.books.collectAsState()
                val readingBooks by vm.readingBooks.collectAsState()

                var selectedTab by rememberSaveable { mutableIntStateOf(0) }
                var addBookOpen by rememberSaveable { mutableStateOf(false) }
                var selectedBook by remember { mutableStateOf<Book?>(null) }

                LaunchedEffect(selectedTab) {
                    if (selectedTab != 1) {
                        selectedBook = null
                    }
                }

                val atBookDetail = selectedTab == 1 && selectedBook != null
                val showChrome = !atBookDetail && !addBookOpen

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    bottomBar = {
                        if (showChrome) {
                            MainNavigationBar(
                                selectedIndex = selectedTab,
                                onSelectTab = { selectedTab = it },
                            )
                        }
                    },
                    floatingActionButton = {
                        if (showChrome && (selectedTab == 0 || selectedTab == 1)) {
                            FloatingActionButton(
                                onClick = { addBookOpen = true },
                                containerColor = Accent,
                                contentColor = Sage,
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_fab_add),
                                    contentDescription =
                                        stringResource(R.string.fab_add_content_description),
                                )
                            }
                        }
                    },
                ) { padding ->
                    when {
                        addBookOpen ->
                            AddBookScreen(
                                onBack = { addBookOpen = false },
                                onSave = { book ->
                                    vm.addBook(book)
                                    addBookOpen = false
                                    selectedTab = 1
                                },
                                contentPadding = padding,
                            )

                        selectedTab == 0 ->
                            HomeScreen(
                                readingBooks = readingBooks,
                                onOpenBook = { book ->
                                    selectedBook = book
                                    selectedTab = 1
                                },
                                contentPadding = padding,
                            )

                        selectedTab == 1 ->
                            if (selectedBook != null) {
                                BookDetailScreen(
                                    book = selectedBook!!,
                                    onBack = { selectedBook = null },
                                    contentPadding = padding,
                                )
                            } else {
                                LibraryScreen(
                                    books = books,
                                    contentPadding = padding,
                                    onOpenBookDetail = { book -> selectedBook = book },
                                )
                            }

                        else ->
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .padding(padding),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = stringResource(R.string.placeholder_screen_soon),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                    }
                }
            }
        }
    }
}

private data class NavDestination(
    val iconRes: Int,
    val labelRes: Int,
    val index: Int,
)

@Composable
private fun MainNavigationBar(
    selectedIndex: Int,
    onSelectTab: (Int) -> Unit,
) {
    val items =
        listOf(
            NavDestination(R.drawable.ic_nav_home, R.string.nav_home, 0),
            NavDestination(R.drawable.ic_nav_library, R.string.nav_library, 1),
            NavDestination(R.drawable.ic_nav_stats, R.string.nav_stats, 2),
            NavDestination(R.drawable.ic_nav_person, R.string.nav_profile, 3),
        )

    NavigationBar(
        containerColor = Sage.copy(alpha = 0.92f),
        tonalElevation = 0.dp,
    ) {
        items.forEach { dest ->
            val selected = selectedIndex == dest.index
            NavigationBarItem(
                selected = selected,
                onClick = { onSelectTab(dest.index) },
                icon = {
                    Icon(
                        painter = painterResource(dest.iconRes),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(stringResource(dest.labelRes))
                },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = Accent,
                        selectedTextColor = Accent,
                        unselectedIconColor = Accent.copy(alpha = 0.45f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = TealSoft.copy(alpha = 0.45f),
                    ),
            )
        }
    }
}
