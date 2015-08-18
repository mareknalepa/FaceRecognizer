package pl.polsl.wkiro.facerecognizer;

import org.opencv.core.Scalar;

import java.util.List;

import pl.polsl.wkiro.facerecognizer.model.Face;
import pl.polsl.wkiro.facerecognizer.model.FaceDetector;


public class RecognizerActivity extends CameraPreviewActivity {

    private FaceDetector faceDetector;

    public RecognizerActivity() {
        super(R.layout.activity_recognizer, R.id.cameraPreview);
    }

    @Override
    protected void loaderCallbackExtra() {
        faceDetector = new FaceDetector(RecognizerActivity.this);
    }

    @Override
    protected void onCameraFrameExtra() {
        List<Face> faces = faceDetector.detectFaces(frameGray);
        for (Face face : faces) {
            face.drawOutline(frameRgba, new Scalar(0, 255, 0, 255), 3);
        }
    }
}
