package pe.gob.reniec.idaas.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.PrintWriter;
import java.io.StringWriter;

import pe.gob.reniec.idaas.demo.common.Constants;
import pe.gob.reniec.idaas.sdk.ReniecIdaasClient;
import pe.gob.reniec.idaas.sdk.dto.TokenResponse;
import pe.gob.reniec.idaas.sdk.dto.User;
import pe.gob.reniec.idaas.sdk.interfaces.ITokenResponse;
import pe.gob.reniec.idaas.sdk.interfaces.IUserinfoResponse;

public class MainActivity extends ParentActivity {

    private static final String TAG = "MainActivity";
    private Button btnLogin = null;
    private WebView webView = null;
    private String state = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.btn_login);
        webView = (WebView) findViewById(R.id.webview);

        final ReniecIdaasClient oClient = getIdaasClient();
        state = oClient.getState();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setDomStorageEnabled(true);
                webSettings.setLoadWithOverviewMode(true);
                webSettings.setUseWideViewPort(true);
                webSettings.setBuiltInZoomControls(true);
                webSettings.setDisplayZoomControls(false);
                webSettings.setSupportZoom(true);
                webSettings.setDefaultTextEncodingName("utf-8");

                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(oClient.getLoginUrl());

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        view.loadUrl(request.getUrl().toString());
                        return false;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        Log.d("===>", url);

                        if (url.indexOf(Constants.redirectUri) == 0) {
                            Uri uri = Uri.parse(url);
                            endpoint(uri);
                        }
                    }
                });
            }
        });
    }

    private void endpoint(Uri uri) {
        String error = uri.getQueryParameter("error");
        String stateResponse = uri.getQueryParameter("state");
        String code = uri.getQueryParameter("code");

        webView.setVisibility(View.INVISIBLE);

        try {
            if (error == null && code != null && stateResponse.equals(state)) {
                final ReniecIdaasClient oClient = getIdaasClient();

                oClient.getTokens(code, new ITokenResponse() {
                    @Override
                    public void result(TokenResponse oTokenResponse) {
                        oClient.getUserInfo(oTokenResponse.getAccessToken(), new IUserinfoResponse() {
                            @Override
                            public void result(User oUser) {
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.putExtra(Constants.EXTRA_USER_INFO, oUser);

                                startActivity(intent);
                            }

                            @Override
                            public void error() {
                                webView.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void error() {
                        webView.setVisibility(View.GONE);
                    }
                });
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));

            Log.e(TAG, ex.getMessage());
            Log.e(TAG, sw.toString());
        }
    }

}
