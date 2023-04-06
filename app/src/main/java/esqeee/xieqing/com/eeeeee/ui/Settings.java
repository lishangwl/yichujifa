package esqeee.xieqing.com.eeeeee.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.yicu.yichujifa.ui.theme.ThemeManager;

import esqeee.xieqing.com.eeeeee.AppCompatPreferenceActivity;
import esqeee.xieqing.com.eeeeee.R;

public class Settings extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addPreferencesFromResource(R.xml.settings);
        ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ThemeManager.attachTheme(this);
        ThemeManager.attachTheme((Toolbar)findViewById(R.id.toolbar));
    }

}
