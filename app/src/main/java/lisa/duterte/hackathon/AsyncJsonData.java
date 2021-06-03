package lisa.duterte.hackathon;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AsyncJsonData extends AsyncTask<String, Void, JSONObject> {

    private AppCompatActivity myActivity;
    DatabaseReference databaseReference;
    JSONArray items;
    Double pos_la,pos_lo;
    Long temperature = 40l;
    ZoneChaleur zoneChaleur;
    PositionDrone positionDrone;
    Integer temperatureFeu = 40;

    public AsyncJsonData(AppCompatActivity mainActivity) {
        myActivity = mainActivity;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        URL url = null;
        HttpURLConnection urlConnection = null;
        String result = null;
        try {
            url = new URL("http","192.168.43.167",8080,strings[0]);

            urlConnection = (HttpURLConnection) url.openConnection(); // Open
            urlConnection.setRequestMethod("GET");
            urlConnection.addRequestProperty("User-Agent","ACME 0.7.3");
            urlConnection.addRequestProperty("Content-Type","application/json");
            urlConnection.addRequestProperty("Accept","application/json");
            urlConnection.addRequestProperty("X-M2M-Origin","CAdmin");
            urlConnection.addRequestProperty("X-M2M-RI","0");
            urlConnection.addRequestProperty("X-M2M-RVI","3");

            InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // Stream

            result = readStream(in); // Read stream

            System.out.println(result);
        }
        catch (MalformedURLException e) { e.printStackTrace(); }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        JSONObject json = null;
        try {
            json = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json; // returns the result
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        try {
            JSONObject hello = s.getJSONObject("m2m:env");


            pos_la = hello.getJSONObject("latitude").getDouble("d") + hello.getJSONObject("latitude").getDouble("m")/60 + hello.getJSONObject("latitude").getDouble("s")/360;
            pos_lo = hello.getJSONObject("longitude").getDouble("d") + hello.getJSONObject("latitude").getDouble("m")/60 + hello.getJSONObject("latitude").getDouble("s")/360;
            temperature = hello.getLong("tp");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (temperature >= temperatureFeu) {
            notificationDialog();
            databaseReference = FirebaseDatabase.getInstance().getReference("Historique");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int cpt=0;

                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        String p = child.getValue(String.class);
                        if (p != null ) {
                            cpt++;
                        }
                    }
                    String zone = "Zone" + cpt;
                    String position = "(" + pos_la + "," + pos_lo + ")" ;
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                    zoneChaleur = new ZoneChaleur();
                    zoneChaleur.setDateHeure(date);
                    zoneChaleur.setPosition(position);
                    zoneChaleur.setTemperature(temperature);

                    databaseReference.child(zone).setValue(zoneChaleur);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("CoordonneesDrone");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                positionDrone = new PositionDrone();
                positionDrone.setLatitude(pos_la);
                positionDrone.setLongitude(pos_lo);

                databaseReference.setValue(positionDrone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();

        // Extracting the JSON object from the String
        String jsonextracted = sb.toString();

        return jsonextracted;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog() {
        NotificationManager notificationManager = (NotificationManager) myActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = String.valueOf(R.string.app_name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(myActivity.getApplicationContext(),NOTIFICATION_CHANNEL_ID);
        notificationBuilder
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(String.valueOf(R.string.app_name))
                .setContentText("Zone de forte chaleur détectée!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if(notificationManager != null) {
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

}