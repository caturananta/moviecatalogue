package co.id.dicoding.moviecatalogueapi.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import co.id.dicoding.moviecatalogueapi.R;
import co.id.dicoding.moviecatalogueapi.constant.Constant;
import co.id.dicoding.moviecatalogueapi.util.LanguageUtil;

public class LanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.header_language_title));
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.lang_id:
                if (checked)
                    LanguageUtil.updateBaseContextLocale(this, Constant.KEY_LANG_ID);
                restartActivity(Constant.KEY_LANG_ID);
                break;
            case R.id.lang_en:
                if (checked)
                    LanguageUtil.updateBaseContextLocale(this, Constant.KEY_LANG_EN);
                restartActivity(Constant.KEY_LANG_EN);
                break;
        }
    }

    private void restartActivity(String lang) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constant.EXTRA_DATA, lang);
        startActivity(intent);
        finish();
    }
}
