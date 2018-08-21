package pe.gob.reneic.pki.reniecidaasexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import pe.gob.reneic.pki.idaas.sdk.dto.User;
import pe.gob.reneic.pki.reniecidaasexample.common.Constants;

/**
 * Created by Miguel Pazo (http://miguelpazo.com)
 */
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private TextView tvDni, tvFirstName, tvPhone, tvPhoneVerified, tvEmail, tvEmailVerified, tvSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home_activity);

        tvDni = (TextView) findViewById(R.id.tv_dni);
        tvFirstName = (TextView) findViewById(R.id.tv_firstName);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvPhoneVerified = (TextView) findViewById(R.id.tv_phoneVerified);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvEmailVerified = (TextView) findViewById(R.id.tv_emailVerified);
        tvSub = (TextView) findViewById(R.id.tv_sub);

        Intent intent = getIntent();

        User user = intent.getParcelableExtra(Constants.EXTRA_USER_INFO);

        tvDni.setText(user.getDoc());
        tvFirstName.setText(user.getFirstName());
        tvPhone.setText(user.getPhoneNumber());
        tvPhoneVerified.setText(user.getPhoneNumberVerified().toString());
        tvEmail.setText(user.getEmail());
        tvEmailVerified.setText(user.getEmailVerified().toString());
        tvSub.setText(user.getSub());
    }
}
