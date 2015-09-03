package pl.polsl.wkiro.facerecognizer;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.List;

import pl.polsl.wkiro.facerecognizer.camera.CameraPreviewActivity;
import pl.polsl.wkiro.facerecognizer.classifier.FaceClassifier;
import pl.polsl.wkiro.facerecognizer.model.Face;
import pl.polsl.wkiro.facerecognizer.model.FaceDetector;

public class RecognizerActivity extends CameraPreviewActivity {

    private FaceDetector faceDetector;
    private FaceClassifier faceClassifier;

    public RecognizerActivity() {
        super(R.layout.activity_recognizer, R.id.cameraPreview);
    }

    @Override
    protected void loaderCallbackExtra() {
        faceDetector = new FaceDetector(RecognizerActivity.this);
        faceClassifier = new FaceClassifier(this);
        faceClassifier.loadClassifier();
    }

    @Override
    protected void onCameraFrameExtra() {
        List<Face> faces = faceDetector.detectFaces(frameGray);
        for (Face face : faces) {
            face.drawOutline(frameProcessed, new Scalar(0, 255, 0, 255), 3);
            Mat faceImage = face.extractRoi(frameGray);
            String label = faceClassifier.recognizeFace(faceImage);
            face.drawLabel(frameProcessed, label, new Scalar(0, 255, 0, 255));
        }
    }
}
