package appl.innov.i_marchand;

import static org.apache.http.conn.ssl.SSLSocketFactory.SSL;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import appl.innov.i_marchand.helper.DatabaseHelper;
import at.markushi.ui.CircleButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    private final BroadcastReceiver MyReceiver = null;

    DatabaseHelper myDb;
    private SQLiteDatabase sql;

    private EditText loginEdit;
    private EditText passwordText;
    String login;
    String password;
    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDb = new DatabaseHelper(this);
        Button btn_con = findViewById(R.id.loginBtn);
        loginEdit = findViewById(R.id.emailOubTxt);
        passwordText = findViewById(R.id.passworOubTxt);

        //ImageView deconnecte = (ImageView) findViewById(R.id.logout);

        btn_con.setOnClickListener(v -> {
            if (loginEdit.getText().length() != 0 && !loginEdit.getText().toString().equals("")) {
                if (passwordText.getText().length() != 0 && !passwordText.getText().toString().equals("")) {
                    broadcastIntent();
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                }  //If Password text control is empty
                else {
                    passwordText.setError("Entrez le mot de passe");
                }
            } else {
                passwordText.setError("Entrez le login");
            }
        });
     /*  btn_con.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(getApplicationContext(), NFCTagActivity.class);
               startActivity(i);
           }
       });*/

    }

    private void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
