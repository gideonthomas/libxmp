LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/XMPToolkit \
	$(LOCAL_PATH)/XMPToolkit/public/include
	
LOCAL_CFLAGS := -O3 -DNDEBUG -fstrict-aliasing -fexceptions -DUNIX_ENV -Wno-return-type

LOCAL_SRC_FILES := XMPTest.cpp

LOCAL_MODULE    := XMPTest

LOCAL_SHARED_LIBRARIES := libxmptoolkit

LOCAL_NDK_STL_VARIANT := gnustl_static

LOCAL_ARM_MODE := arm

include $(BUILD_SHARED_LIBRARY)
