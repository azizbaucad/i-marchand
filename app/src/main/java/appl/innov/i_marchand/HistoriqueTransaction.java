package appl.innov.i_marchand;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoriqueTransaction extends AppCompatActivity {
    TextView montantDetail,typeTransactionDetail,date,idTransaction,soldeNouveauDetail,typeOperationDetail;
    String montantStr,typeTransactionStr,dateStr,idTransactStr,soldeNouveauStr,typeOperationStr;
    //String devise = getResources().getString(R.string.currency);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historique_transaction);
        //String name = "Date de l'opération :" + "\n";
        montantDetail = findViewById(R.id.montantDetail);
        typeTransactionDetail = findViewById(R.id.typeTransactionDetail);
        date = findViewById(R.id.timestamp);
        idTransaction = findViewById(R.id.idTransaction);
        soldeNouveauDetail = findViewById(R.id.soldeNouveauDetail);
        typeOperationDetail = findViewById(R.id.typeOperationDetail);

        Intent intent = getIntent();
        montantStr = intent.getStringExtra("montantStr");
        typeTransactionStr = intent.getStringExtra("typeTransactionStr");
        dateStr = intent.getStringExtra("dateStr");
        idTransactStr = intent.getStringExtra("idTransactStr");
        soldeNouveauStr = intent.getStringExtra("soldeNouveauStr");
        typeOperationStr = intent.getStringExtra("typeOperationStr");
        //String text = "<b>" + name + "</b>" + "\n" + dateStr ;
        montantDetail.setText(montantStr);
        typeTransactionDetail.setText(typeTransactionStr);
        date.setText(dateStr);
        idTransaction.setText(idTransactStr);
        soldeNouveauDetail.setText(soldeNouveauStr+getResources().getString(R.string.currency));
        typeOperationDetail.setText(typeOperationStr);

        ImageView retour = (ImageView)findViewById(R.id.retourHistory);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HistoriqueTransaction.this, NFCTagActivity.class);
                startActivity(i);
            }
        });
    }
}