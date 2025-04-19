package app.api

interface LibraryApi {
    @GET("/books")
    suspend fun getBooks(): List<Book>

    @POST("/books")
    suspend fun addBook(@Body book: Book)

    @GET("/users")
    suspend fun getUsers(): List<User>

    @POST("/users")
    suspend fun addUser(@Body user: User)

    @GET("/branches")
    suspend fun getBranches(): List<LibraryBranch>

    @POST("/branches")
    suspend fun addBranch(@Body branch: LibraryBranch)
}


