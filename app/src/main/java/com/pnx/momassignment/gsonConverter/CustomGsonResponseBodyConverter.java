package com.pnx.momassignment.gsonConverter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.pnx.momassignment.network.recive.RecvBase;
import com.pnx.momassignment.util.JsonLogPrint;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    public CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
//        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        String json = value.string();
        try {
//            T result = adapter.read(jsonReader);
//            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
//                throw new JsonIOException("JSON document was not fully consumed.");
//            }
            String TAG = "API " + adapter.fromJson("{}").getClass().getSimpleName();
            String jsonString = JsonLogPrint.printJson(TAG, json, true);

            T result = adapter.fromJson(json);//adapter.read(jsonReader);
//            String TAG = "API " + result.getClass().getSimpleName();
//            String jsonString = JsonLogPrint.printJson(TAG, gson.toJson(result), true);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            JsonLogPrint.printJson("CustomGsonResponseBodyConverter", json);
            return (T) new RecvBase();
        } finally {
            value.close();
        }
    }
}