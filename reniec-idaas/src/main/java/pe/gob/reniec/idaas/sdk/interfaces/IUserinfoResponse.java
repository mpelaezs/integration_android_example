package pe.gob.reniec.idaas.sdk.interfaces;

import pe.gob.reniec.idaas.sdk.dto.User;

/**
 * Created by Miguel Pazo (http://miguelpazo.com)
 */
public interface IUserinfoResponse {

    void result(User oUser);

    void error();

}
