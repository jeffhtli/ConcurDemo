package com.telenav.jeff.concur.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public abstract class ConcurService
{
    public static final String AUTH_PARAM_CLIENT_ID = "wtGLebftwemK3gHn2em4nM";
    public static final String AUTH_PARAM_CONSUMER_SECRET = "ZRrrj92cODHCv6dXmtbaEJlgbWITGQs4";
    
    private static final String HTTP_HEADER_AUTHORIZATION = "Authorization";
    private static final String HTTP_HEADER_ACCEPT = "Accept";
    private static final String LOG_TAG = "ConcurService";
    
    
    
    
    
    protected static HttpClient httpClient;
    
    static 
    {
        httpClient = new DefaultHttpClient();
    }
    
    protected byte[] sendRequest(HttpUriRequest request)
    {
        request.addHeader(HTTP_HEADER_AUTHORIZATION, "OAuth " + TokenManager.getToken().getToken());
        request.addHeader(HTTP_HEADER_ACCEPT, "application/xml");
        
        Log.d(LOG_TAG, request.getURI().toString());
        
        byte[] result = null;
        try
        {
            HttpResponse response = httpClient.execute(request);
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();
            
            Log.d(LOG_TAG, String.format("HTTP Response code: %s", statusCode));
            
            if (statusCode == HttpStatus.SC_OK)
            {
               HttpEntity entity = response.getEntity();
               
               int length = (int)entity.getContentLength();
               Log.d(LOG_TAG, "Content Length: " + length);
               
               result = new byte[length];
               InputStream is = response.getEntity().getContent();
               BufferedInputStream bis = new BufferedInputStream(is);
               bis.read(result);
               bis.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }
    
    protected byte[] get(String url)
    {
        HttpGet request = new HttpGet(url);
        return sendRequest(request);
    }
    
}
