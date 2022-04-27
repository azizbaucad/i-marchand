package appl.innov.i_marchand;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import appl.innov.i_marchand.helper.DatabaseHelper;
//implements AdapterView.OnItemClickListener
public class ListTransaction extends AppCompatActivity {
    DatabaseHelper myDb; // DataBase
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter; // this is a comment this is a comment this is a comment this is a comment ok
    private String[] testValues;
    private DatabaseHelper databaseHelper;
    private SoapObject response,newList;
    ArrayAdapter<String> arrayAdapter ;
    //private time_ Timer;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transaction);
        /*** debut de l'import***/
        databaseHelper = new DatabaseHelper(this);
         timer = new Timer();
         timer.schedule(new TimerTask() {
             @Override
             public void run() {
                 getListTransaction();
                 newList = getListTransaction();
                 ArrayList<String> montantList = new ArrayList<String>();
                 ArrayList<String> dateList = new ArrayList<String>();
                 montantList.add("10");
                 montantList.add("20");
                 ArrayList<String> montantListFinal = null;
                 //newList0 = newList[]
                 Log.e("La nouvelle SoapObject nouvelle List retourné est","" + newList);
                 String error = newList.getProperty(0).toString();
                 String testValues = "" ;
                 String montantBrute = "";
                 String dateBrute = "";
                 //Log.e("Error nouvelle nouvelle est", "" + error);
                 for (int i=2; i<newList.getPropertyCount(); i++){
                     testValues = newList.getProperty(i).toString().replace("anyType", "");
                     String[] stringArray = testValues.split(";");
                     String montant = stringArray[2];
                     String[] montantArray = montant.split("=");
                     montantBrute = montantArray[1];
                     Log.e("MONTANT BRUTE BRUTE BRUTE",""+montantBrute);
                     montantList.add(montantBrute);

                     String date = stringArray[0];
                     String[] dateArray = date.split("=");
                     dateBrute = dateArray[1];
                     Log.e("DATE BRUTE BRUTE BRUTE",""+dateBrute);
                     dateList.add(dateBrute);
                     //Log.e("Montant Liste", "" + montantList);
                     // Log.i("MONTANT LISTE BRUTE", " " + montantList.add(montantBrute));
                 }
                 Log.e("Montant Liste Liste Liste ...", "" + montantList);
                 Log.e("Montant Liste Liste Liste ...", "" + dateList);

                 //newList0 = newList[]
                 Log.e("La NEWWWWWWWWWWWWWWWW..................; SoapObject nouvelle List retourné est","" + newList);

                 //getMontant();
                 Log.e("LA BDDDDDDDDDDDDDD","0");

                 listView = findViewById(R.id.list_view);
                 String[] languages = {"Transfer", "Retrait", "JavaScript", "PHP"};
                 Log.e(" La liste est----bla bla bla--------------------------------------", "" + montantList);
                 arrayAdapter = new ArrayAdapter<String>(
                         ListTransaction.this,
                         R.layout.layout_list_view_item,
                         montantList);
                 runOnUiThread(new Runnable() {

                     @Override
                     public void run() {
                         listView.setAdapter(arrayAdapter);
                     }
                 });
             }
         },0,3000);
        /*** Fin de l'import***/
    }

    public SoapObject getListTransaction(){
        //Cursor res = myDb.getAllData();
        Cursor res = databaseHelper.getAllData();
        List<String> listSimple = null;
        if(res.moveToLast()){
            // http://50.116.97.25:8080/cash-ws/CashWalletServiceWS?wsdl // server de test
            // String URL = "http://ibusinesscompanies.com:18080/cash-ws/CashWalletServiceWS?wsdl"; // serveur de prod
            String URL = "http://50.116.97.25:8080/cash-ws/CashWalletServiceWS?wsdl"; // serveur de test
            String NAMESPACE = "http://runtime.services.cash.innov.sn/";
            String SOAP_ACTION = "";
            String METHOD_NAME = "listOperationHistoriqueTransaction";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            String idSession_;
            String telephone;
            PropertyInfo tel = new PropertyInfo();
            tel.setName("telephone");
            telephone = res.getString(4);
            tel.setValue(telephone);
            tel.setType(String.class);
            request.addProperty(tel);
            PropertyInfo session = new PropertyInfo();
            session.setName("sessionId");
            idSession_ = res.getString(1);
            session.setValue(idSession_);
            session.setType(String.class);
            request.addProperty(session);
            Log.e(" Resultat ", " : " + "WS" + session + " " + "BDD" + " " + idSession_);
            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                //envelope.dotNet=true;
                //envelope.bodyOut = request;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);
                //SoapObject response = (SoapObject) envelope.bodyIn;
                response = (SoapObject) envelope.getResponse();
                listSimple = new ArrayList<String>();
                Log.e("Le Bon resultat ", " " + response);
                String error = response.getProperty(0).toString();
                Log.e(" Erreur ", "" + error);
                if (error.equals("0")) {
                    //String[] testValues;
                    String testValues;
                    String output = "";
                    //testValues = new String[response.getPropertyCount()];
                    //testValues = new response.getPropertyCount();
                    Log.e(" REPONSE ", "La reponse est bonne");
                    for (int i=2; i<response.getPropertyCount(); i++){
                        testValues = response.getProperty(i).toString();
                        //testValues = response.getPropertyAsString(i).toString().replace();
                        listSimple.add(response.getPropertyAsString(i).toString().replace("anyType", ""));
                        Log.e( " REPONSE OK.... ", "" + testValues);
                        Log.e("La Liste Simple est ", "-------"+listSimple);
                        String[] stringArray = testValues.split(";");
                        // recupérer le montant
                        String montant = stringArray[2];
                        String[] montantArray = montant.split("=");
                        String montantBrute = montantArray[1];
                        Log.e("MONTANT BRUTE", " " + montantBrute);
                        //String[] stringArray = testValues.split(";");
                        // recupérer la date
                        String date = stringArray[0];
                        String[] dateArray = date.split("=");
                        String dateBrute = dateArray[1];
                        Log.e("Date ", " " + dateBrute);
                        // recupérer le type
                        String type_ = stringArray[4];
                        String[] typeArray = type_.split("=");
                        String typeBrute = typeArray[1];
                        Log.e("Type Debit ", " " + typeBrute);
                        //for(int j=0; j<testValues)
                        //output += "date : " + testValues[2][0]
//                        String[] stringArray = testValues[i].split(";");
//                        String montant__ = stringArray[2];
//                        String[] montantArray = montant__.split("=");
//                        String montantBrute = montantArray[1];
//                        Log.e( " MONTANT ...", "" + montant__);
//                        Log.e( " MONTANT BRUTE ...", "" + montantBrute);
                        //Log.e(" LISTE ...", " " + testValues[i]);
                    }
                    Log.e("La Liste Simple est ", "-------"+listSimple);
                    /*String operations = response.getProperty(0).toString();
                    String[] stringArray = operations.split(";");
                    String montant_ = stringArray[2];
                    Log.e(" RESPONSE String Array ", " " + montant_);*/
                    //sLog.e(" REPONSE ", " " + operations);
                } else Log.e( " REPONSE", "Mauvaise Reponse");
                //String[] response = result.getProperty(0).toString();
                //Log.e(" GooD response  ", " : " + " " + response);
                /*for (int i=0; i<response.getPropertyCount();i++){
                    PropertyInfo pi = new PropertyInfo();
                    response.getPropertyInfo(i,pi);
                    Object property = response.getProperty(i);
                    //Log.e()
                    //if (pi.name.equals("operations") && property instanceof SoapObject) {
                        SoapObject transDetail = (SoapObject) property;
                        String transDate = transDetail.getPrimitivePropertyAsString("date");
                        String transMontant = transDetail.getPrimitivePropertyAsString("montant");
                        Log.e("Res","Date Trans : " + transDate );
                        Log.e("Res", "Montant Trans : " + transMontant );
                    //}
                }*/


//                if (err.equals("0")) {
//
//                    Log.e("result", " ok.............. ");
//                }
                //String[] testValues = new String[result.getPropertyCount()];
                /*for (int i=0; i < result.getPropertyCount(); i++){
                    testValues[0] = result.getProperty(0).toString();
                    Log.e(" Resulta Test Value ", " :" + " " + testValues[0]);
//                    String erreur = result.getProperty(testValues[0]).toString();
//                    String[] stringArray = erreur.split(";");
//                    String resultat = stringArray[0];
//                    Log.e(" Resultat du String Array " , " :" + " " + resultat);
                }*/
                /*if (testValues[0].equals("0")) {
                    String accounts = testValues[0][0].toString();
                }*/

            } catch (Exception e) {
                Log.e(" Resultat ", "Ne marche pas ...");
            }


            // add parameters
            /*String telephone;
            PropertyInfo tel = new PropertyInfo();
            tel.setName("telephone");
            telephone = res.getString(4);
            tel.setValue(telephone);
            tel.setType(String.class);
            request.addProperty(tel);

            String idSession_;
            //set the idSession retrieved
            PropertyInfo session =new PropertyInfo();
            session.setName("sessionId");
            idSession_ =  res.getString(1);
            session.setValue(idSession_);
            session.setType(String.class);
            request.addProperty(session);

            Log.e("result", "La nouvelle session  dans la BDD est de " + idSession_);
            Log.e("result", "La  nouvelle session dans le WS est de " + session);
            //Log.e("result", "id session avant entrée dans le try" + idSession_);
            Log.e("result", " Telephone " + telephone);
            try {
                Log.e("resultat", "ID Sessions après entrere dans le try " + idSession_);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapObject result = (SoapObject) envelope.getResponse();
                //SoapObject result = (SoapObject) envelope.bodyIn;
                testValues = new String[result.getPropertyCount()];
                for (int i=0; i < result.getPropertyCount(); i++){
                    testValues[0] = result.getProperty(0).toString();
                    //Log.e(" Test ", " : "+ testValues[0]);
                }
                Log.e(" Test ", " : "+ testValues[0]);
                //Log.e(" resultat ", " resultat " + result);
                //String error = result.getProperty(0).toString();
                //String message = result.getProperty(1).toString();
                //Log.e("Error ", "Error " + error);
              *//* if (error.equals("0")){
                    Log.e("getlistTransaction", "getlistTransaction Called with success");
                    Log.e("result", "responceEEEEEEEEEEEEEEEEEEEEEEEE" + result);
                    runOnUiThread(() -> {

                    });
                }else {
                    Log.e("ERROR", "getlistTransaction NOT CALLED WITH SUCCESS");
                }*//*
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }*/
        }
        return response;
    }
}
