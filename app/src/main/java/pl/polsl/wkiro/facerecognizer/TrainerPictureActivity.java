package pl.polsl.wkiro.facerecognizer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

import pl.polsl.wkiro.facerecognizer.model.Face;
import pl.polsl.wkiro.facerecognizer.model.FaceDetector;
import pl.polsl.wkiro.facerecognizer.model.PictureHolder;

public class TrainerPictureActivity extends Activity {

    private Mat frameRgba;
    private Mat frameGray;
    private Bitmap frameBitmap;
    private ImageView imageView;
    private FaceDetector faceDetector;
    private List<Face> faces;
    private List<TextView> labels;
    private List<EditText> editTexts;
    private int sequentialNumber = 1;

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    faceDetector = new FaceDetector(TrainerPictureActivity.this);
                    PictureHolder ph = PictureHolder.getInstance();

                    frameRgba = ph.getFrameRgba();
                    frameGray = ph.getFrameGray();

                    faces = faceDetector.detectFaces(frameGray);
                    for (Face face : faces) {
                        face.drawColorOutline(frameRgba, 3);
                    }

                    frameBitmap = Bitmap.createBitmap(frameRgba.cols(), frameRgba.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(frameRgba, frameBitmap);
                    imageView.setImageBitmap(frameBitmap);

                    createFacesForm();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public TrainerPictureActivity() {
        labels = new ArrayList<>();
        editTexts = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_picture);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, loaderCallback);
    }

    public void trainRecognizerClick(View view) {

    }

    private void createFacesForm() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        sequentialNumber = 1;
        for (Face face : faces) {
            TextView label = new TextView(TrainerPictureActivity.this);
            label.setWidth(160);
            label.setText("Face " + sequentialNumber);
            label.setId(20 + sequentialNumber);
            label.setTextAppearance(TrainerPictureActivity.this, android.R.style.TextAppearance_Medium);
            labels.add(label);
            rl.addView(label);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) label.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            if (sequentialNumber == 1) {
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                params.addRule(RelativeLayout.BELOW, 39 + sequentialNumber);
            }
            label.setLayoutParams(params);

            EditText editText = new EditText(TrainerPictureActivity.this);
            editText.setId(40 + sequentialNumber);
            editTexts.add(editText);
            rl.addView(editText);
            params = (RelativeLayout.LayoutParams) editText.getLayoutParams();
            params.addRule(RelativeLayout.RIGHT_OF, 20 + sequentialNumber);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            if (sequentialNumber == 1) {
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                params.addRule(RelativeLayout.BELOW, 39 + sequentialNumber);
            }
            editText.setLayoutParams(params);

            ++sequentialNumber;
        }
    }
}
