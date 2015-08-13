package pl.polsl.wkiro.facerecognizer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class RecognizerActivity extends ActionBarActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private CameraBridgeViewBase cvCameraView;
    private File cascadeFile;
    private CascadeClassifier detector;

    private Mat frameRgba;
    private Mat frameGray;

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    try {
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        cascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream fos = new FileOutputStream(cascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        fos.close();

                        detector = new CascadeClassifier(cascadeFile.getAbsolutePath());
                        if (detector.empty()) {
                            detector = null;
                        }
                        cascadeDir.delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

        int minFaceSize = Math.round(frameGray.rows() * 0.2f);

        MatOfRect faces = new MatOfRect();

        if (detector != null) {
            detector.detectMultiScale(frameGray, faces, 1.1, 2, 2, new Size(minFaceSize, minFaceSize), new Size());
        }

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; ++i) {
            Core.rectangle(frameRgba, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
        }

        return frameRgba;
    }
}