package com.androidbro.verticalcolorseekbarexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidbro.verticalcolorseekbar.VerticalColorSeekBar;

public class MainActivity extends AppCompatActivity {

    protected VerticalColorSeekBar colorSeekBar;
    protected TextView currentColorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colorSeekBar = (VerticalColorSeekBar) findViewById(R.id.color_seek_bar);
        currentColorTextView = (TextView) findViewById(R.id.current_color);
        colorSeekBar.setOnColorChangedListener(new VerticalColorSeekBar.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                currentColorTextView.setText(String.format("Current color code: %1d", color));
            }
        });
        colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
