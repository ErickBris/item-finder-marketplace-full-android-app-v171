package com.libraries.dataparser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.Data;
import com.models.DataNews;
import com.models.DataResponse;
import com.models.ResponseReview;
import com.models.Status;
import com.models.User;
import com.libraries.ssl.MGHTTPClient;

public class DataParser  {
	
	public InputStream retrieveStream(String url) {
        try {
            HttpClient httpClient = MGHTTPClient.getNewHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("Status Code", "Error " + statusCode + " for URL " + url); 
                return null;
             }
            HttpEntity getResponseEntity = httpResponse.getEntity();
            InputStream stream = getResponseEntity.getContent();
           return stream;
        } 
        catch (IOException e) {
           Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }
        return null;
     }

	public JsonNode getJsonRootNode(String url)	{
		InputStream source = retrieveStream(url);
		if(source == null)
			return null;
		
		JsonFactory f = new JsonFactory();
        f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
        ObjectMapper mapper = new ObjectMapper(f);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		JsonNode rootNode = null;
		try  {
			rootNode = mapper.readTree(source);
		} 
		catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootNode;
	}
	
	public Data getData(String url)	{
		InputStream source = retrieveStream(url);
		if(source == null)
			return null;
		
		JsonFactory f = new JsonFactory();
        f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
        
        ObjectMapper mapper = new ObjectMapper(f);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		Data data = new Data();
		try  {
			data = mapper.readValue(source, Data.class);
		} 
		catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public static DataResponse getJSONFromUrlWithPostRequest(String url, List<NameValuePair> params) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            if(params != null)
            	httpPost.setEntity(new UrlEncodedFormEntity(params));
            
            HttpResponse httpResponse = httpClient.execute(httpPost);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("Status Code", "Error " + statusCode + " for URL " + url); 
                return null;
             }
            
            HttpEntity getResponseEntity = httpResponse.getEntity();
            InputStream source = getResponseEntity.getContent();
            JsonFactory f = new JsonFactory();
            f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
            ObjectMapper mapper = new ObjectMapper(f);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            DataResponse data = new DataResponse();
    		try  {
    			data = mapper.readValue(source, DataResponse.class);
    			return data;
    		} 
    		catch (JsonParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    		catch (JsonMappingException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    		catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
		catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } 
		catch (ClientProtocolException e) {
            e.printStackTrace();
        } 
		catch (IOException e) {
            e.printStackTrace();
        }
		return null;
    }

	public static ResponseReview getJSONFromUrlReview(String url, List<NameValuePair> params) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            if(params != null)
            	httpPost.setEntity(new UrlEncodedFormEntity(params));
            
            HttpResponse httpResponse = httpClient.execute(httpPost);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("Status Code", "Error " + statusCode + " for URL " + url); 
                return null;
             }
            
            HttpEntity getResponseEntity = httpResponse.getEntity();
            InputStream source = getResponseEntity.getContent();
            JsonFactory f = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(f);
    		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            ResponseReview data = new ResponseReview();

    		try  {
    			data = mapper.readValue(source, ResponseReview.class);
    			return data;
    		} 
    		catch (JsonParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    		catch (JsonMappingException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    		catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
		catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } 
		catch (ClientProtocolException e) {
            e.printStackTrace();
        } 
		catch (IOException e) {
            e.printStackTrace();
        }
		return null;
    }

	public static Data getJSONFromUrlWithPostRequestUsingData(String url, List<BasicNameValuePair> params) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			if(params != null)
				httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			final int statusCode = httpResponse.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK) {
				Log.w("Status Code", "Error " + statusCode + " for URL " + url);
				return null;
			}

			HttpEntity getResponseEntity = httpResponse.getEntity();
			InputStream source = getResponseEntity.getContent();

			JsonFactory f = new JsonFactory();
			f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);

			ObjectMapper mapper = new ObjectMapper(f);
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

			Data data = new Data();
			try  {
				data = mapper.readValue(source, Data.class);
				return data;
			}
			catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Data uploadFileWithParams(String url, ArrayList<NameValuePair> params, File file) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			FileBody fileBody = null;
			if(file != null)
				fileBody = new FileBody(file);

			if(fileBody != null)
				reqEntity.addPart("uploaded_file", fileBody);

			if(params != null) {
				Charset charset = Charset.forName("UTF-8");
				for(NameValuePair pair : params) {
					StringBody stringB = new StringBody(pair.getValue(), charset);
					reqEntity.addPart(pair.getName(), stringB);
				}
			}

			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity getResponseEntity = response.getEntity();
			InputStream source = getResponseEntity.getContent();

			/*
			BufferedReader r = new BufferedReader(new InputStreamReader(source));
			StringBuilder total = new StringBuilder();
			String line = null;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
			source.close();

			if(line != null)
				Log.e("response", line);
			*/

			JsonFactory f = new JsonFactory();
			f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);

			ObjectMapper mapper = new ObjectMapper(f);
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			Data data = null;
			try  {
				data = mapper.readValue(source, Data.class);
				return data;
			}
			catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
