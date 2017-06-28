package com.csci448.dkern_a4.breakout;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements SensorEventListener
{
    private GameView mGameView;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mAmbientLightSensor;

    /**
     * Starts activity and game
     * Gets sensors
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAmbientLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mAmbientLightSensor, SensorManager.SENSOR_DELAY_UI);

        mGameView = (GameView)findViewById(R.id.game_view);
    }

    /**
     * Handles accelerometer and ambient light sensor events
     * Move paddle or change color of screen
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        switch(event.sensor.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                mGameView.movePaddle(event.values[0]);
                break;
            case Sensor.TYPE_LIGHT:
                if(event.values[0] >= 10)
                {
                    mGameView.mBackgroundColor = Color.LTGRAY;
                    mGameView.mBrickColor = Color.CYAN;
                    mGameView.mPaddleColor = Color.GREEN;
                    mGameView.mBallColor = Color.YELLOW;
                }
                else
                {
                    mGameView.mBackgroundColor = Color.DKGRAY;
                    mGameView.mBrickColor = Color.LTGRAY;
                    mGameView.mPaddleColor = Color.BLUE;
                    mGameView.mBallColor = Color.RED;
                }
                break;
        }
    }

    /**
     * Unused
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    /**
     * Registers listeners when return to activity
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mAmbientLightSensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Unregisters sensors when activity stops
     */
    @Override
    protected void onStop()
    {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    public void gameOver()
    {
        final Toast toast = Toast.makeText(this, "Game Over. Tap anywhere to restart.", Toast.LENGTH_LONG);
        toast.show();
        mGameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                toast.cancel();
                GameActivity.this.recreate();
            }
        });
    }
}
