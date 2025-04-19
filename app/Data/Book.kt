package app.Data

data class Book(
    val id: Long = 0,
    val title: String,
    val author: String,
    val tags: List<String>
)