package fr.epsi.pointbreak.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.epsi.pointbreak.R;
import fr.epsi.pointbreak.models.Tournament;
import fr.epsi.pointbreak.viewholders.TournamentListRecyclerViewViewHolder;

/**
 * Created by frus71323 on 23/01/2018.
 */

public class TournamentsListRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Tournament> mTournamentsList = null;
    private TournamentListRecyclerViewViewHolder.OnTournamentSelectListener mListener = null;

    public TournamentsListRecyclerViewAdapter(ArrayList<Tournament> tournamentsList, TournamentListRecyclerViewViewHolder.OnTournamentSelectListener listener) {
        this.mTournamentsList = tournamentsList;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_tournaments_list_item, parent, false);
        return new TournamentListRecyclerViewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TournamentListRecyclerViewViewHolder) {
            ((TournamentListRecyclerViewViewHolder)holder).update(mTournamentsList.get(position), mListener);
        }
    }

    @Override
    public int getItemCount() {
         return mTournamentsList.size();
    }
}
