package com.libraries.bitmap;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.Data;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class MGImageUtils {

	public static Bitmap getBitmap(String imagePath) {

	    long size_file = getFileSize(new File(imagePath));

	    size_file = (size_file) / 1000; // in Kb now
	    int ample_size = 1;
	    // ample size is used to reduce size of bitmap
	    if (size_file <= 250) {
	         ample_size = 2;
	    } else if (size_file > 251 && size_file < 1500) {  
	        ample_size = 4; 
	    } else if (size_file >= 1500 && size_file < 3000){
	        ample_size = 8; 
	    } else if (size_file >= 3000 && size_file <= 4500){ 
	        ample_size = 12; 
	    } else if (size_file >= 4500) {
	       ample_size = 16;
	  }

	      Bitmap bitmap = null;
	      BitmapFactory.Options bitoption = new BitmapFactory.Options();
	      bitoption.inSampleSize = ample_size;

	      Bitmap bitmapPhoto = BitmapFactory.decodeFile(imagePath, bitoption);

	      ExifInterface exif = null;
	      try {
	         exif = new ExifInterface(imagePath);
	        } catch (IOException e) {
	        e.printStackTrace(); 
	        }
	     int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
	     Matrix matrix = new Matrix();

	     if ((orientation == 3)) {
	               matrix.postRotate(180);
	               bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
	               bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
	                true);
	      } else if (orientation == 6) {
	               matrix.postRotate(90);
	               bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
	               bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
	                true);
	      } else if (orientation == 8) {
	               matrix.postRotate(270);
	               bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
	               bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
	               true);

	     } else {
	               matrix.postRotate(0);
	               bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
	               bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
	               true);
	     }
	   return bitmap;
	}
	
	public static long getFileSize(final File file) {
        if (file == null || !file.exists())
             return 0;
        if (!file.isDirectory())
             return file.length();
         final List<File> dirs = new LinkedList<File>();
               dirs.add(file);
               long result = 0;
              
              while (!dirs.isEmpty()) {
                      final File dir = dirs.remove(0);
                      if (!dir.exists())
                      continue;
                      final File[] listFiles = dir.listFiles();
                      if (listFiles == null || listFiles.length == 0)
                      continue;
                     for (final File child : listFiles) {
                              result += child.length();
                              if (child.isDirectory())
                               dirs.add(child);
                               }
                 }
              return result;
	}
	
	public static Data uploadFile(String fileName, String strURL) {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(fileName); 
        int serverResponseCode = -1;
        String serverResponseMessage = "";
         
        if (!sourceFile.isFile()) {
        	Log.e("uploadFile", "Source File not exist :" + fileName);	
        	return null;
        }
        
        Log.e("fileName", fileName);
        
        Data data = null;
        
        try { 
            // open a URL connection to the Servlet
        	FileInputStream fileInputStream = new FileInputStream(sourceFile);
        	URL url = new URL(strURL);
			  
        	Log.e("URL", url.toString());
        	String imgName = String.valueOf(System.currentTimeMillis()) + ".jpg";
        	
			// Open a HTTP  connection to  the URL
			conn = (HttpURLConnection) url.openConnection(); 
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("uploaded_file", imgName);
			           
			dos = new DataOutputStream(conn.getOutputStream());	 
			dos.writeBytes(twoHyphens + boundary + lineEnd);

//			String header = String.format(
//					"Content-Disposition: form-data; name=\"uploaded_file\";filename=\"%s\";store_id=\"%d\"%s",
//					fileName, 
//					storeId,
//					lineEnd);
			
			dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" 
					+ imgName + "\"" + lineEnd);
			
//			dos.writeBytes(header);
			dos.writeBytes(lineEnd);
	 
			// create a buffer of  maximum size
			bytesAvailable = fileInputStream.available(); 
	 
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
	 
			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
	             
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
	            bytesAvailable = fileInputStream.available();
	            bufferSize = Math.min(bytesAvailable, maxBufferSize);
	            bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
			}
	 
			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			 
			// Responses from the server (code and message)
			serverResponseCode = conn.getResponseCode();
			serverResponseMessage = conn.getResponseMessage();
			
			InputStream source = conn.getInputStream();
			
//            BufferedReader reader = new BufferedReader(new InputStreamReader(source, "UTF-8"), 8);
//            StringBuilder sb = new StringBuilder();
//
//            String line = null;
//            while ((line = reader.readLine()) != null)
//            	sb.append(line + "\n");
//            Log.e("sb.toString();", sb.toString());
            
			JsonFactory f = new JsonFactory();
            f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
            
            ObjectMapper mapper = new ObjectMapper(f);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            
            data = mapper.readValue(source, Data.class);
			
			Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
			if(serverResponseCode == 200) {
				Log.e("SUCCESS", "UPLOAD SUCCESS");               
			}    
			   
			//close the streams
			fileInputStream.close();
			dos.flush();
			dos.close();
			        
        	}catch (Exception e) {
        		e.printStackTrace();
        	}  
        
        return data; 
	}
	
	public static String getRealPathFromURI(Uri contentUri, Context c) {
		
	    String[] proj = { MediaStore.Images.Media.DATA };
	    CursorLoader loader = new CursorLoader(c, contentUri, proj, null, null, null);
	    Cursor cursor = loader.loadInBackground();
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
}
