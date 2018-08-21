package pe.gob.reneic.pki.idaas.sdk.interfaces;

import pe.gob.reneic.pki.idaas.sdk.dto.TokenResponse;

/**
 * Created by Miguel Pazo (http://miguelpazo.com)
 */
public interface ITokenResponse {

    void result(TokenResponse oTokenResponse);

    void error();

}
