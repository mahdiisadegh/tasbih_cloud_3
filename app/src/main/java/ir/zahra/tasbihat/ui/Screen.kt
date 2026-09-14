package ir.zahra.tasbihat.ui

/** The three kinds of screen the app can show. */
sealed class Screen {
    object Splash : Screen()

    /** stageIndex/imageIndex are 0-based; [ir.zahra.tasbihat.data.AppContent.stages] provides the data. */
    data class Stage(val stageIndex: Int, val imageIndex: Int) : Screen()

    object Final : Screen()
}
