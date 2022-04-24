package com.pnx.momassignment.gsonConverter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.pnx.momassignment.util.JsonLogPrint;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

public class CustomGsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = gson.newJsonWriter(writer);
        adapter.write(jsonWriter, value);
        jsonWriter.close();

        String TAG = "SEND_Body " + adapter.fromJson("{}").getClass().getSimpleName();
        String jsonString = JsonLogPrint.printJson(TAG, adapter.toJson(value), true);
        RequestBody requestBody = RequestBody.create(buffer.readByteArray(), MEDIA_TYPE);
        return requestBody;
    }
}
