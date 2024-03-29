package pe.gob.reniec.idaas.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import pe.gob.reniec.idaas.demo.common.Constants;
import pe.gob.reniec.idaas.sdk.ReniecIdaasClient;
import pe.gob.reniec.idaas.sdk.dto.User;

/**
 * Created by Miguel Pazo (http://miguelpazo.com)
 */
public class HomeActivity extends ParentActivity {

    private static final String TAG = "HomeActivity";
    private TextView tvDni = null, tvFirstName = null, tvPhone = null,
            tvPhoneVerified = null, tvEmail = null, tvEmailVerified = null, tvSub = null;
    private Button btnLogout = null;
    private WebView webView = null;

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
        btnLogout = (Button) findViewById(R.id.btn_logout);
        webView = (WebView) findViewById(R.id.webview_home);

        Intent intent = getIntent();

        Gson gson = new Gson();
        String userExtra = intent.getStringExtra(Constants.EXTRA_USER_INFO);
        User user = gson.fromJson(userExtra, User.class);

        tvDni.setText(user.getDoc());
        tvFirstName.setText(user.getFirstName());
        tvPhone.setText(user.getPhoneNumber());
        tvPhoneVerified.setText(user.getPhoneNumberVerified() != null ? user.getPhoneNumberVerified().toString() : "");
        tvEmail.setText(user.getEmail());
        tvEmailVerified.setText(user.getEmailVerified() != null ? user.getEmailVerified().toString() : "");
        tvSub.setText(user.getSub());

        final ReniecIdaasClient oClient = getIdaasClient();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(oClient.getLogoutUri(Constants.redirectUri));

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        return false;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

                        if (url.indexOf(Constants.redirectUri) == 0) {
                            Intent intentBack = new Intent(HomeActivity.this, MainActivity.class);
                            startActivity(intentBack);
                        }
                    }
                });
            }
        });
    }
}
