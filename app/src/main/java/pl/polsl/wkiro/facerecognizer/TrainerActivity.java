package pl.polsl.wkiro.facerecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.opencv.core.Scalar;

import java.util.List;

import pl.polsl.wkiro.facerecognizer.camera.CameraPreviewActivity;
import pl.polsl.wkiro.facerecognizer.model.Face;
import pl.polsl.wkiro.facerecognizer.model.FaceDetector;
import pl.polsl.wkiro.facerecognizer.model.PictureHolder;

public class TrainerActivity extends CameraPreviewActivity {

    private FaceDetector faceDetector;
    private Button trainButton;

    public TrainerActivity() {
        super(R.layout.activity_trainer, R.id.cameraPreview);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trainButton = (Button) findViewById(R.id.trainButton);
    }

    @Override
    protected void loaderCallbackExtra() {
        faceDetector = new FaceDetector(TrainerActivity.this);
    }

    @Override
    protected void onCameraFrameExtra() {
        List<Face> faces = faceDetector.detectFaces(frameGray);
        for (Face face : faces) {
            face.drawOutline(frameProcessed, new Scalar(0, 255, 0, 255), 3);
        }
    }

    public void trainClick(View view) {
        if (frameRgba != null && frameGray != null) {
            Intent intent = new Intent(this, TrainerPictureActivity.class);

            PictureHolder ph = PictureHolder.getInstance();
            ph.setFrameRgba(frameRgba);
            ph.setFrameGray(frameGray);

            startActivity(intent);
        }
    }
}
