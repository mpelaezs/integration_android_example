package pe.gob.reniec.idaas.sdk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * @author Miguel Pazo (http://miguelpazo.com)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    @JsonProperty("issuer")
    private String issuer;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("auth_uri")
    private String authUri;
    @JsonProperty("federation_id")
    private String federationId;
    @JsonProperty("token_uri")
    private String tokenUri;
    @JsonProperty("userinfo_uri")
    private String userInfoUri;
    @JsonProperty("logout_uri")
    private String logoutUri;
    @JsonProperty("auth_provider_keys_uri")
    private String keysUri;
    @JsonProperty("redirect_uris")
    private String[] redirectUris;
    @JsonProperty("javascript_origins")
    private String[] originUris;

    public Config() {
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAuthUri() {
        return authUri;
    }

    public void setAuthUri(String authUri) {
        this.authUri = authUri;
    }

    public String getFederationId() {
        return federationId;
    }

    public void setFederationId(String federationId) {
        this.federationId = federationId;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }

    public String getLogoutUri() {
        return logoutUri;
    }

    public void setLogoutUri(String logoutUri) {
        this.logoutUri = logoutUri;
    }

    public String getKeysUri() {
        return keysUri;
    }

    public void setKeysUri(String keysUri) {
        this.keysUri = keysUri;
    }

    public String[] getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String[] redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String[] getOriginUris() {
        return originUris;
    }

    public void setOriginUris(String[] originUris) {
        this.originUris = originUris;
    }

    @Override
    public String toString() {
        return "Config{" +
                "issuer='" + issuer + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", authUri='" + authUri + '\'' +
                ", federationId='" + federationId + '\'' +
                ", tokenUri='" + tokenUri + '\'' +
                ", userInfoUri='" + userInfoUri + '\'' +
                ", logoutUri='" + logoutUri + '\'' +
                ", keysUri='" + keysUri + '\'' +
                ", redirectUris=" + Arrays.toString(redirectUris) +
                ", originUris=" + Arrays.toString(originUris) +
                '}';
    }
}