package pl.polsl.wkiro.facerecognizer.camera;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public abstract class CameraPreviewActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    final protected int layoutResId;
    final protected int cameraViewResId;

    protected CameraBridgeViewBase ocvCameraView;

    protected Mat frameRgba;
    protected Mat frameGray;
    protected Mat frameProcessed;

    protected abstract void loaderCallbackExtra();

    protected abstract void onCameraFrameExtra();

    public CameraPreviewActivity(int layoutResId, int cameraViewResId) {
        this.layoutResId = layoutResId;
        this.cameraViewResId = cameraViewResId;
    }

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    loaderCallbackExtra();
                    ocvCameraView.enableView();
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
        setContentView(layoutResId);

        ocvCameraView = (CameraBridgeViewBase) findViewById(cameraViewResId);
        ocvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ocvCameraView != null) {
            ocvCameraView.disableView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, loaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (ocvCameraView != null) {
            ocvCameraView.disableView();
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
        frameProcessed = frameRgba.clone();

        onCameraFrameExtra();

        return frameProcessed;
    }
}
