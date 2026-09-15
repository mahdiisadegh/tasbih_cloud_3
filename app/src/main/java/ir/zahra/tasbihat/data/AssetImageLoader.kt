import kotlin.collections.MutableMap
package ir.zahra.tasbihat.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Decodes artwork straight from assets/images/... and keeps a small LRU
 * cache so tapping back and forth between recently seen images (e.g. after
 * "شروع مجدد") does not re-decode from disk every time.
 *
 * Decoding runs off the main thread and images are down-sampled to a
 * sensible max dimension, since the source artwork can be full-resolution
 * (1920x1080 or larger) and we only ever need screen-sized bitmaps.
 */
object AssetImageLoader {

    private const val MAX_DIMENSION = 1600
    private const val CACHE_CAPACITY = 8

    private val cache = object : LinkedHashMap<String, ImageBitmap>(CACHE_CAPACITY, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.Entry<String, ImageBitmap>?): Boolean {
            return size > CACHE_CAPACITY
        }
    }

    suspend fun load(context: Context, assetPath: String): ImageBitmap {
        cache[assetPath]?.let { return it }
        return withContext(Dispatchers.IO) {
            val bitmap = decodeSampled(context, assetPath)
            val imageBitmap = bitmap.asImageBitmap()
            synchronized(cache) { cache[assetPath] = imageBitmap }
            imageBitmap
        }
    }

    /** Fire-and-forget warm-up so the *next* image is likely already decoded by the time it's needed. */
    suspend fun prefetch(context: Context, assetPath: String) {
        if (cache.containsKey(assetPath)) return
        load(context, assetPath)
    }

    private fun decodeSampled(context: Context, assetPath: String): Bitmap {
        val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.assets.open(assetPath).use { BitmapFactory.decodeStream(it, null, boundsOptions) }

        var sampleSize = 1
        val largestSide = maxOf(boundsOptions.outWidth, boundsOptions.outHeight)
        while (largestSide / sampleSize > MAX_DIMENSION) {
            sampleSize *= 2
        }

        val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        return context.assets.open(assetPath).use {
            BitmapFactory.decodeStream(it, null, decodeOptions)
                ?: throw IllegalStateException("Could not decode asset image: $assetPath")
        }
    }
}
