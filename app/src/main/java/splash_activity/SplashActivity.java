package splash_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.race604.drawable.wave.WaveDrawable;

import pl.flanelowapopijava.duel_with_english.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logoImage = (ImageView) findViewById(R.id.splashScreenLogo);
        WaveDrawable waveDrawable = new WaveDrawable(this, R.drawable.logo);
        logoImage.setImageDrawable(waveDrawable);
        waveDrawable.setIndeterminate(true);
    }
}
