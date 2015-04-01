package no.lqasse.zoff;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;

import no.lqasse.zoff.Helpers.ToastMaster;
import no.lqasse.zoff.Server.Server;

/**
 * Created by lassedrevland on 21.01.15.
 */
public class SettingsActivity extends ActionBarActivity {
    private final String PREFS_FILE = "no.lqasse.zoff.prefs";
    private HashMap<String, Boolean> settings = new HashMap<>();
    private String password;
    private String ROOM_NAME;
    private String POST_URL;
    private SharedPreferences sharedPreferences;


    private CheckBox voteCB;
    private CheckBox addsongsCB;
    private CheckBox longsongsCB;
    private CheckBox frontpageCB;
    private CheckBox allvideosCB;
    private CheckBox removeplayCB;
    private CheckBox skipCB;
    private CheckBox shuffleCB;


    private Button postSettingsBtn;
    private EditText pwField;
    private ProgressBar progressBar;

    private Activity settingsActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Intent i = getIntent();
        Bundle b = i.getExtras();

        settings.put("vote", b.getBoolean("vote"));
        settings.put("addsongs", b.getBoolean("addsongs"));
        settings.put("longsongs", b.getBoolean("longsongs"));
        settings.put("frontpage", b.getBoolean("frontpage"));
        settings.put("allvideos", b.getBoolean("allvideos"));
        settings.put("removeplay", b.getBoolean("removeplay"));
        settings.put("skip",b.getBoolean("skip"));
        settings.put("shuffle",b.getBoolean("shuffle"));

        ROOM_NAME = b.getString("ROOM_NAME");
        password = getPassword();

        voteCB = (CheckBox) findViewById(R.id.vote);
        addsongsCB = (CheckBox) findViewById(R.id.addsongs);
        longsongsCB = (CheckBox) findViewById(R.id.longsongs);
        frontpageCB = (CheckBox) findViewById(R.id.frontpage);
        allvideosCB = (CheckBox) findViewById(R.id.allvideos);
        removeplayCB = (CheckBox) findViewById(R.id.removeplay);
        skipCB = (CheckBox) findViewById(R.id.skip);
        shuffleCB  = (CheckBox) findViewById(R.id.shuffle);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        postSettingsBtn = (Button) findViewById(R.id.postSettingsBtn);
        pwField = (EditText) findViewById(R.id.pwEditText);


//Første er reversert pga lolzoff, husk å reversere når innstillinger settes

        voteCB.setChecked(!settings.get("vote")); //Reversed
        addsongsCB.setChecked(!settings.get("addsongs")); //Reversed
        longsongsCB.setChecked(settings.get("longsongs"));
        frontpageCB.setChecked(settings.get("frontpage"));
        allvideosCB.setChecked(settings.get("allvideos"));
        removeplayCB.setChecked(settings.get("removeplay"));
        skipCB.setChecked(settings.get("skip"));
        shuffleCB.setChecked(settings.get("shuffle"));

        if (!password.equals("")) {
            pwField.setText(password);
        }





        postSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = pwField.getText().toString();

                Boolean vote =       (!voteCB.isChecked()); //Reverse back to match zoff
                Boolean addsongs =   (!addsongsCB.isChecked());//Reverse back to match zoff
                Boolean longsongs =  (longsongsCB.isChecked());
                Boolean frontpage =  (frontpageCB.isChecked());
                Boolean allvideos =  (allvideosCB.isChecked());
                Boolean removeplay = (removeplayCB.isChecked());
                Boolean skip = (skipCB.isChecked());
                Boolean shuffle = (shuffleCB.isChecked());

                Server.postSettings(settingsActivity, password, vote, addsongs, longsongs, frontpage, allvideos, removeplay, skip, shuffle);

                progressBar.setVisibility(View.VISIBLE);
                postSettingsBtn.setEnabled(false);
            }
        });


    }

    public void settingsPostResponse(String response){
        progressBar.setVisibility(View.GONE);
        postSettingsBtn.setEnabled(true);

        if (response.contains("correct")) {
            ToastMaster.showToast(this, ToastMaster.TYPE.SAVED_SETTINGS);
            password = pwField.getText().toString();
            setPass(password);


        } else {
            ToastMaster.showToast(this, ToastMaster.TYPE.WRONG_PASSWORD_SETTINGS);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getPassword() {
        String password;
        sharedPreferences = getSharedPreferences(PREFS_FILE, 0);
        password = sharedPreferences.getString(ROOM_NAME, "");

        return password;

    }

    private void setPass(String password) {
        sharedPreferences = getSharedPreferences(PREFS_FILE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ROOM_NAME, password);
        editor.commit();
    }

    @Override
    protected void onResume() {
        stopNotificationService();
        super.onResume();
    }

    @Override
    protected void onPause() {
        startNotificationService();


        super.onPause();
    }

    public void startNotificationService() {
        Intent notificationIntent = new Intent(this, NotificationService.class);
        notificationIntent.putExtra("ROOM_NAME", ROOM_NAME);
        startService(notificationIntent);
    }

    private void stopNotificationService(){
        Intent notificationIntent = new Intent(this, NotificationService.class);
        stopService(notificationIntent);
    }

    @Override
    protected void onStop() {
        startNotificationService();
        super.onStop();
    }

}
