package appl.innov.i_marchand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<TransactionModel> {

    private Context activityContext;
    private List<TransactionModel> list;
    public static final String TAG = "ListView";

    public ListAdapter(Context context, List<TransactionModel> list){
        super(context, R.layout.layout_list_view_item, list);
        this.activityContext = context;
        this.list = list;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup){

        final ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(activityContext).inflate(R.layout.layout_list_view_item, null);
            viewHolder = new ViewHolder();

            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.montant = (TextView) view.findViewById(R.id.montant);
            viewHolder.idTransact = (TextView) view.findViewById(R.id.idTransact);
            viewHolder.typeTransaction = (TextView) view.findViewById(R.id.typeTransaction);
            viewHolder.soldeNouveau = (TextView) view.findViewById(R.id.soldeNouveau);
            viewHolder.typeOperation = (TextView) view.findViewById(R.id.typeOperation);

            viewHolder.date.setText(list.get(position).getDate());
            viewHolder.montant.setText(list.get(position).getMontant());
            viewHolder.idTransact.setText(list.get(position).getIdTransaction());
            viewHolder.typeTransaction.setText(list.get(position).getTypeTransaction());
            viewHolder.soldeNouveau.setText(list.get(position).getSoldeNouveau());
            viewHolder.typeOperation.setText(list.get(position).getTypeOperation());

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

    private static class ViewHolder {

        TextView soldeNouveau;
        TextView typeOperation;
        TextView idTransact;
        TextView typeTransaction;
        TextView date;
        TextView montant;
    }

}
