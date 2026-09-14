package ir.zahra.tasbihat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import ir.zahra.tasbihat.data.AssetImageLoader

/**
 * Displays an image stored under app/src/main/assets/images/... Decoding
 * happens off the main thread; until it's ready we simply show the
 * background color so there's no flash of white.
 */
@Composable
fun AssetImage(
    assetPath: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val bitmapState = produceState<ImageBitmap?>(initialValue = null, key1 = assetPath) {
        value = AssetImageLoader.load(context, assetPath)
    }

    val bitmap = bitmapState.value
    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        androidx.compose.foundation.layout.Box(
            modifier = modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
