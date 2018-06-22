package fr.epsi.pointbreak.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.epsi.pointbreak.R;
import fr.epsi.pointbreak.models.Match;
import fr.epsi.pointbreak.viewholders.MatchListRecyclerViewViewHolder;

/**
 * Created by Tancrède on 16/05/2018.
 */

public class MatchListRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Match> mMatches = null;
    private MatchListRecyclerViewViewHolder.OnMatchSelectListener mListener = null;

    public MatchListRecyclerViewAdapter(List<Match> matches, MatchListRecyclerViewViewHolder.OnMatchSelectListener listener) {
        this.mMatches = matches;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_matchs_list_item, parent, false);
        return new MatchListRecyclerViewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MatchListRecyclerViewViewHolder) {
            ((MatchListRecyclerViewViewHolder)holder).update(mMatches.get(position), mListener);
        }
    }

    @Override
    public int getItemCount() {
        return mMatches.size();
    }
}
