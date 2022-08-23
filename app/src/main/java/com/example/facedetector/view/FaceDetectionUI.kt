package com.example.facedetector.view

import android.content.Context
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
    context: Context,
    faceDetectionDataModel: FaceDetectionDataModel,
    file: File
) {
    val TAG = "DrawRectangleOnFace"


    if (file.exists()) {
        Log.d(
            TAG,
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
        Log.d(TAG, "DrawRectangleOnFace: file not found")
    }

    val faces: List<Face?>? = faceDetectionDataModel.result?.faces
    var density = Resources.getSystem().getDisplayMetrics().scaledDensity
    if (faces != null) {
        if (faces.isNotEmpty()) {
            for (Face in faces) {
                Canvas(modifier = Modifier) {
                    if (Face != null) {

                        // Get the screen's density scale
                        // val scale: Float = Resources.getSystem().displayMetrics.scaledDensity
                        // Convert the dps to pixels, based on density scale
                        //val GESTURE_THRESHOLD_DP = ViewConfiguration.get(context).scaledTouchSlop
                        //density = (GESTURE_THRESHOLD_DP * scale + 0.5f)
                        /*val width =(Face.coordinates?.width ?.times(density)?.plus(0.5f))?: 0f
                        val height =(Face.coordinates?.height ?.times(density)?.plus(0.5f))?: 0f
                        val xmin = (Face.coordinates?.xmin ?.times(density)?.plus(0.5f))?: 0f
                        val ymin = (Face.coordinates?.ymin ?.times(density)?.plus(0.5f))?: 0f
                        val size = Size(width = width, height = height)*/
                        val width = Face.coordinates?.width?.div(density) ?: 0f
                        val height = Face.coordinates?.height?.div(density) ?: 0f
                        val xmin = Face.coordinates?.xmin?.div(density) ?: 0f
                        val ymin = Face.coordinates?.ymin?.div(density) ?: 0f
                        val size = Size(width = width, height = height)
                        Log.d(
                            "DrawRectangleOnFace",
                            "coordinates:  $width ,$height, $xmin, $ymin, $size"
                        )
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



