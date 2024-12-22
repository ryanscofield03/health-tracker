package nz.ac.canterbury.seng303.healthtracking

val dataAccessModule = module {
    single { AppDatabase.getDatabase(androidContext()) }
}