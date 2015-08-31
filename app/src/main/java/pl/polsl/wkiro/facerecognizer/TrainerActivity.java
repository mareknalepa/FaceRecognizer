package pl.polsl.wkiro.facerecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.opencv.core.Scalar;

import java.util.List;

import pl.polsl.wkiro.facerecognizer.camera.CameraPreviewActivity;
import pl.polsl.wkiro.facerecognizer.model.Face;
import pl.polsl.wkiro.facerecognizer.model.FaceDetector;
import pl.polsl.wkiro.facerecognizer.model.PictureHolder;

public class TrainerActivity extends CameraPreviewActivity {

    private FaceDetector faceDetector;

    private MenuItem menuShowClassifierStatus;
    private MenuItem menuTrainClassifier;

    public TrainerActivity() {
        super(R.layout.activity_trainer, R.id.cameraPreview);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuShowClassifierStatus = menu.add("Show classifier status");
        menuTrainClassifier = menu.add("Train classifier");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == menuShowClassifierStatus) {

        } else if (item == menuTrainClassifier) {

        }
        return true;
    }

    public void processPictureClick(View view) {
        if (frameRgba != null && frameGray != null) {
            Intent intent = new Intent(this, TrainerDatabaseActivity.class);

            PictureHolder ph = PictureHolder.getInstance();
            ph.setFrameRgba(frameRgba);
            ph.setFrameGray(frameGray);

            startActivity(intent);
        }
    }
}
