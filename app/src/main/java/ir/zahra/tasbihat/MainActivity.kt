package ir.zahra.tasbihat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import ir.zahra.tasbihat.audio.AudioController
import ir.zahra.tasbihat.data.SettingsStore
import ir.zahra.tasbihat.ui.TasbihatApp
import ir.zahra.tasbihat.ui.TasbihatState
import ir.zahra.tasbihat.ui.theme.TasbihatTheme

class MainActivity : ComponentActivity() {

    private lateinit var audioController: AudioController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settingsStore = SettingsStore(this)
        audioController = AudioController(this)

        setContent {
            val state = remember { TasbihatState(settingsStore, audioController) }

            TasbihatTheme {
                TasbihatApp(
                    state = state,
                    onSendFeedback = { sendFeedbackEmail() }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        audioController.startMusic()
    }

    override fun onResume() {
        super.onResume()
        audioController.resumeAll()
    }

    override fun onPause() {
        super.onPause()
        audioController.pauseAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioController.release()
    }

    /**
     * Opens an email composer pre-filled with a subject/body, as requested
     * by the "ارسال عکس یا نظر" option. No server or account of ours is
     * involved — it's purely a system email Intent.
     */
    private fun sendFeedbackEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.feedback_email_address)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_email_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.feedback_email_body))
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}
