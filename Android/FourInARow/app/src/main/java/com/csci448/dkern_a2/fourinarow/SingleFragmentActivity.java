package com.csci448.dkern_a2.fourinarow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dkern on 2/8/17.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity
{
    /**
     * Creates fragment held by activity
     * @return fragment created
     */
    protected abstract Fragment createFragment();

    /**
     * Basic creation of Activity
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit(); }
    }
}
