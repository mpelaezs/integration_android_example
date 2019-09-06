package pe.gob.reniec.idaas.demo;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

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
                        String url = request.getUrl().toString();

                        if (url.startsWith("intent://")) {
                            Uri uriParsed = Uri.parse(url);
                            String uriFragment = uriParsed.getFragment();

                            String appPackage = getPart(uriFragment, "package=", ";");
                            String name = getPart(uriFragment, "S.name=", ";S.code=");
                            String code = getPart(uriFragment, "S.code=", ";end");

                            Intent intent = new Intent();
                            intent.setAction(appPackage.concat(".shared"));
                            intent.setType("text/plain");
                            intent.putExtra(Constants.EXTRA_CODE, code);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            PackageManager packageManager = getPackageManager();
                            List activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

                            if (activities.size() > 0) {
                                getBaseContext().startActivity(intent);
                            } else {
                                Toast.makeText(getBaseContext(), "Debes instalar la app ".concat(name).concat("."), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=".concat(appPackage))));
                            }

                            return true;
                        }

                        return false;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

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
                                Gson gson = new Gson();
                                intent.putExtra(Constants.EXTRA_USER_INFO, gson.toJson(oUser));

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

    private String getPart(String data, String begin, String end) {
        String result = data.substring(data.indexOf(begin) + begin.length());
        result = result.substring(0, result.indexOf(end));

        return result;
    }
}
