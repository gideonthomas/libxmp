package com.xmptest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XMPTest {

	public final static ArrayList<String> Licenses = new ArrayList<String>((List<String>) Arrays
			.asList(new String[] { "Attribution", "Attribution-ShareAlike",
					"Attribution-NoDerivs", "Attribution-NonCommercial",
					"Attribution-NonCommercial-ShareAlike",
					"Attribution-NonCommercial-NoDerivs" }));

	public native int setLicense(String fileName, String licenseName);

	public native String getLicense(String fileName);

	static {
		System.loadLibrary("xmptest");
	}
}
