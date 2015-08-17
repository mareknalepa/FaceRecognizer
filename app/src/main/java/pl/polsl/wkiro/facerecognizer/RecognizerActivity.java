package pl.polsl.wkiro.facerecognizer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.List;

import pl.polsl.wkiro.facerecognizer.model.Face;
import pl.polsl.wkiro.facerecognizer.model.FaceDetector;


public class RecognizerActivity extends ActionBarActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private CameraBridgeViewBase cvCameraView;
    private FaceDetector faceDetector;

    private Mat frameRgba;
    private Mat frameGray;

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    faceDetector = new FaceDetector(RecognizerActivity.this);
                    cvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_recognizer);

        cvCameraView = (CameraBridgeViewBase) findViewById(R.id.cameraPreview);
        cvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cvCameraView != null) {
            cvCameraView.disableView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, loaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (cvCameraView != null) {
            cvCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        frameRgba = new Mat();
        frameGray = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        frameRgba.release();
        frameGray.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        frameRgba = inputFrame.rgba();
        frameGray = inputFrame.gray();

        List<Face> faces = faceDetector.detectFaces(frameGray);
        for (Face face : faces) {
            face.drawOutline(frameRgba, new Scalar(0, 255, 0, 255), 3);
        }

        return frameRgba;
    }
}
