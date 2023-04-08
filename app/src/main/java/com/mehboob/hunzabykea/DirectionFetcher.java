package com.mehboob.hunzabykea;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DirectionFetcher implements Runnable {
        private final String apiKey;
        private final LatLng origin;
        private final LatLng destination;
        private final OnDirectionsFetchedListener listener;

        public DirectionFetcher(String apiKey, LatLng origin, LatLng destination, OnDirectionsFetchedListener listener) {
            this.apiKey = apiKey;
            this.origin = origin;
            this.destination = destination;
            this.listener = listener;
        }

        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            String url = String.format(Locale.US,
                    "https://maps.googleapis.com/maps/api/directions/json?origin=%f,%f&destination=%f,%f&key=%s",
                    origin.latitude, origin.longitude, destination.latitude, destination.longitude, apiKey);

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String json = response.body().string();
                JSONObject jsonObject = new JSONObject(json);
                JSONArray routes = jsonObject.getJSONArray("routes");
                JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");
                List<LatLng> path = new ArrayList<>();
                for (int i = 0; i < steps.length(); i++) {
                    JSONObject step = steps.getJSONObject(i);
                    JSONObject startLocation = step.getJSONObject("start_location");
                    path.add(new LatLng(startLocation.getDouble("lat"), startLocation.getDouble("lng")));
                    JSONArray polyline = step.getJSONObject("polyline").getJSONArray("points");
                    path.addAll(PolyUtil.decode(polyline.toString()));
                    JSONObject endLocation = step.getJSONObject("end_location");
                    path.add(new LatLng(endLocation.getDouble("lat"), endLocation.getDouble("lng")));
                }
                listener.onDirectionsFetched(path);
            } catch (IOException | JSONException e) {
                // Handle any errors
                e.printStackTrace();
                listener.onDirectionsFetchFailed();
            }
        }

        public interface OnDirectionsFetchedListener {
            void onDirectionsFetched(List<LatLng> path);
            void onDirectionsFetchFailed();
        }


}
