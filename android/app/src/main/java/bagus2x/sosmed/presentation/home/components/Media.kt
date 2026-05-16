package bagus2x.sosmed.presentation.home.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import bagus2x.sosmed.domain.model.Media
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import timber.log.Timber

@Composable
fun Media(
    media: Media,
    onImageClicked: (Media.Image) -> Unit,
    onVideoClicked: (Media.Video) -> Unit,
    modifier: Modifier = Modifier
) {
    when (media) {
        is Media.Image -> {
            var errorMsg by remember { mutableStateOf("") }
            val context = LocalContext.current
            Box(
                modifier = modifier.clickable { onImageClicked(media) }
            ) {
                AsyncImage(
                    model = media.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onState = { state ->
                        when (state) {
                            is AsyncImagePainter.State.Error -> {
                                val msg = state.result.throwable.message ?: "unknown"
                                Timber.e(state.result.throwable, "Coil failed: ${media.imageUrl}")
                                errorMsg = msg
                                Toast.makeText(context, "Img err: $msg", Toast.LENGTH_LONG).show()
                            }
                            is AsyncImagePainter.State.Success -> {
                                errorMsg = ""
                            }
                            else -> {}
                        }
                    }
                )
                if (errorMsg.isNotEmpty()) {
                    Text(
                        text = errorMsg,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
        is Media.Video -> {
            VideoThumbnail(
                thumbnailUrl = media.thumbnailUrl,
                contentDescription = null,
                modifier = modifier,
                onClick = { onVideoClicked(media) }
            )
        }
    }
}
