package pe.gob.reniec.idaas.sdk.interfaces;

import pe.gob.reniec.idaas.sdk.dto.TokenResponse;

/**
 * Created by Miguel Pazo (http://miguelpazo.com)
 */
public interface ITokenResponse {

    void result(TokenResponse oTokenResponse);

    void error();

}
