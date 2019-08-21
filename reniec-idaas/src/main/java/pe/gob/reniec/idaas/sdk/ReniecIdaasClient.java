package pe.gob.reniec.idaas.sdk;

import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import pe.gob.reniec.idaas.sdk.dto.Config;
import pe.gob.reniec.idaas.sdk.dto.IdToken;
import pe.gob.reniec.idaas.sdk.dto.TokenResponse;
import pe.gob.reniec.idaas.sdk.dto.User;
import pe.gob.reniec.idaas.sdk.enums.Acr;
import pe.gob.reniec.idaas.sdk.enums.Prompt;
import pe.gob.reniec.idaas.sdk.enums.Scope;
import pe.gob.reniec.idaas.sdk.interfaces.ITokenResponse;
import pe.gob.reniec.idaas.sdk.interfaces.IUserinfoResponse;
import pe.gob.reniec.idaas.sdk.utils.UrlQueryString;

/**
 * Created by Miguel Pazo (http://miguelpazo.com)
 */
public class ReniecIdaasClient {

    private static final String TAG = "ReniecIdaasClient";

    private String redirectUri = null;
    private List<Scope> lstScopes = new ArrayList<>();
    private Acr acr = Acr.ONE_FACTOR;
    private Prompt prompt = null;
    private Integer maxAge = null;
    private String state = null;
    private String loginHint = null;
    private Config config;
    private AsyncHttpClient client = new AsyncHttpClient();

    public ReniecIdaasClient(String configFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.readValue(new File(configFile), Config.class);
    }

    public ReniecIdaasClient(InputStream data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.readValue(data, Config.class);
    }

    public String getLoginUrl() {
        String paramScope = "openid";

        LinkedHashMap<String, Object> query = new LinkedHashMap<>();

        query.put("acr_values", this.acr.getValue());
        query.put("client_id", this.config.getClientId());
        query.put("response_type", "code");
        query.put("redirect_uri", this.redirectUri);

        if (this.prompt != null) {
            query.put("prompt", this.prompt.getValue());
        }

        if (this.state != null) {
            query.put("state", this.state);
        }

        if (this.maxAge != null) {
            query.put("max_age", this.maxAge);
        }

        if (this.loginHint != null) {
            query.put("login_hint", this.loginHint);
        }

        //lstScopes
        for (Scope scope : this.lstScopes) {
            paramScope += " " + scope.getValue();
        }

        query.put("scope", paramScope);

        return this.config.getAuthUri() + "?" + UrlQueryString.getInstance().buildQuery(query);
    }

    public void getTokens(final String code, final ITokenResponse iTokenResponse) {
        RequestParams params = new RequestParams();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("client_id", config.getClientId());
        params.add("client_secret", config.getClientSecret());

        client.post(config.getTokenUri(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (statusCode == 200) {
                        ObjectMapper m = new ObjectMapper();
                        TokenResponse tokenResponse = m.readValue(response.toString(), TokenResponse.class);

                        IdToken idToken = new IdToken();

                        try {
                            String[] parts = tokenResponse.getIdToken().split("\\.");
                            ObjectMapper objectMapper = new ObjectMapper();
                            idToken = objectMapper.readValue(new String(Base64.decode(parts[1], Base64.DEFAULT), "UTF-8"), IdToken.class);
                        } catch (Exception ex) {
                            StringWriter sw = new StringWriter();
                            ex.printStackTrace(new PrintWriter(sw));

                            Log.e(TAG, "Error parsing id_token ".concat(ex.getMessage()));
                            Log.e(TAG, sw.toString());
                        }

                        tokenResponse.setIdTokenObject(idToken);
                        iTokenResponse.result(tokenResponse);
                    } else {
                        iTokenResponse.error();
                    }
                } catch (Exception ex) {
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));

                    Log.e(TAG, ex.getMessage());
                    Log.e(TAG, sw.toString());

                    iTokenResponse.error();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject response) {
                iTokenResponse.error();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String message, Throwable t) {
                iTokenResponse.error();
            }
        });
    }

    public void getUserInfo(final String accessToken, final IUserinfoResponse iUserinfoResponse) {
        client.addHeader("Authorization", "Bearer ".concat(accessToken));

        client.post(config.getUserInfoUri(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (statusCode == 200) {
                        ObjectMapper m = new ObjectMapper();
                        User userResponse = m.readValue(response.toString(), User.class);

                        iUserinfoResponse.result(userResponse);
                    } else {
                        iUserinfoResponse.error();
                    }
                } catch (Exception ex) {
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));

                    Log.e(TAG, ex.getMessage());
                    Log.e(TAG, sw.toString());

                    iUserinfoResponse.error();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject response) {
                iUserinfoResponse.error();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String message, Throwable t) {
                iUserinfoResponse.error();
            }
        });
    }


    public String getLogoutUri(String redirectPostLogout) {
        LinkedHashMap<String, Object> query = new LinkedHashMap<>();

        query.put("post_logout_redirect_uri", redirectPostLogout);

        return this.config.getLogoutUri() + "?" + UrlQueryString.getInstance().buildQuery(query);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setAcr(Acr acr) {
        this.acr = acr;
    }

    public void setPrompt(Prompt prompt) {
        this.prompt = prompt;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public void addScope(Scope scope) {
        this.lstScopes.add(scope);
    }

    public void cleanScopes() {
        this.lstScopes.clear();
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public Config getConfig() {
        return config;
    }

    public void setLoginHint(String loginHint) {
        this.loginHint = loginHint;
    }
}
