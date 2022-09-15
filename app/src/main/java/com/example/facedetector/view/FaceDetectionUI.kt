package com.example.facedetector.view

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.facedetector.R
import com.example.facedetector.model.Face
import com.example.facedetector.model.FaceDetectionDataModel
import com.example.facedetector.viewmodel.FaceDetectionViewModel
import java.io.File

@Composable
fun DrawRectangleOnFace(
    faceDetectionDataModel: FaceDetectionDataModel?,
    file: File,
    faceDetectionViewModel: FaceDetectionViewModel
) {
    if (file.exists()) {
        Log.d(
            "FaceDetectionUI",
            "matrices: ${Resources.getSystem().getDisplayMetrics().density}  ${
                Resources.getSystem().displayMetrics.xdpi
            }  ${Resources.getSystem().displayMetrics.ydpi} "
        )
        val imgBitmap = BitmapFactory.decodeFile(file.absolutePath)
        Log.d("FaceDetectionUI", "imgBitmap : ${imgBitmap.width}  ${imgBitmap.height}")

        Image(
            painter = rememberImagePainter(data = File(file.absolutePath)),
            contentDescription = "",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight()
                .fillMaxWidth()
        )


        //to unit test error response alert dialog
        /* if (faces != null) {
             if (faces.isEmpty()){
                 faceDetectionDataModel.status?.type ="error"
                 faceDetectionDataModel.status?.text ="test error 1"}
         }*/
        if (faceDetectionDataModel != null) {
            if (faceDetectionDataModel?.status?.type.equals("success")) {
                val faces: List<Face?>? = faceDetectionDataModel?.result?.faces
                if (faces != null) {
                    if (faces.isNotEmpty()) {
                        for (Face in faces) {
                            Canvas(modifier = Modifier) {
                                if (Face != null) {

                                    val width =
                                        calculateWidth(Face.coordinates?.width, imgBitmap.width)
                                            ?: 0f
                                    val height =
                                        calculateHeight(Face.coordinates?.height, imgBitmap.height)
                                            ?: 0f
                                    val xmin =
                                        calculateWidth(Face.coordinates?.xmin, imgBitmap.width)
                                            ?: 0f
                                    val ymin =
                                        calculateHeight(Face.coordinates?.ymin, imgBitmap.height)
                                            ?: 0f

                                    val size = Size(width = width, height = height)
                                    Log.d(
                                        "FaceDetectionUI",
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

                    } else {
                        alertDialogBackToCamera(
                            title = stringResource(R.string.face_detection),
                            text = stringResource(R.string.no_faces_found),
                            faceDetectionViewModel
                        )
                    }
                }
            } else if (faceDetectionDataModel?.status?.type.equals("error")) {
                val type: String = faceDetectionDataModel?.status?.type!!
                val text: String = faceDetectionDataModel?.status?.text!!
                alertDialogBackToCamera(
                    title = "$type",
                    text = "$text",
                    faceDetectionViewModel
                )
            } else {
                customCircularProgressBar()
            }

        } else {
            alertDialogBackToCamera(
                title = stringResource(R.string.face_detection),
                text = stringResource(R.string.no_faces_found_null),
                faceDetectionViewModel
            )
        }
    } else {
        Log.d("FaceDetectionUI", "DrawRectangleOnFace: file not found")
    }

}


fun calculateWidth(value: Int?, imageWidth: Int): Float? {
    val widthPercentage = Resources.getSystem().displayMetrics.widthPixels / imageWidth.toFloat()
    return value?.times(widthPercentage) as Float
}

fun calculateHeight(value: Int?, imageHeight: Int): Float? {
    val heightPercentage = Resources.getSystem().displayMetrics.heightPixels / imageHeight.toFloat()
    return value?.times(heightPercentage) as Float
}


@Composable
fun customCircularProgressBar() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(100.dp),
            color = Color.Green,
            strokeWidth = 8.dp
        )
    }
}


@Composable
fun alertDialogBackToCamera(
    title: String,
    text: String,
    faceDetectionViewModel: FaceDetectionViewModel
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            TextButton(onClick = { faceDetectionViewModel.eventTriggered(FaceDetectionViewModel.OPEN_CAMERA) })
            { Text(text = stringResource(R.string.back_to_Camera)) }
        },

        title = { Text(title) },
        text = { Text(text) }
    )
}





