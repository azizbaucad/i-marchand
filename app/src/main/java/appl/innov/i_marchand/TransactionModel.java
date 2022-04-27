package appl.innov.i_marchand;

public class TransactionModel {

    String date;
    String montant;
    String typeTransaction;
    String idTransaction;
    String soldeNouveau;
    String typeOperation;

    public TransactionModel(String date, String montant, String typeTransaction, String idTransaction, String soldeNouveau, String typeOperation) {
        this.date = date;
        this.montant = montant;
        this.typeTransaction = typeTransaction;
        this.idTransaction = idTransaction;
        this.soldeNouveau = soldeNouveau;
        this.typeOperation = typeOperation;
    }

    public String getSoldeNouveau() {
        return soldeNouveau;
    }

    public void setSoldeNouveau(String soldeNouveau) {
        this.soldeNouveau = soldeNouveau;
    }

    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
