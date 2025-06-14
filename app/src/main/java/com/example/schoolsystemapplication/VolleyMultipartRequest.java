package com.example.schoolsystemapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VolleyMultipartRequest extends Request<NetworkResponse> {
    private final Response.Listener<NetworkResponse> mListener;
    private final Response.ErrorListener mErrorListener;
    private final Map<String, String> mParams;
    private final Map<String, DataPart> mByteData;
    private String boundary;

    public VolleyMultipartRequest(int method, String url,
                                Response.Listener<NetworkResponse> listener,
                                Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mParams = new HashMap<>();
        this.mByteData = new HashMap<>();
    }

    public void setParams(Map<String, String> params) {
        mParams.putAll(params);
    }

    public void addFile(String key, DataPart dataPart) {
        mByteData.put(key, dataPart);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() {
        return mParams;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        boundary = "apiclient-" + System.currentTimeMillis();
        String lineEnd = "\r\n";
        String twoHyphens = "--";

        try {
            // Text params
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                bos.write((twoHyphens + boundary + lineEnd).getBytes());
                bos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + lineEnd).getBytes());
                bos.write(("Content-Type: text/plain; charset=UTF-8" + lineEnd).getBytes());
                bos.write(lineEnd.getBytes());
                bos.write(entry.getValue().getBytes("UTF-8"));
                bos.write(lineEnd.getBytes());
            }

            // File params
            for (Map.Entry<String, DataPart> entry : mByteData.entrySet()) {
                DataPart dp = entry.getValue();
                bos.write((twoHyphens + boundary + lineEnd).getBytes());
                bos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"; filename=\"" + dp.getFileName() + "\"" + lineEnd).getBytes());
                bos.write(("Content-Type: application/octet-stream" + lineEnd).getBytes());
                bos.write(lineEnd.getBytes());
                bos.write(dp.getContent());
                bos.write(lineEnd.getBytes());
            }

            bos.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(com.android.volley.VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    public static class DataPart {
        private final String fileName;
        private final byte[] content;

        public DataPart(String name, byte[] data) {
            this.fileName = name;
            this.content = data;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }
    }
}