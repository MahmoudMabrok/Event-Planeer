package utils.mahmoudmabrok.eventplanner.feature.login;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.CalendarScopes;
import com.uk.tastytoasty.TastyToasty;

import java.util.Collections;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import utils.mahmoudmabrok.eventplanner.R;
import utils.mahmoudmabrok.eventplanner.feature.displayEvents.CalenderLoad;
import utils.mahmoudmabrok.eventplanner.feature.displayEvents.DisplayEvents;

public class LogIn extends AppCompatActivity {

    private static final String TAG = "LogIn";
    public static final String PREF_ACCOUNT_NAME = "accountName";
    private static final int REQUEST_ACCOUNT_PICKER = 1001;

    @BindView(R.id.login_button)
    LoginButton loginButton;

    private CallbackManager callBackManager;
    private GoogleAccountCredential credential;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // facebook login -> are suspended
        // setUpLogin();
        // checkLoginState();

        credential = GoogleAccountCredential.usingOAuth2(this,
                Collections.singleton(CalendarScopes.CALENDAR));

        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        if (settings.getString(PREF_ACCOUNT_NAME, null) == null) {
            chooseAccount();
            Log.d(TAG, "chooseAccount: ");
        } else {
            // already logged with Google Account
            opeDisplayEvents();
        }
    }


    /**
     * facebook login
     */
    private void setUpLogin() {
        callBackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: " + loginResult.getAccessToken());
                opeDisplayEvents();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: ");
            }
        });
    }

    private void opeDisplayEvents() {
        Intent openAcivity = new Intent(LogIn.this, CalenderLoad.class);
        startActivity(openAcivity);
        finish();
    }

    /**
     * login state for facebook
     */
    private void checkLoginState() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoged = accessToken != null && !accessToken.isExpired();
        if (isLoged) {
            opeDisplayEvents();
            Log.d(TAG, "checkLoginState: already logged");
        }
    }


    /**
     * Starts an activity in Google Play Services so the user can pick an
     * account.
     */
    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //   callBackManager.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                checkPicler(data, resultCode);
        }
    }

    private void checkPicler(Intent data, int resultCode) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                if (accountName != null) {
                    SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(PREF_ACCOUNT_NAME, accountName);
                    editor.apply();
                    opeDisplayEvents();
                }
            } else {
                reSelectAccount();
            }
        }
    }

    private void reSelectAccount() {
        TastyToasty.error(this, "Please select account").show();
        chooseAccount();
    }

}
