LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#OPENCV_CAMERA_MODULES:=off
#OPENCV_INSTALL_MODULES:=off
#OPENCV_LIB_TYPE:=SHARED
include ~/Android/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk

LOCAL_MODULE    := face_classifier_native
LOCAL_SRC_FILES := face_classifier_native.cpp
#LOCAL_C_INCLUDES := $(LOCAL_PATH)
LOCAL_C_INCLUDES += ~/Android/OpenCV-android-sdk/sdk/native/jni/include

LOCAL_LDLIBS +=  -llog -ldl

include $(BUILD_SHARED_LIBRARY)
