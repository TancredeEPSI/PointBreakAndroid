package fr.epsi.pointbreak.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import fr.epsi.pointbreak.R;
import fr.epsi.pointbreak.models.Tournament;

/**
 * Created by frus71323 on 23/01/2018.
 */

public class TournamentListRecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Tournament mTournament;
    private ImageView mTournamentImage;
    private  OnTournamentSelectListener mListener;

    public TournamentListRecyclerViewViewHolder(View itemView) {
        super(itemView);
        mTournamentImage = itemView.findViewById(R.id.activity_tournament_list_item_image_view_id);
    }

    public void update(Tournament tournament, final OnTournamentSelectListener listener) {
        mTournament = tournament;
        if (mTournament.getName().equals("Australian Open")){
            mTournamentImage.setImageResource(R.drawable.autralian_open_logo);
        } else if (mTournament.getName().equals("Roland Garros")){
            mTournamentImage.setImageResource(R.drawable.roland_garros_logo);
        } else if (mTournament.getName().equals("Wimbledon")){
            mTournamentImage.setImageResource(R.drawable.wimbledon_logo);
        } else if (mTournament.getName().equals("US Open")){
            mTournamentImage.setImageResource(R.drawable.us_open_logo);
        }
        mTournamentImage.setOnClickListener(this);
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        mListener.onTournamentSelected(mTournament);
    }

    public interface OnTournamentSelectListener {
        void onTournamentSelected(Tournament tournament);
    }
}
