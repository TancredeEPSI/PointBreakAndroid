package fr.epsi.pointbreak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import fr.epsi.pointbreak.R;
import fr.epsi.pointbreak.adapters.TournamentsListRecyclerViewAdapter;
import fr.epsi.pointbreak.dataRequest.DataRequest;
import fr.epsi.pointbreak.models.Tournament;
import fr.epsi.pointbreak.viewholders.TournamentListRecyclerViewViewHolder;

public class TournamentsListActivity extends AppCompatActivity implements TournamentListRecyclerViewViewHolder.OnTournamentSelectListener, DataRequest.TournamentInterface {

    private ArrayList<Tournament> tournamentsArrayList = new ArrayList<>();

    RecyclerView mTournamentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournaments_list);

        //Set support to custom toolbar
        Toolbar mToolbar = findViewById(R.id.toolbar_app);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Available Tournaments");
        }

        DataRequest.getInstance(this).getTournament(this);

        mTournamentsList = findViewById(R.id.tournaments_list_recycler_view_id);
        // Set the layout for the tournament list
        mTournamentsList.setLayoutManager(new LinearLayoutManager(this));
        TournamentsListRecyclerViewAdapter mTournamentsListAdpater = new TournamentsListRecyclerViewAdapter(tournamentsArrayList, this);
        mTournamentsList.setAdapter(mTournamentsListAdpater);
    }

    @Override
    public void onTournamentSelected(Tournament tournament) {
        Intent matchsListActivity = new Intent(this, MatchsListActivity.class);
        matchsListActivity.putExtra("TOURNAMENT_ID", tournament.getId());
        matchsListActivity.putExtra("TOURNAMENT_NAME", tournament.getName());
        DataRequest.getInstance().tournamentName = tournament.getName();
        startActivity(matchsListActivity);
    }

    @Override
    public void onTournamentReceived(List<Tournament> tournaments) {
        tournamentsArrayList.clear();
        tournamentsArrayList.addAll(tournaments);
        TournamentsListRecyclerViewAdapter mTournamentsListAdpater = new TournamentsListRecyclerViewAdapter(tournamentsArrayList, this);
        mTournamentsList.setAdapter(mTournamentsListAdpater);

    }

    @Override
    public void errorLoadingTournaments() {

    }
}
