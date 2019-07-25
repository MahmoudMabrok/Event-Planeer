package utils.mahmoudmabrok.eventplanner.feature.login;

import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import utils.mahmoudmabrok.eventplanner.R;
import utils.mahmoudmabrok.eventplanner.feature.displayEvents.DisplayEvents;

public class LogIn extends AppCompatActivity {

    private static final String TAG = "LogIn";

    @BindView(R.id.login_button)
    LoginButton loginButton;

    private CallbackManager callBackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setUpLogin();

        checkLoginState();
    }


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
        Intent openAcivity = new Intent(LogIn.this, DisplayEvents.class);
        startActivity(openAcivity);
        finish();
    }

    private void checkLoginState() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoged = accessToken != null && !accessToken.isExpired();
        if (isLoged) {
            opeDisplayEvents();
            Log.d(TAG, "checkLoginState: already logged");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callBackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
