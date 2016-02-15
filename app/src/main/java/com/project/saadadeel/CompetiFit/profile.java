package com.project.saadadeel.CompetiFit;

        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.google.gson.Gson;
        import com.project.saadadeel.CompetiFit.connection.DBConnect;
        import com.project.saadadeel.CompetiFit.connection.User;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.logging.Level;
        import java.util.logging.Logger;

/**
 * Created by hp1 on 21-01-2015.
 */
public class profile extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile, container, false);
        return v;
    }
}