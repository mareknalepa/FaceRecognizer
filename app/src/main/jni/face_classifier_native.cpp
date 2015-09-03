#include "face_classifier_native.h"

#include <opencv2/core/core.hpp>
#include <opencv2/contrib/contrib.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <iostream>
#include <fstream>
#include <sstream>

using namespace cv;
using namespace std;

static Ptr<FaceRecognizer> classifier;

JNIEXPORT void JNICALL Java_pl_polsl_wkiro_facerecognizer_classifier_FaceClassifier_init
(JNIEnv *env, jclass obj)
{
    classifier = createFisherFaceRecognizer();
}

JNIEXPORT void JNICALL Java_pl_polsl_wkiro_facerecognizer_classifier_FaceClassifier_load
(JNIEnv *env, jclass obj, jstring path)
{
    const char* path_str = env->GetStringUTFChars(path, JNI_FALSE);
    classifier->load(string(path_str));
    env->ReleaseStringUTFChars(path, path_str);
}

JNIEXPORT void JNICALL Java_pl_polsl_wkiro_facerecognizer_classifier_FaceClassifier_train
(JNIEnv *env, jclass obj, jlongArray images, jintArray labels, jstring path)
{
    vector<Mat> images_vector;
    jsize images_size = env->GetArrayLength(images);
    jlong* images_data = env->GetLongArrayElements(images, 0);
    for (int i = 0; i < images_size; ++i) {
        Mat& image = *(Mat*) images_data[i];
        images_vector.push_back(image);
    }

    vector<int> labels_vector;
    jsize labels_size = env->GetArrayLength(labels);
    jint* labels_data = env->GetIntArrayElements(labels, 0);
    for (int i = 0; i < labels_size; ++i) {
        labels_vector.push_back(labels_data[i]);
    }

    const char* path_str = env->GetStringUTFChars(path, JNI_FALSE);

    classifier->train(images_vector, labels_vector);
    classifier->save(string(path_str));

    env->ReleaseLongArrayElements(images, images_data, 0);
    env->ReleaseIntArrayElements(labels, labels_data, 0);
    env->ReleaseStringUTFChars(path, path_str);
}

JNIEXPORT jint JNICALL Java_pl_polsl_wkiro_facerecognizer_classifier_FaceClassifier_predict
(JNIEnv *env, jclass obj, jlong image)
{
    Mat& image_mat = *(Mat*) image;
    return (jint) classifier->predict(image_mat);
}
