package ir.zahra.tasbihat.data

/**
 * A single "مرحله" (stage) of the tasbih: a folder of sequential images,
 * a dhikr phrase, and the raw-audio asset that plays with each image.
 *
 * To replace artwork in the future: just drop new files with the SAME
 * names into assets/images/<folderName>/ — nothing here needs to change
 * unless the number of images in a stage changes (update [imageCount]).
 */
data class Stage(
    val folderName: String,
    val fileNamePrefix: String,
    val imageCount: Int,
    val dhikrText: String,
    val audioAssetPath: String
) {
    /** assets/images/<folderName>/<fileNamePrefix>_01.jpg style path, 1-based index. */
    fun imageAssetPath(index1Based: Int): String {
        val padded = index1Based.toString().padStart(2, '0')
        return "images/$folderName/${fileNamePrefix}_$padded.jpg"
    }
}

object AppContent {
    const val SPLASH_IMAGE_ASSET = "images/splash.jpg"
    const val FINAL_IMAGE_ASSET = "images/final_image.jpg"

    const val BACKGROUND_MUSIC_ASSET = "audio/audio4.wav"

    val stages = listOf(
        Stage(
            folderName = "section1",
            fileNamePrefix = "section1",
            imageCount = 34,
            dhikrText = "الله اکبر",
            audioAssetPath = "audio/audio1.wav"
        ),
        Stage(
            folderName = "section2",
            fileNamePrefix = "section2",
            imageCount = 33,
            dhikrText = "الحمدلله",
            audioAssetPath = "audio/audio2.wav"
        ),
        Stage(
            folderName = "section3",
            fileNamePrefix = "section3",
            imageCount = 33,
            dhikrText = "سبحان الله",
            audioAssetPath = "audio/audio3.wav"
        )
    )
}
