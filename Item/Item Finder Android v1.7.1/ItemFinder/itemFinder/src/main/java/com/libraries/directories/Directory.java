package com.libraries.directories;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Directory {

	private static File pfDir;
	
	public Directory(String path){
		pfDir = new File(path);
		Log.e("path getName()", pfDir.getName());
	}
	
	//checks if the directory exist
	public Boolean isExist() {
		if(pfDir.exists())
			return true;
		else
			return false;
	}

	//create a directory
	public void createDir() {
		pfDir.mkdirs();
		
	}

	//get the directory path
	public String getPath() {
		return pfDir.getPath();
	}

	//get the path for the camera
	public String getPFCameraTakenPath() {
		// TODO Auto-generated method stub
		String path = pfDir.getPath()+"/camera/";
		return path;
	}

	//get the path for data
	public String getPFDataPath() {
		// TODO Auto-generated method stub
		String path = pfDir.getPath()+"/data/";
		return path;
	}

	public void createSubDirCameraTaken() {
		// TODO Auto-generated method stub
		File f = new File(getPFCameraTakenPath());
		f.mkdirs();
	}

	public void createSubDirData() {
		// TODO Auto-generated method stub
		File f = new File(getPFDataPath());
		f.mkdirs();
	}

	//checks if there are existing SD Card mounted
	public Boolean checkifItHasExternalMemory() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		
		return false;
	}

}
