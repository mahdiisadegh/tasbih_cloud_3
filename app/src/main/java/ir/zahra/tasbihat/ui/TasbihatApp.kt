package ir.zahra.tasbihat.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.zahra.tasbihat.R
import ir.zahra.tasbihat.data.AppContent
import ir.zahra.tasbihat.data.AssetImageLoader
import ir.zahra.tasbihat.ui.components.AssetImage
import ir.zahra.tasbihat.ui.components.BottomInfoBar
import ir.zahra.tasbihat.ui.components.SettingsSheet
import kotlinx.coroutines.delay

private const val SPLASH_DURATION_MS = 2000L

@Composable
fun TasbihatApp(
    state: TasbihatState,
    onSendFeedback: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedContent(
            targetState = state.screen,
            transitionSpec = {
                if (initialState is Screen.Splash) {
                    fadeIn(tween(700)) togetherWith fadeOut(tween(700))
                } else {
                    (slideInHorizontally(tween(380)) { fullWidth -> fullWidth } + fadeIn(tween(220))) togetherWith
                        (slideOutHorizontally(tween(380)) { fullWidth -> -fullWidth } + fadeOut(tween(180)))
                }
            },
            label = "screen"
        ) { targetScreen ->
            when (targetScreen) {
                is Screen.Splash -> SplashScreen(onFinished = state::onSplashFinished)
                is Screen.Stage -> StageScreen(
                    stageIndex = targetScreen.stageIndex,
                    imageIndex = targetScreen.imageIndex,
                    onTap = state::onImageTapped,
                    onSettingsClick = state::openSettings
                )
                is Screen.Final -> FinalScreen(
                    onFeedback = onSendFeedback,
                    onRestart = state::onRestart
                )
            }
        }

        if (state.settingsSheetVisible) {
            SettingsSheet(
                musicEnabled = state.musicEnabled,
                dhikrEnabled = state.dhikrEnabled,
                onMusicToggle = state::onToggleMusic,
                onDhikrToggle = state::onToggleDhikr,
                onRestart = state::onRestart,
                onDismiss = state::closeSettings
            )
        }
    }
}

@Composable
private fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(SPLASH_DURATION_MS)
        onFinished()
    }
    AssetImage(
        assetPath = AppContent.SPLASH_IMAGE_ASSET,
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun StageScreen(
    stageIndex: Int,
    imageIndex: Int,
    onTap: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val stage = AppContent.stages[stageIndex]
    val imageNumber = imageIndex + 1
    val context = LocalContext.current

    // Warm the *next* image in the cache so the slide-in has one ready.
    LaunchedEffect(stageIndex, imageIndex) {
        val nextPath = when {
            imageIndex + 1 < stage.imageCount -> stage.imageAssetPath(imageNumber + 1)
            stageIndex + 1 < AppContent.stages.size -> AppContent.stages[stageIndex + 1].imageAssetPath(1)
            else -> AppContent.FINAL_IMAGE_ASSET
        }
        AssetImageLoader.prefetch(context, nextPath)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onTap
            )
    ) {
        AssetImage(
            assetPath = stage.imageAssetPath(imageNumber),
            contentDescription = stage.dhikrText,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        BottomInfoBar(
            dhikrText = stage.dhikrText,
            currentImageNumber = imageNumber,
            totalImages = stage.imageCount,
            dotsCount = stage.imageCount,
            activeDotIndex = imageIndex,
            onSettingsClick = onSettingsClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun FinalScreen(
    onFeedback: () -> Unit,
    onRestart: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AssetImage(
            assetPath = AppContent.FINAL_IMAGE_ASSET,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onRestart, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.final_restart))
            }
            OutlinedButton(onClick = onFeedback, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.final_feedback))
            }
        }
    }
}
