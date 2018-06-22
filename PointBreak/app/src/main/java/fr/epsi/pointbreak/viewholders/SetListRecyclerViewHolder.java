package fr.epsi.pointbreak.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import fr.epsi.pointbreak.R;


public class SetListRecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView texteHaut;
    private TextView texteBas;

    public SetListRecyclerViewHolder(View itemView) {
        super(itemView);
        texteHaut = itemView.findViewById(R.id.textHaut);
        texteBas = itemView.findViewById(R.id.textBas);
    }

    public void update(String haut, String bas){
        texteHaut.setText(haut);
        texteBas.setText(bas);
    }

}