/*
        unregisterReceiver(MyReceiver);
*/
    }

    @SuppressLint("StaticFieldLeak")
    class AsyncCallWSDeconnexion extends AsyncTask<String, Void, String> {
        private final ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected String doInBackground(String... url) {
            deconnexionUser();
            return null;
        }

        @Override
        protected void onPreExecute() {
            SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#651812"));
            pDialog.setTitleText("Chargement...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            Dialog.dismiss();

        }
    }

    @SuppressLint("StaticFieldLeak")
    class AsyncCallWS extends AsyncTask<String, Void, String> {

        SweetAlertDialog pDialog =
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected String doInBackground(String... url) {
            loginAction();
            return null;
        }

        @Override
        protected void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF3D00"));
            pDialog.setTitleText("Chargement...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
        }
    }


    TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
    };


    private void loginAction() {
        // http://50.116.97.25:8080/cash-ws/CashWalletServiceWS?wsdl // server de test
        // String URL = "http://ibusinesscompanies.com:18080/cash-ws/CashWalletServiceWS?wsdl"; // serveur de prod
        String URL = "http://50.116.97.25:8080/cash-ws/CashWalletServiceWS?wsdl"; // serveur de test
        String NAMESPACE = "http://runtime.services.cash.innov.sn/";
        String SOAP_ACTION = "";
        String METHOD_NAME = "login";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        login = loginEdit.getText().toString();
        password = passwordText.getText().toString();

        //Pass value for userName variable of the web service
        PropertyInfo unameProp = new PropertyInfo();
        unameProp.setName("login");//Define the variable name in the web service method
        unameProp.setValue(login);//set value for userName variable
        unameProp.setType(String.class);//Define the type of the variable
        request.addProperty(unameProp);//Pass properties to the variable

        //Pass value for Password variable of the web service
        PropertyInfo passwordProp = new PropertyInfo();
        passwordProp.setName("password");
        passwordProp.setValue(password);
        passwordProp.setType(String.class);
        request.addProperty(passwordProp);

        //Pass value for Password variable of the web service
        String mod = "APP";
        PropertyInfo mode = new PropertyInfo();
        mode.setName("mode");
        mode.setValue(mod);
        mode.setType(String.class);
        request.addProperty(mode);
        try {
            SSLContext sc = SSLContext.getInstance(SSL);
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = false;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Log.d("login", login);
            Log.d("password", password);
            Log.d("request", request.toString());

            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject result = (SoapObject) envelope.getResponse();

            System.out.println("call Donnnnnnnnnnnnnnnnnnnnnnnnnnne");
            String erreur = result.getProperty(0).toString();
            String iduser = result.getProperty(1).toString();

            Log.e("result", "responceEEEEEEEEEEEEEEEEEEEEEEEE" + result);
            Log.e("XXenvelope", "envelope" + envelope.toString());

            Log.e("token", "tokennnn" + token);

            Cursor res = myDb.getAllData();
            if (erreur.equals("0")) {
                String message1 = result.getProperty(2).toString();
                String nom = result.getProperty(3).toString();
                String prenom = result.getProperty(4).toString();
                String profil = result.getProperty(5).toString();
                String telephone = result.getProperty(6).toString();
                token = result.getProperty(7).toString();
                Log.e("token", "tokennnn" + token);
                Log.e("result", "Message1 " + message1);

                boolean isInserted = myDb.insertData(
                        token, null, null, telephone,
                        iduser, null, null, null, null
                );

                if (isInserted) {
                    Log.e("result", "donnéeeeeeeeeeeeees inséréééééééés" + token);
                    Log.e("result", "iduser from login function" + iduser);

                    runOnUiThread(() -> {
                        Intent i = new Intent(getApplicationContext(), NFCTagActivity.class);
                        startActivity(i);
                    });
                }
            } else if (erreur.equals("13")) {
                runOnUiThread(this::OnUserSession);
            } else if (erreur.equals("10")) {
                runOnUiThread(this::changePasswordDialog);
            } else if (erreur.equals("11")) {
                runOnUiThread(this::changeOTPDialog);
            } else {
                runOnUiThread(this::ErrorLogin);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Fin de la fonction LoginAction

    public void deconnexionSucces() {

    }


    public void deconnexionUser() {
        Cursor res = myDb.getAllData();
        if (res.moveToLast()) {
            // http://50.116.97.25:8080/cash-ws/CashWalletServiceWS?wsdl // server de test
            // String URL = "http://ibusinesscompanies.com:18080/cash-ws/CashWalletServiceWS?wsdl"; // serveur de prod
            String URL = "http://50.116.97.25:8080/cash-ws/CashWalletServiceWS?wsdl"; // serveur de test
            String NAMESPACE = "http://runtime.services.cash.innov.sn/";
            String SOAP_ACTION = "";
            String METHOD_NAME = "deconnexionUser";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            String idSession_;
            //set the idSession retrieved
            PropertyInfo session = new PropertyInfo();
            session.setName("idSession");
            idSession_ = res.getString(1);
            session.setValue(idSession_);
            session.setType(String.class);
            request.addProperty(session);
            Log.e("result", "idddddd sessssssion avant appel de deconnexionUser" + idSession_);

            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapObject result = (SoapObject) envelope.getResponse();
                String error = result.getProperty(1).toString();

                Log.e("result", "responceeeeeeee from ws deconnexionUserrrrrr" + result);
                if (error.equals("1")) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            deconnexionSucces();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*  public void renvoieOTP(){
          SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

      }*/
    public void youAreNotCostumer() {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle("Alerte");
        alertDialog.setIcon(R.drawable.ic_cross);
        alertDialog.setMessage("Vous n'avez pas de compte marchand!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void OnUserSession() {

        // Creation de ALerte Dialog pour confirmation
        SweetAlertDialog confirmDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
        //confirmDialog.setTitleText("Are you sure? ");
        confirmDialog.setContentText("Vous ètes déjàs connecté sur i-pay \n Voulez-vous poursuivre la connexion ?");
        confirmDialog.setConfirmText("Oui");
        // creation du bouton oui
        confirmDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog oui) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(), NFCTagActivity.class);
                        startActivity(i);
                    }
                });

            }
        });

        confirmDialog.show();
        // fin de la cration du bouton oui*
        // creation du bouton non
        confirmDialog.setCancelButton("Non", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog nonDialog) {

                //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                //startActivity(intent);
                nonDialog.dismiss();
                finish(); // Tuer cette activité permmettrant de quitter la page de connexion.

                // nonDialog.dismissWithAnimation();

            }
        });

    }

    public void ErrorLogin() {

        // Mettre le Sweet Alert Dialog Error Message
        SweetAlertDialog sDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
        //sDialog.setTitleText("");
        sDialog.setContentText("Téléphone ou Mot de passe Incorrect !");
       // sDialog.setCustomImage(R.drawable.error_center_x);
        sDialog.setConfirmText("OK");
        sDialog.showCancelButton(false);
        sDialog.show();

        /*AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle("Alerte");
        alertDialog.setIcon(R.drawable.ic_cross);
        alertDialog.setMessage("identifiants incorrects");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();*/
    }

    public void outOfSession() {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle("Alerte");
        alertDialog.setIcon(R.drawable.ic_alert);
        alertDialog.setMessage("Votre session a expiré ! Veuillez vous reconnecter !");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void changePasswordDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle("Alerte");
        alertDialog.setIcon(R.drawable.ic_alert);
        alertDialog.setMessage("Veuillez changer votre mot de passe !");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void changeOTPDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle("Alerte");
        alertDialog.setIcon(R.drawable.ic_alert);
        alertDialog.setMessage("Merci d'aller sur i-pay.africa pour vérifier votre mail et/ou OTP.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


}
