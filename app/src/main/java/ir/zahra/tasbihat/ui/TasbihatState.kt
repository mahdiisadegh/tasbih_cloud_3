package ir.zahra.tasbihat.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ir.zahra.tasbihat.audio.AudioController
import ir.zahra.tasbihat.data.AppContent
import ir.zahra.tasbihat.data.SettingsStore

/**
 * Owns the single navigation/UI state machine described in the spec:
 * splash (2s) -> stage 1 images -> stage 2 images -> stage 3 images -> final.
 * Tapping always advances by exactly one image / stage; there is no "back".
 */
class TasbihatState(
    private val settingsStore: SettingsStore,
    private val audioController: AudioController
) {
    var screen by mutableStateOf<Screen>(Screen.Splash)
        private set

    var musicEnabled by mutableStateOf(settingsStore.musicEnabled)
        private set

    var dhikrEnabled by mutableStateOf(settingsStore.dhikrEnabled)
        private set

    var settingsSheetVisible by mutableStateOf(false)
        private set

    init {
        audioController.setMusicEnabled(musicEnabled)
        audioController.setDhikrEnabled(dhikrEnabled)
    }

    fun onSplashFinished() {
        screen = Screen.Stage(stageIndex = 0, imageIndex = 0)
        playCurrentDhikr()
    }

    fun onImageTapped() {
        val current = screen as? Screen.Stage ?: return
        val stage = AppContent.stages[current.stageIndex]

        if (current.imageIndex + 1 < stage.imageCount) {
            screen = current.copy(imageIndex = current.imageIndex + 1)
            playCurrentDhikr()
        } else if (current.stageIndex + 1 < AppContent.stages.size) {
            screen = Screen.Stage(stageIndex = current.stageIndex + 1, imageIndex = 0)
            playCurrentDhikr()
        } else {
            screen = Screen.Final
        }
    }

    fun onRestart() {
        screen = Screen.Stage(stageIndex = 0, imageIndex = 0)
        audioController.restartMusicFromBeginning()
        playCurrentDhikr()
        settingsSheetVisible = false
    }

    fun onToggleMusic(enabled: Boolean) {
        musicEnabled = enabled
        settingsStore.musicEnabled = enabled
        audioController.setMusicEnabled(enabled)
    }

    fun onToggleDhikr(enabled: Boolean) {
        dhikrEnabled = enabled
        settingsStore.dhikrEnabled = enabled
        audioController.setDhikrEnabled(enabled)
    }

    fun openSettings() {
        settingsSheetVisible = true
    }

    fun closeSettings() {
        settingsSheetVisible = false
    }

    private fun playCurrentDhikr() {
        val current = screen as? Screen.Stage ?: return
        val stage = AppContent.stages[current.stageIndex]
        audioController.playDhikr(stage.audioAssetPath)
    }
}
