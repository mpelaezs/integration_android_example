package pe.gob.reneic.pki.reniecidaasexample;

import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import pe.gob.reneic.pki.idaas.sdk.ReniecIdaasClient;
import pe.gob.reneic.pki.idaas.sdk.enums.Acr;
import pe.gob.reneic.pki.idaas.sdk.enums.Scope;
import pe.gob.reneic.pki.reniecidaasexample.common.Constants;

/**
 * Created by Miguel Pazo (http://miguelpazo.com)
 */
public class ParentActivity extends AppCompatActivity {

    private static final String TAG = "ParentActivity";

    protected ReniecIdaasClient getIdaasClient() {
        try {
            InputStream file = getResources().openRawResource(getResources().getIdentifier("reniec_idaas", "raw", getPackageName()));
            ReniecIdaasClient oClient = new ReniecIdaasClient(file);

            String state = Base64.encodeToString(String.valueOf(System.currentTimeMillis()).getBytes(), Base64.NO_WRAP);

            oClient.setRedirectUri(Constants.redirectUri);
            oClient.setAcr(Acr.ONE_FACTOR);
            oClient.addScope(Scope.PROFILE);
            oClient.addScope(Scope.EMAIL);
            oClient.addScope(Scope.PHONE);
            oClient.setState(state);

            return oClient;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));

            Log.e(TAG, ex.getMessage());
            Log.e(TAG, sw.toString());
        }

        return null;
    }

}
