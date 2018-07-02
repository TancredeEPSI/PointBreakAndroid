package fr.epsi.pointbreak.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import fr.epsi.pointbreak.R;
import fr.epsi.pointbreak.models.Match;

/**
 * Created by Tancrède on 16/05/2018.
 */

public class MatchListRecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Match mMatch;
    private OnMatchSelectListener mListener;
    private TextView tvTeam1;
    private TextView tvTeam2;
    private TextView textDate;

    public MatchListRecyclerViewViewHolder(View itemView) {
        super(itemView);
        tvTeam1 = itemView.findViewById(R.id.tvTeam1);
        tvTeam2 = itemView.findViewById(R.id.tvTeam2);
        textDate = itemView.findViewById(R.id.textDate);
    }

    public void update(Match match, final OnMatchSelectListener listener) {
        mMatch = match;
        this.itemView.setOnClickListener(this);
        mListener = listener;
        tvTeam1.setText(mMatch.playerOneName);
        tvTeam2.setText(mMatch.playerThreeName);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        textDate.setText(outputFormat.format(mMatch.dateMatch));
    }

    @Override
    public void onClick(View view) {
        mListener.onMatchSelected(mMatch);
    }

    public interface OnMatchSelectListener {
        void onMatchSelected(Match match);
    }
}
