package net.webilisim.websliderdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import net.webilisim.webslider.IndicatorAnimations;
import net.webilisim.webslider.WEBSliderAnimations;
import net.webilisim.webslider.WEBSliderView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start image slider
        WEBSliderView WEBSliderView = findViewById(R.id.webSliderView);
        WEBSliderView.setSliderAdapter(new SliderAdapterExample(this));
        WEBSliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        WEBSliderView.setSliderTransformAnimation(WEBSliderAnimations.FADETRANSFORMATION);
        WEBSliderView.setAutoCycleDirection(net.webilisim.webslider.WEBSliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        WEBSliderView.setIndicatorSelectedColor(Color.WHITE);
        WEBSliderView.setIndicatorUnselectedColor(Color.GRAY);
        WEBSliderView.setScrollTimeInSec(4);
        WEBSliderView.startAutoCycle();
    }
}
