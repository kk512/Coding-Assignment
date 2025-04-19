package app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.api.LibraryApi
import app.api.RetrofitInstance
import com.example.libraryapp.ui.theme.LibraryAppTheme
import kotlinx.coroutines.launch
import retrofit2.http.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LibraryAppScreen()
                }
            }
        }
    }
}

@Composable
fun LibraryAppScreen() {
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var branches by remember { mutableStateOf<List<LibraryBranch>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val api: LibraryApi = RetrofitInstance.getInstance(LibraryApi::class.java)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("📚 Library Dashboard", style = MaterialTheme.typography.headlineMedium)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                coroutineScope.launch {
                    isLoading = true
                    errorMessage = null
                    try {
                        val booksResp = api.getBooks()
                        val usersResp = api.getUsers()
                        val branchesResp = api.getBranches()
                        if (booksResp.isSuccessful && usersResp.isSuccessful && branchesResp.isSuccessful) {
                            books = booksResp.body().orEmpty()
                            users = usersResp.body().orEmpty()
                            branches = branchesResp.body().orEmpty()
                        } else {
                            errorMessage = "Error loading data."
                        }
                    } catch (e: Exception) {
                        errorMessage = "${e.localizedMessage}"
                    } finally {
                        isLoading = false
                    }
                }
            }) {
                Text("Load All Data")
            }
        }

        if (isLoading) CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        errorMessage?.let {
            Text(
                it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Books", style = MaterialTheme.typography.titleLarge)
        LazyColumn {
            items(books) { book ->
                Card(modifier = Modifier.padding(4.dp).fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Title: ${book.title}")
                        Text("Author: ${book.author}")
                        Text("Tags: ${book.tags.joinToString()}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Users", style = MaterialTheme.typography.titleLarge)
        LazyColumn {
            items(users) { user ->
                Card(modifier = Modifier.padding(4.dp).fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Name: ${user.name}")
                        Text("Email: ${user.email}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Branches", style = MaterialTheme.typography.titleLarge)
        LazyColumn {
            items(branches) { branch ->
                Card(modifier = Modifier.padding(4.dp).fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Branch: ${branch.name}")
                        Text("Location: ${branch.location}")
                    }
                }
            }
        }
    }
}
