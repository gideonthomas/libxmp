LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

include $(LOCAL_PATH)/../XMPToolkit/Android.mk

include $(CLEAR_VARS)
	
LOCAL_CFLAGS := -O3 -DNDEBUG -fstrict-aliasing -fexceptions -DUNIX_ENV -Wno-return-type

LOCAL_SRC_FILES := \
	../jni/XMPTest.cpp
	
LOCAL_ARM_MODE := arm

LOCAL_NDK_STL_VARIANT := gnustl_static

LOCAL_LDLIBS := -llog

LOCAL_MODULE_TAGS := optional
LOCAL_SHARED_LIBRARIES := libxmptoolkit

LOCAL_MODULE := libxmptest

-include $(LOCAL_PATH)/gnustl.mk

include $(BUILD_SHARED_LIBRARY)
