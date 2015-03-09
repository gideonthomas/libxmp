#include <jni.h>
#include <android/log.h>

// Must be defined to instantiate template classes
#define TXMP_STRING_TYPE std::string

// Must be defined to give access to XMPFiles
#define XMP_INCLUDE_XMPFILES 1

#include <cstdio>
#include <vector>
#include <string>
#include <cstring>
#include <cstdlib>
#include <map>

// Ensure XMP templates are instantiated
#include "public/include/XMP.incl_cpp"

// Provide access to the API
#include "public/include/XMP.hpp"

#include <iostream>
#include <fstream>

extern "C" {
JNIEXPORT jint JNICALL Java_com_xmptest_XMPTest_setLicense(JNIEnv *env, jobject obj,
		jstring fileName, jstring licenseName);
JNIEXPORT jstring JNICALL Java_com_xmptest_XMPTest_getLicense(JNIEnv *env, jobject obj,
		jstring fileName);
};

void terminate() {
	SXMPFiles::Terminate();
	SXMPMeta::Terminate();
}

JNIEXPORT jint JNICALL Java_com_xmptest_XMPTest_setLicense(JNIEnv *env, jobject obj,
		jstring fileName, jstring licenseName) {
	std::string fName = env->GetStringUTFChars(fileName, NULL);
	std::string licenseKey = env->GetStringUTFChars(licenseName, NULL);

	if(!SXMPMeta::Initialize()) {
		__android_log_print(ANDROID_LOG_VERBOSE, "XMPTest", "Could not initialize XMPMeta component");
		return -1;
	}

	__android_log_print(ANDROID_LOG_VERBOSE, "XMPTest", "Initialized XMPMeta component");

	XMP_OptionBits options = kXMPFiles_ServerMode | kXMPFiles_IgnoreLocalText;

	try {
		if(!SXMPFiles::Initialize(options)) {
			__android_log_print(ANDROID_LOG_VERBOSE, "XMPTest", "Could not initialize XMPFiles component");
			terminate();
			return -1;
		}

		__android_log_print(ANDROID_LOG_VERBOSE, "XMPTest", "Initialized XMPFiles component");

		XMP_OptionBits opts = kXMPFiles_OpenForUpdate | kXMPFiles_OpenUseSmartHandler;
		bool ok;
		SXMPFiles myFile;
		SXMPMeta meta;
		std::map<std::string, std::string> licenses;

		licenses["Attribution"] = "https://creativecommons.org/licenses/by/4.0/legalcode";
		licenses["Attribution-ShareAlike"] = "https://creativecommons.org/licenses/by-sa/4.0/legalcode";
		licenses["Attribution-NoDerivs"] = "https://creativecommons.org/licenses/by-nd/4.0/legalcode";
		licenses["Attribution-NonCommercial"] = "https://creativecommons.org/licenses/by-nc/4.0/legalcode";
		licenses["Attribution-NonCommercial-ShareAlike"] = "https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode";
		licenses["Attribution-NonCommercial-NoDerivs"] = "https://creativecommons.org/licenses/by-nc-nd/4.0/legalcode";

		ok = myFile.OpenFile(fName, kXMP_JPEGFile, opts);

		char *buf = (char*)malloc(fName.length() + 1);
		strcpy(buf, fName.c_str());

		if(!ok) {
			__android_log_print(ANDROID_LOG_VERBOSE, "XMPTest", "Could not open file %s", buf);
			free(buf);
			terminate();
			return -1;
		}

		__android_log_print(ANDROID_LOG_VERBOSE, "XMPTest", "Successfully opened file %s", buf);

		myFile.GetXMP(&meta);

		__android_log_print(ANDROID_LOG_VERBOSE, "XMPTest", "Retrieved XMP metadata from file");

		meta.SetProperty(kXMP_NS_XMP_Rights, "WebStatement", licenses[licenseKey], 0);

		__android_log_print(ANDROID_LOG_VERBOSE, "XMPTest", "Modified XMP metadata for file");

		myFile.PutXMP(meta);

		__android_log_print(ANDROID_LOG_VERBOSE, "XMPTest", "Saving XMP metadata to file");

		myFile.CloseFile();

		__android_log_print(ANDROID_LOG_VERBOSE, "XMPTest", "Successfully updated XMP metadata for file");
		free(buf);
		terminate();
		return 0;
	} catch(XMP_Error &e) {
		__android_log_print(ANDROID_LOG_VERBOSE, "XMPTest", "Encountered exception %s", e.GetErrMsg());
		return -1;
	}
}

JNIEXPORT jstring JNICALL Java_com_xmptest_XMPTest_getLicense(JNIEnv *env, jobject obj,
		jstring fileName) {
	std::string fName = env->GetStringUTFChars(fileName, NULL);

	if(!SXMPMeta::Initialize()) {
		return NULL;
	}

	XMP_OptionBits options = kXMPFiles_ServerMode | kXMPFiles_IgnoreLocalText;

	try {
		if(!SXMPFiles::Initialize(options)) {
			terminate();
			return NULL;
		}

		XMP_OptionBits opts = kXMPFiles_OpenForRead | kXMPFiles_OpenUseSmartHandler;
		bool ok;
		SXMPFiles myFile;
		SXMPMeta meta;
		std::map<std::string, std::string> licenses;

		licenses["Attribution"] = "https://creativecommons.org/licenses/by/4.0/legalcode";
		licenses["Attribution-ShareAlike"] = "https://creativecommons.org/licenses/by-sa/4.0/legalcode";
		licenses["Attribution-NoDerivs"] = "https://creativecommons.org/licenses/by-nd/4.0/legalcode";
		licenses["Attribution-NonCommercial"] = "https://creativecommons.org/licenses/by-nc/4.0/legalcode";
		licenses["Attribution-NonCommercial-ShareAlike"] = "https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode";
		licenses["Attribution-NonCommercial-NoDerivs"] = "https://creativecommons.org/licenses/by-nc-nd/4.0/legalcode";

		ok = myFile.OpenFile(fName, kXMP_JPEGFile, opts);

		if(!ok) {
			terminate();
			return NULL;
		}

		myFile.GetXMP(&meta);

		std::string value = "";

		meta.GetProperty(kXMP_NS_XMP_Rights, "WebStatement", &value, 0);

		char *buf = (char*)malloc(value.length() + 1);
		strcpy(buf, value.c_str());
		jstring license = env->NewStringUTF(buf);
		free(buf);

		return license;
	} catch(XMP_Error &e) {
		return NULL;
	}
}
