package co.id.dicoding.moviecatalogueapi.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import co.id.dicoding.moviecatalogueapi.R;
import co.id.dicoding.moviecatalogueapi.constant.Constant;
import co.id.dicoding.moviecatalogueapi.receiver.AlarmReceiver;
import co.id.dicoding.moviecatalogueapi.util.PreferencesUtil;

public class ConfigActivity extends AppCompatActivity {

    Switch releaseBtnSwitch, dailyBtnSwitch;
    private final String release = "co.id.dicoding.moviecatalogueapi.RELEASE";
    private final String daily = "co.id.dicoding.moviecatalogueapi.DAILY";
    private AlarmReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.header_config_title));
        }

        alarmReceiver = new AlarmReceiver();

        releaseBtnSwitch = findViewById(R.id.switch1);
        dailyBtnSwitch = findViewById(R.id.switch2);

        checkState();
        switchHandler();
    }

    private void checkState() {
        Boolean isReleaseSetled = PreferencesUtil.getBoolean(this, Constant.KEY_PREF_RELEASE_REMINDER);
        Boolean isDailySetled = PreferencesUtil.getBoolean(this, Constant.KEY_PREF_DAILY_REMINDER);

        if (isReleaseSetled) {
            releaseBtnSwitch.setChecked(true);
        }

        if (isDailySetled) {
            dailyBtnSwitch.setChecked(true);
        }
    }

    private void switchHandler() {

        releaseBtnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("releaseBtnSwitch", "onCheckedChanged: " + isChecked);
                doSaveConfig(isChecked, release);
            }
        });

        dailyBtnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("dailyBtnSwitch", "onCheckedChanged: " + isChecked);
                doSaveConfig(isChecked, daily);
            }
        });

    }

    private void doSaveConfig(Boolean state, String flag) {
        switch (flag) {
            case release:
                PreferencesUtil.save(this, Constant.KEY_PREF_RELEASE_REMINDER, state);
                if (state == true) {
                    alarmReceiver.setReminder(ConfigActivity.this, AlarmReceiver.TYPE_RELEASE_REMINDER);
                } else {
                    alarmReceiver.cancelAlarm(ConfigActivity.this, AlarmReceiver.TYPE_RELEASE_REMINDER);
                }
                break;
            case daily:
                PreferencesUtil.save(this, Constant.KEY_PREF_DAILY_REMINDER, state);
                if (state == true) {
                    alarmReceiver.setReminder(ConfigActivity.this, AlarmReceiver.TYPE_DAILY_REMINDER);
                } else {
                    alarmReceiver.cancelAlarm(ConfigActivity.this, AlarmReceiver.TYPE_DAILY_REMINDER);
                }
                break;
        }
    }
}
