package com.example.facedetector.view

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.facedetector.model.Face
import com.example.facedetector.model.FaceDetectionDataModel
import java.io.File

//TODO Implement JetPack compose UI


@Composable
fun DrawRectangleOnFace(
    faceDetectionDataModel: FaceDetectionDataModel,
    file: File
) {
    if (file.exists()) {
        Log.d(
            "aaaaaaa",
            "matrics: ${Resources.getSystem().getDisplayMetrics().density}  ${
                Resources.getSystem().getDisplayMetrics().xdpi
            }  ${Resources.getSystem().getDisplayMetrics().ydpi} "
        )
        Image(
            painter = rememberImagePainter(data = File(file.absolutePath)),
            contentDescription = "",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight()
                .fillMaxWidth()
        )
    } else {
        Log.d("chetan", "DrawRectangleOnFace: file not found")
    }

    val faces: List<Face?>? = faceDetectionDataModel.result?.faces
    var density = 2f//Resources.getSystem().getDisplayMetrics().scaledDensity
    if (faces != null) {
        if (faces.isNotEmpty()) {
            for (Face in faces) {
                Canvas(modifier = Modifier) {
                    if (Face != null) {
                        val width = Face.coordinates?.width?.div(density) ?: 0f
                        val height = Face.coordinates?.height?.div(density) ?: 0f
                        val xmin = Face.coordinates?.xmin?.div(density) ?: 0f
                        val ymin = Face.coordinates?.ymin?.div(density) ?: 0f
                        val size = Size(width = width, height = height)
                        drawRect(
                            color = Color.Green,
                            size = size,
                            topLeft = Offset(xmin, ymin),
                            style = Stroke(width = 2.dp.toPx()),
                        )
                    }
                }
            }
        }
    }
}



