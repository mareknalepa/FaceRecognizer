package pl.polsl.wkiro.facerecognizer.model;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Face extends Rect {

    private Scalar assignedColor;

    public Face() {
        super();
        assignedColor = ColorsFactory.createColor();
    }

    public Face(Rect rectObj) {
        super();
        assignedColor = ColorsFactory.createColor();
        this.x = rectObj.x;
        this.y = rectObj.y;
        this.width = rectObj.width;
        this.height = rectObj.height;
    }

    public void drawOutline(Mat frame, Scalar color, int thickness) {
        Core.rectangle(frame, tl(), br(), color, thickness);
    }

    public void drawColorOutline(Mat frame, int thickness) {
        drawOutline(frame, assignedColor, thickness);
    }

    public Scalar getAssignedColor() {
        return assignedColor;
    }

    public Mat extractRoi(Mat frame) {
        Mat extracted = new Mat();
        Imgproc.resize(frame.submat(y, y + height, x, x + width), extracted, new Size(256, 256));
        return extracted;
    }
}
