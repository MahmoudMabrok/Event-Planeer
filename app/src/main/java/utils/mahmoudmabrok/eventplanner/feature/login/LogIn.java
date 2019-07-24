package utils.mahmoudmabrok.eventplanner.feature.login;

import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.mahmoudmabrok.eventplanner.R;

public class LogIn extends AppCompatActivity {

    @BindView(R.id.login_button)
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        
    }

    @OnClick(R.id.login_button)
    public void onViewClicked() {
    }
}
