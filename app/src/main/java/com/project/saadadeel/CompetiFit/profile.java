package com.project.saadadeel.CompetiFit;

        import android.annotation.TargetApi;
        import android.app.ActionBar;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.NavUtils;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.gson.Gson;
        import com.project.saadadeel.CompetiFit.Models.Races;
        import com.project.saadadeel.CompetiFit.Models.User;
        import com.project.saadadeel.CompetiFit.connection.DBConnect;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.EmptyStackException;

/**
 * Created by hp1 on 21-01-2015.
 */
public class profile extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    User user;
    Gson gson = new Gson();
    Context context = this;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String json = this.sharedPreferences.getString("user", "");
        this.user = gson.fromJson(json, User.class);
        this.token = this.sharedPreferences.getString("TOKEN", "");

        populateView();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        profile.this.finish();
    }

    public void populateView(){
        final TextView username = (TextView) findViewById(R.id.profile_username);
        username.setText("Username : "+ this.user.getUsername());

        TextView name = (TextView) findViewById(R.id.profile_name);
        name.setText("Name : "+ this.user.getUserFirstName() + " " + this.user.getUserLastName());

        TextView runs= (TextView) findViewById(R.id.profile_runs);
        runs.setText("Total runs : " + this.user.getRuns().size());

        TextView races = (TextView) findViewById(R.id.profile_races);
        races.setText("Username : " + this.user.getRaces().size());

        EditText oldPassword = (EditText) findViewById(R.id.profile_password_current);
        final String password = oldPassword.getText().toString();

        EditText newPasswordEdit = (EditText) findViewById(R.id.profile_password_new);
        final String newPassword = newPasswordEdit.getText().toString();

        EditText repeatPasswordEdit= (EditText) findViewById(R.id.profile_password_new_repeat);
        final String repeatPassword = repeatPasswordEdit.getText().toString();

        Button button = (Button) findViewById(R.id.change_password);
        button.setOnClickListener(new View.OnClickListener() {
            boolean raceSent = false;

            public void onClick(View v) {
                JSONObject object = new JSONObject();
                if(!password.equals(user.getUserPassword())){
                    Toast.makeText(context, "Old Password Incorrect",
                            Toast.LENGTH_LONG).show();
                }else if(newPassword.equals(repeatPassword)){
                    Toast.makeText(context, "Passwords do not match",
                            Toast.LENGTH_LONG).show();
                }else{
                    try {
                        object.put("newPassword", newPassword);
                        object.put("username", user.getUsername());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (isNetworkAvailable()) {
                        DBConnect db = new DBConnect(object, token);
                        db.post("/passwordChange");
                        Toast.makeText(context, "Password Changed",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, UserMain.class);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "No internet connection available",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}