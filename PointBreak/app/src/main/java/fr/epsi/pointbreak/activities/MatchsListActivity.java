package fr.epsi.pointbreak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.epsi.pointbreak.R;
import fr.epsi.pointbreak.adapters.MatchListRecyclerViewAdapter;
import fr.epsi.pointbreak.dataRequest.DataRequest;
import fr.epsi.pointbreak.models.Match;
import fr.epsi.pointbreak.viewholders.MatchListRecyclerViewViewHolder;

public class MatchsListActivity extends AppCompatActivity implements DataRequest.MatchInterface, MatchListRecyclerViewViewHolder.OnMatchSelectListener {

    private ImageView imageMatch;
    private int idTournament;
    private List<Match> matches = new ArrayList<>();
    private RecyclerView matchListRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchs_list);

        final Intent intent = getIntent();
        String mTournamentName = intent.getStringExtra("TOURNAMENT_NAME");
        idTournament = intent.getIntExtra("TOURNAMENT_ID", 0);

        //Set support to custom toolbar
        Toolbar mToolbar = findViewById(R.id.toolbar_app);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Available Matchs");
        }

        matchListRecycler = ( RecyclerView ) findViewById(R.id.match_list_recycler);
        imageMatch = findViewById(R.id.match_picture);


        if (mTournamentName.equals("Australian Open")) {
            imageMatch.setImageResource(R.drawable.autralian_open_logo);
        } else if (mTournamentName.equals("Roland Garros")) {
            imageMatch.setImageResource(R.drawable.roland_garros_logo);
        } else if (mTournamentName.equals("Wimbledon")) {
            imageMatch.setImageResource(R.drawable.wimbledon_logo);
        } else if (mTournamentName.equals("US Open")) {
            imageMatch.setImageResource(R.drawable.us_open_logo);
        }


        matchListRecycler.setLayoutManager(new LinearLayoutManager(this));
        MatchListRecyclerViewAdapter mMatchesListAdpater = new MatchListRecyclerViewAdapter(matches, this);
        matchListRecycler.setAdapter(mMatchesListAdpater);

        DataRequest.getInstance().getMatchForTournament(this,idTournament);
    }

    @Override
    public void onMatchReceived(List<Match> matchList) {
        matches.clear();
        matches.addAll(matchList);
        MatchListRecyclerViewAdapter mMatchesListAdpater = new MatchListRecyclerViewAdapter(matches, this);
        matchListRecycler.setAdapter(mMatchesListAdpater);
    }

    @Override
    public void errorLoadingMatch() {
        Toast.makeText(this,"erreur de chargement des matchs", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMatchSelected(Match match) {

        DataRequest.getInstance().currentMatch = match;
        Intent intent = new Intent(this, MatchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }

}
