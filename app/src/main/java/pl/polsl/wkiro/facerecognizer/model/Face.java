package pl.polsl.wkiro.facerecognizer.model;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

public class Face extends Rect {

    public Face() {
        super();
    }

    public Face(Rect rectObj) {
        super();
        this.x = rectObj.x;
        this.y = rectObj.y;
        this.width = rectObj.width;
        this.height = rectObj.height;
    }

    public void drawOutline(Mat frame, Scalar color, int thickness) {
        Core.rectangle(frame, tl(), br(), color, thickness);
    }
}
