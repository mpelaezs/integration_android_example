package pe.gob.reneic.pki.idaas.sdk.interfaces;

import pe.gob.reneic.pki.idaas.sdk.dto.User;

/**
 * Created by Miguel Pazo (http://miguelpazo.com)
 */
public interface IUserinfoResponse {

    void result(User oUser);

    void error();

}
