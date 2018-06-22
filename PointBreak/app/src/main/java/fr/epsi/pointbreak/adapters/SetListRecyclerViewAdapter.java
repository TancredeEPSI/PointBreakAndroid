package fr.epsi.pointbreak.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.epsi.pointbreak.R;
import fr.epsi.pointbreak.viewholders.SetListRecyclerViewHolder;

/**
 * Created by Tancrède on 11/06/2018.
 */

public class SetListRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<List<String>> score;

    public SetListRecyclerViewAdapter(List<List<String>> score){
        this.score = score;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_match_list_set_item, parent, false);
        return new SetListRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SetListRecyclerViewHolder) {
            ((SetListRecyclerViewHolder)holder).update(score.get(position).get(0), score.get(position).get(1));
        }
    }

    @Override
    public int getItemCount() {
        return score.size();
    }
}
