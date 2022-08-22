package com.example.facedetector.network;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.example.facedetector.model.FaceDetectionDataModel;
import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUpload {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static FaceDetectionDataModel uploadImage(Context context, File fileToUpload) {
        String TAG = "ImageUpload";
        try {
            if (fileToUpload.exists()) {

                String endpoint = "/faces/detections";

                String crlf = "\r\n";
                String twoHyphens = "--";
                String boundary = "Image Upload";

                URL urlObject = new URL("https://api.imagga.com/v2" + endpoint);

                HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
                String basicAuth = "Basic YWNjXzQ3YWU1Y2JkZjNlYTMzMjo0MDRjOGUxMWFkOGE3MWEzNDBkZjdlMmY4N2JiOGIwNg==";
                connection.setRequestProperty("Authorization", basicAuth);// "Basic " + basicAuth);
                connection.setUseCaches(false);
                connection.setDoOutput(false);

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);

                DataOutputStream request = new DataOutputStream(connection.getOutputStream());

                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileToUpload.getName() + "\"" + crlf);
                request.writeBytes(crlf);


                InputStream inputStream = new FileInputStream(fileToUpload);
                int bytesRead;
                byte[] dataBuffer = new byte[1024];
                while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
                    request.write(dataBuffer, 0, bytesRead);
                }

                request.writeBytes(crlf);
                request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
                request.flush();
                request.close();

                InputStream responseStream = new BufferedInputStream(connection.getInputStream());

                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

                String line = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                responseStreamReader.close();
                String response = stringBuilder.toString();
                Log.d(TAG, "Response " + response);
                responseStream.close();
                connection.disconnect();
                FaceDetectionDataModel faceDetectionDataModel = new Gson().fromJson(response, FaceDetectionDataModel.class);
                Log.d(TAG, "Response " + faceDetectionDataModel.toString());
                return faceDetectionDataModel;

            } else {
                Log.d(TAG, "file not exist");
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}

