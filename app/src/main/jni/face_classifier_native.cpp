#include "face_classifier_native.h"
#include <opencv2/core/core.hpp>

void Java_pl_polsl_wkiro_facerecognizer_classifier_FaceClassifier_load
(JNIEnv *jenv, jclass obj, jstring path)
{
}

void Java_pl_polsl_wkiro_facerecognizer_classifier_FaceClassifier_save
(JNIEnv *jenv, jclass obj, jstring path)
{
}

void Java_pl_polsl_wkiro_facerecognizer_classifier_FaceClassifier_train
(JNIEnv *jenv, jclass obj, jlong face_frame_addr, jint label)
{
}

jint Java_pl_polsl_wkiro_facerecognizer_classifier_FaceClassifier_predict
(JNIEnv *jenv, jclass obj, jlong face_frame_addr)
{
    return 0;
}
