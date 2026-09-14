package ir.zahra.tasbihat.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import ir.zahra.tasbihat.data.AppContent

/**
 * Two fully independent audio channels, as required by the spec:
 *
 *  - Background music (audio4): starts once, loops continuously for as
 *    long as the app is open, and is never interrupted by image changes.
 *  - Dhikr voice (audio1/2/3): a short one-shot sound that restarts from
 *    the beginning every time a new image is shown.
 *
 * Turning one channel off never affects the other.
 */
class AudioController(context: Context) {

    private val appContext = context.applicationContext

    // --- Background music (long-running loop) ---
    private var musicPlayer: MediaPlayer? = null
    private var musicEnabled = true

    // --- Dhikr voice (short, replayed on every image) ---
    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
        )
        .build()

    private val dhikrSoundIds = mutableMapOf<String, Int>()
    private var dhikrEnabled = true
    private var lastDhikrStreamId: Int? = null

    init {
        AppContent.stages.map { it.audioAssetPath }.distinct().forEach { path ->
            val afd = appContext.assets.openFd(path)
            val id = soundPool.load(afd, 1)
            afd.close()
            dhikrSoundIds[path] = id
        }
    }

    fun setMusicEnabled(enabled: Boolean) {
        musicEnabled = enabled
        if (enabled) {
            if (musicPlayer == null) startMusic() else musicPlayer?.let { if (!it.isPlaying) it.start() }
        } else {
            musicPlayer?.pause()
        }
    }

    fun setDhikrEnabled(enabled: Boolean) {
        dhikrEnabled = enabled
        if (!enabled) {
            lastDhikrStreamId?.let { soundPool.stop(it) }
        }
    }

    /** Called once when the app becomes visible. */
    fun startMusic() {
        if (musicPlayer != null) {
            if (musicEnabled) musicPlayer?.start()
            return
        }
        val afd = appContext.assets.openFd(AppContent.BACKGROUND_MUSIC_ASSET)
        musicPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            isLooping = true
            prepare()
            if (musicEnabled) start()
        }
    }

    /** Restarts audio4 from the very beginning (used by "شروع مجدد"). */
    fun restartMusicFromBeginning() {
        musicPlayer?.let {
            it.seekTo(0)
            if (musicEnabled) it.start()
        } ?: startMusic()
    }

    /** Plays the dhikr sound for the given stage's audio asset, from the start. */
    fun playDhikr(audioAssetPath: String) {
        if (!dhikrEnabled) return
        lastDhikrStreamId?.let { soundPool.stop(it) }
        val soundId = dhikrSoundIds[audioAssetPath] ?: return
        lastDhikrStreamId = soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    fun pauseAll() {
        musicPlayer?.pause()
    }

    fun resumeAll() {
        if (musicEnabled) musicPlayer?.start()
    }

    fun release() {
        musicPlayer?.release()
        musicPlayer = null
        soundPool.release()
    }
}
