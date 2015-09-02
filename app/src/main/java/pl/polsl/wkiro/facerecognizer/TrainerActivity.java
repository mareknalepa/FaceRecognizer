package pl.polsl.wkiro.facerecognizer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.opencv.core.Scalar;

import java.util.List;

import pl.polsl.wkiro.facerecognizer.camera.CameraPreviewActivity;
import pl.polsl.wkiro.facerecognizer.classifier.ClassifierDatabase;
import pl.polsl.wkiro.facerecognizer.model.Face;
import pl.polsl.wkiro.facerecognizer.model.FaceDetector;
import pl.polsl.wkiro.facerecognizer.model.PictureHolder;

public class TrainerActivity extends CameraPreviewActivity {

    private FaceDetector faceDetector;
    private ClassifierDatabase classifierDatabase;

    private MenuItem menuShowClassifierStatus;
    private MenuItem menuResetClassifier;
    private MenuItem menuTrainClassifier;

    private List<Face> faces;

    public TrainerActivity() {
        super(R.layout.activity_trainer, R.id.cameraPreview);
        classifierDatabase = new ClassifierDatabase(this);
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
        faces = faceDetector.detectFaces(frameGray);
        for (Face face : faces) {
            face.drawOutline(frameProcessed, new Scalar(0, 255, 0, 255), 3);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuShowClassifierStatus = menu.add("Show classifier status");
        menuResetClassifier = menu.add("Reset classifier");
        menuTrainClassifier = menu.add("Train classifier");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == menuShowClassifierStatus) {
            showClassifierStatus();
        } else if (item == menuResetClassifier) {
            resetClassifier();
        } else if (item == menuTrainClassifier) {
            trainClassifier();
        }
        return true;
    }

    public void processPictureClick(View view) {
        if (frameRgba != null && frameGray != null && !faces.isEmpty()) {
            Intent intent = new Intent(this, TrainerDatabaseActivity.class);

            PictureHolder ph = PictureHolder.getInstance();
            ph.setFrameRgba(frameRgba);
            ph.setFrameGray(frameGray);

            startActivity(intent);
        }
    }

    private void showClassifierStatus() {
        int dbElements = classifierDatabase.filesNumber();
        String text;
        if (dbElements == 0) {
            text = "Classifier database is empty.";
        } else if (dbElements == 1) {
            text = "Classifier already trained.";
        } else {
            text = "Classifier ready to train with " + (dbElements - 1) + " examples.";
        }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void resetClassifier() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    classifierDatabase.clear();
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to permanently delete all examples stored in database?");
        builder.setPositiveButton("Yes", dialogClickListener);
        builder.setNegativeButton("No", dialogClickListener);
        builder.show();
    }

    private void trainClassifier() {

    }
}
