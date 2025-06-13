package com.example.schoolsystemapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom Volley request class for uploading files and parameters using multipart/form-data
 */
public class VolleyMultipartRequest extends Request<NetworkResponse> {

    // Listener for successful response
    private final Response.Listener<NetworkResponse> mListener;

    // Listener for error response
    private final Response.ErrorListener mErrorListener;

    // Text parameters to be sent in the request
    private final Map<String, String> mParams;

    // File data to be sent in the request (e.g., PDF, image, etc.)
    private final Map<String, DataPart> mByteData;

    // Constructor
    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mParams = new HashMap<>();
        this.mByteData = new HashMap<>();
    }

    // Add text parameters to the request
    public void setParams(Map<String, String> params) {
        mParams.putAll(params);
    }

    // Add file data with key (e.g., "birth_certificate") and file content
    public void addFile(String key, DataPart dataPart) {
        mByteData.put(key, dataPart);
    }

    // You can override this to add custom headers if needed
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    // Return text parameters
    @Override
    protected Map<String, String> getParams() {
        return mParams;
    }

    // Set content type for multipart request
    @Override
    public String getBodyContentType() {
        // Note: This boundary will not work for actual multipart — you need more logic for real boundary
        return "multipart/form-data;boundary=" + System.currentTimeMillis();
    }

    // Parse the network response from server
    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    // Deliver the response to the success listener
    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    // Deliver error to the error listener
    @Override
    public void deliverError(com.android.volley.VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    /**
     * Inner class to hold file data to send
     */
    public static class DataPart {
        private final String fileName; // e.g., "birth_12345.pdf"
        private final byte[] content;  // file content as byte[]

        public DataPart(String name, byte[] data) {
            this.fileName = name;
            this.content = data;
        }

        // Get file name
        public String getFileName() {
            return fileName;
        }

        // Get raw file content
        public byte[] getContent() {
            return content;
        }
    }
}
