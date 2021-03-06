package dkern.csci448.com.locatr;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class LocatrActivity extends SingleFragmentActivity
{
    private static final int REQUEST_ERROR = 0;

    public static Intent newIntent(Context context) {
        return new Intent(context, LocatrActivity.class);
    }

    @Override
    protected Fragment createFragment()
    {
        return LocatrFragment.newInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if( errorCode != ConnectionResult.SUCCESS ) {
            Dialog errorDialog = apiAvailability
                    .getErrorDialog( this, errorCode, REQUEST_ERROR,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel( DialogInterface dialog ) {
                                    // Leave if services are unavailable
                                    finish(); }
                            });
            errorDialog.show();
        }
    }
}
