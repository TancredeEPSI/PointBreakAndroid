package fr.epsi.pointbreak.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.epsi.pointbreak.R;
import fr.epsi.pointbreak.adapters.SetListRecyclerViewAdapter;
import fr.epsi.pointbreak.dataRequest.DataRequest;
import fr.epsi.pointbreak.keys.Keys;
import fr.epsi.pointbreak.models.DataMatch;
import fr.epsi.pointbreak.models.Match;

public class MatchActivity extends AppCompatActivity implements DataRequest.ActionInterface {

    public Match match;
    private DataMatch dataMatch;

    //View
    private ConstraintLayout view;
    private ImageView imageCourt;

    //joueurs
    private TextView textNomGauche;
    private TextView textPrenomGauche;
    private ImageView imageFlagGauche;
    private TextView textNomDroite;
    private TextView textPrenomDroite;
    private ImageView imageFlagDroite;

    //Score
    private TextView textScoreT1;
    private TextView textScoreT2;

    //Btn
    private SubmitButton btnWIn;
    private SubmitButton btnLoose;
    private SubmitButton btnAce;
    private SubmitButton btnFault;
    private SubmitButton btnLet;
    private Button btnChrono;
    private FloatingActionButton fabAlerte;

    //chrono
    private TextView textChrono;
    private boolean isRunning = false;
    private CountDownTimer countDownTimer;
    private Chronometer chrono;

    //Service
    private ImageView imageServiceGauche;
    private ImageView imageServiceDroit;

    //Loading
    private ProgressDialog progress;

    //Score
    private List<List<String>> score = new ArrayList<>();
    private RecyclerView setListRecycler;
    private SetListRecyclerViewAdapter setListAdpater;

    //warnings / challenges
    private TextView textChallengeDroit;
    private TextView textChallengeGauche;
    private TextView textWarningDroit;
    private TextView textWarningGauche;

    //Attribut
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        match = DataRequest.getInstance().currentMatch;

        initComposants();
        initListeners(this);
        initMenuAlerte(this);

        //init des texts qui ne devraient pas changer
        textPrenomGauche.setText(match.playerThreeName);
        imageFlagGauche.setImageResource(getFlag(match.playerThreeCountry));
        textPrenomDroite.setText(match.playerOneName);
        imageFlagDroite.setImageResource(getFlag(match.playerOneCountry));

        if ( match.isSimple ){
            textNomGauche.setVisibility(View.INVISIBLE);
            textNomDroite.setVisibility(View.INVISIBLE);
        }else{
            textNomGauche.setText(match.playerTwoName);
            textNomDroite.setText(match.playerFourName);
        }

        // loading des données du début
        progress = new ProgressDialog(this);
        progress.setTitle("Chargement des données");
        progress.setMessage("Veuillez patienter pendant le chargement des données");
        progress.setCancelable(false);


        if ( !match.isStarded ){
            this.showPopupStart(this);
        }else {
            //sert a refresh les données
            DataRequest.getInstance().sendAction(this,match.id,Keys.Action.Start.intValue,0);
            progress.show();
            long lastSuccess = match.dateDebut.getTime(); //Some Date object
            long elapsedRealtimeOffset = System.currentTimeMillis() - SystemClock.elapsedRealtime();
            chrono.setBase(lastSuccess - elapsedRealtimeOffset);
            chrono.start();
        }

    }

    private void refreshView() {

        if (dataMatch != null ){

            this.displayScores();
            this.refreshSet();
            this.refreshService();

        }

    }

    private void showPopupChallenge(final Activity activity){

        this.fabAlerte.performClick();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("CHALLENGE");
        builder.setCancelable(true);
        String mes = "Veuillez choisir l'équipe qui va recevoir le challenge";
        String team1 = match.playerOneName;
        String team2 = match.playerThreeName;

        builder.setMessage(mes);
        builder.setPositiveButton(team1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataRequest.getInstance().sendAction(activity, match.id, Keys.Action.Challenge.intValue, 1);
                progress.show();
            }
        });
        builder.setNegativeButton(team2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataRequest.getInstance().sendAction(activity, match.id, Keys.Action.Challenge.intValue, 2);
                progress.show();
            }
        });

        builder.show();

    }

    private void showPopupWarning(final Activity activity){

        this.fabAlerte.performClick();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("WARNING");
        builder.setCancelable(true);
        String mes = "Veuillez choisir l'équipe qui va recevoir l'avertissement";
        String team1 = match.playerOneName;
        String team2 = match.playerThreeName;

        builder.setMessage(mes);
        builder.setPositiveButton(team1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataRequest.getInstance().sendAction(activity, match.id, Keys.Action.Warning.intValue, 1);
                progress.show();
            }
        });
        builder.setNegativeButton(team2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DataRequest.getInstance().sendAction(activity, match.id, Keys.Action.Warning.intValue, 2);
                progress.show();
            }
        });

        builder.show();

    }

    private void showPopupStart(final Activity activity) {

        DataRequest.getInstance().sendAction(activity, match.id, Keys.Action.Start.intValue, 0);
        chrono.start();
        match.isStarded = true;
        match.dateDebut = new Date();
        progress.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Début de partie");
        builder.setCancelable(false);
        String mes = "";
        String team1 = "";
        String team2 = "";
        if ( this.match.isSimple ){
            mes = "Choississez le joueur qui commence à servir";
            team1 = match.playerOneName;
            team2 = match.playerThreeName;
        }else{
            mes = "Choississez l'équipe qui commence à servir";
            team1 = "Equipe 1";
            team2 = "Equipe 2";
        }

        builder.setMessage(mes);
        builder.setPositiveButton(team1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {;
                DataRequest.getInstance().sendAction(activity, match.id, Keys.Action.Service1.intValue, 0);
                progress.show();
            }
        });
        builder.setNegativeButton(team2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataRequest.getInstance().sendAction(activity, match.id, Keys.Action.Service2.intValue, 0);
                progress.show();
            }
        });

        builder.show();
    }

    private void initMenuAlerte(final Activity activity) {

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setLayoutParams(new FrameLayout.LayoutParams(200, 200));

        //Warning
        ImageView itemIconWarning = new ImageView(this);
        itemIconWarning.setImageResource(R.drawable.warning);
        SubActionButton buttonWarning = itemBuilder.setContentView(itemIconWarning).build();
        itemIconWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWarning(activity);
            }
        });

        //Medic
        ImageView itemIconMedic = new ImageView(this);
        itemIconMedic.setImageResource(R.drawable.medic);
        SubActionButton buttonMedic = itemBuilder.setContentView(itemIconMedic).build();
        itemIconMedic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRequest.getInstance().sendAction(activity, match.id, Keys.Action.Medic.intValue, dataMatch.m_service);
            }
        });

        //Challenge
        ImageView itemIconChallenge = new ImageView(this);
        itemIconChallenge.setImageResource(R.drawable.challenge);
        SubActionButton buttonChallenge = itemBuilder.setContentView(itemIconChallenge).build();
        itemIconChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupChallenge(activity);
            }
        });

        //Pause
        ImageView itemIconPause = new ImageView(this);
        itemIconPause.setImageResource(R.drawable.pause);
        SubActionButton buttonPause = itemBuilder.setContentView(itemIconPause).build();
        itemIconPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRequest.getInstance().sendAction(activity, match.id, Keys.Action.PauseMatch.intValue, dataMatch.m_service);
                chrono.stop();
            }
        });

        //Unpause/Resume
        ImageView itemIconResume = new ImageView(this);
        itemIconResume.setImageResource(R.drawable.play);
        SubActionButton buttonResume = itemBuilder.setContentView(itemIconResume).build();
        itemIconResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRequest.getInstance().sendAction(activity, match.id, Keys.Action.UnPauseMatch.intValue, dataMatch.m_service);
                chrono.start();
            }
        });

        //End
        ImageView itemIconEnd = new ImageView(this);
        itemIconEnd.setImageResource(R.drawable.stop);
        SubActionButton buttonEnd = itemBuilder.setContentView(itemIconEnd).build();
        itemIconEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRequest.getInstance().sendAction(activity, match.id, Keys.Action.End.intValue, dataMatch.m_service);
                chrono.stop();
            }
        });

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .setStartAngle(180)
                .setEndAngle(270)
                .setRadius(600)
                .addSubActionView(buttonWarning)
                .addSubActionView(buttonMedic)
                .addSubActionView(buttonChallenge)
                .addSubActionView(buttonPause)
                .addSubActionView(buttonResume)
                .addSubActionView(buttonEnd)
                .attachTo(fabAlerte)
                .build();


    }

    private void initListeners(final MatchActivity matchActivity) {
        btnWIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRequest.getInstance().sendAction(matchActivity, match.id, Keys.Action.pointWon.intValue,dataMatch.m_service);
            }
        });
        btnWIn.setOnResultEndListener(new SubmitButton.OnResultEndListener() {
            @Override
            public void onResultEnd() {
                btnWIn.reset();
            }
        });

        btnLoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRequest.getInstance().sendAction(matchActivity, match.id, Keys.Action.pointWon.intValue,dataMatch.m_service == 1 ? 2 : 1);
            }
        });
        btnLoose.setOnResultEndListener(new SubmitButton.OnResultEndListener() {
            @Override
            public void onResultEnd() {
                btnLoose.reset();
            }
        });

        btnAce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRequest.getInstance().sendAction(matchActivity, match.id, Keys.Action.Ace.intValue,dataMatch.m_service);
            }
        });
        btnAce.setOnResultEndListener(new SubmitButton.OnResultEndListener() {
            @Override
            public void onResultEnd() {
                btnAce.reset();
            }
        });

        btnFault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRequest.getInstance().sendAction(matchActivity, match.id, Keys.Action.Fault.intValue,dataMatch.m_service);
            }
        });
        btnFault.setOnResultEndListener(new SubmitButton.OnResultEndListener() {
            @Override
            public void onResultEnd() {
                btnFault.reset();
            }
        });

        btnLet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRequest.getInstance().sendAction(matchActivity, match.id, Keys.Action.Let.intValue,dataMatch.m_service);
            }
        });
        btnLet.setOnResultEndListener(new SubmitButton.OnResultEndListener() {
            @Override
            public void onResultEnd() {
                btnLet.reset();
            }
        });

        btnChrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning) {
                    countDownTimer.start();
                    isRunning = true;
                }
            }
        });
    }

    private void initComposants() {

        //view
        view = findViewById(R.id.view);
        imageCourt = findViewById(R.id.imgCourt);

        switch (DataRequest.getInstance().tournamentName){

            case "Australian Open":
                color = getResources().getColor(R.color.openAustralie);
                view.setBackgroundColor(color);
                imageCourt.setImageResource(R.drawable.australianopen_court);
                break;

            case "Roland Garros":
                color = getResources().getColor(R.color.rollandGarros);
                view.setBackgroundColor(color);
                imageCourt.setImageResource(R.drawable.frenchopen_court);
                break;

            case "Wimbledon":
                color = getResources().getColor(R.color.wimbledon);
                view.setBackgroundColor(color);
                imageCourt.setImageResource(R.drawable.wimbledon_court);
                break;

            case "US Open":
                color = getResources().getColor(R.color.usOpen);
                view.setBackgroundColor(color);
                imageCourt.setImageResource(R.drawable.usopen_court);
                break;

            default:
                color = getResources().getColor(R.color.openAustralie);
                view.setBackgroundColor(color);
                imageCourt.setImageResource(R.drawable.australianopen_court);
                break;

        }

        //Joueurs
        textNomGauche = findViewById(R.id.textNomGauche);
        textPrenomGauche = findViewById(R.id.textPrenomGauche);
        imageFlagGauche = findViewById(R.id.imageFlagGauche);
        textNomDroite = findViewById(R.id.textNomDroit);
        textPrenomDroite = findViewById(R.id.textPrenomDroit);
        imageFlagDroite = findViewById(R.id.imageFlagDroite);

        //Score
        textScoreT1 = findViewById(R.id.textScoreGauche);
        textScoreT2 = findViewById(R.id.textScoreDroite);
        setListRecycler = findViewById(R.id.setList);

        //btn
        btnWIn = findViewById(R.id.btnWin);
        btnLoose = findViewById(R.id.btnLoose);
        btnChrono = findViewById(R.id.btnChrono);
        btnChrono.setText(getResources().getString(R.string.Start));
        fabAlerte = findViewById(R.id.fabAlerte);
        btnAce = findViewById(R.id.btnAce);
        btnFault = findViewById(R.id.btnFault);
        btnLet = findViewById(R.id.btnLet);

        //Chrono
        textChrono = findViewById(R.id.textChrono);
        countDownTimer = new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                textChrono.setText(getResources().getString(R.string.TimeRemaining) + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textChrono.setText("done!");
                showFinTimer();
                isRunning = false;
            }
        };
        chrono = findViewById(R.id.chronometer);

        //Service
        imageServiceGauche = findViewById(R.id.imageServiceGauche);
        imageServiceDroit = findViewById(R.id.imageServiceDroit);

        //Warnings et Challenge
        textWarningDroit = findViewById(R.id.textWarningDroite);
        textWarningGauche = findViewById(R.id.textWarningGauche);
        textChallengeDroit = findViewById(R.id.textChallengeDroite);
        textChallengeGauche = findViewById(R.id.textChallengeGauche);

    }

    private void showFinTimer() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Voulez vous lancer le prochain jeu ?")
                .setTitle(getResources().getString(R.string.PauseFinished));

        // Add the buttons
        builder.setPositiveButton(getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public void onActionCallback(DataMatch dataMatch) {
        this.dataMatch = dataMatch;
        this.resultBtn(true);
        progress.dismiss();
        this.refreshView();
    }

    @Override
    public void errorActionCallback() {
        this.resultBtn(true);
        progress.dismiss();
        Toast.makeText(this,"Erreur dans le rechargement des donneés",Toast.LENGTH_LONG).show();
    }

    private void resultBtn(Boolean result){
        btnWIn.doResult(result);
        btnLoose.doResult(result);
        btnAce.doResult(result);
        btnFault.doResult(result);
        btnLet.doResult(result);

    }

    private void displayScores(){

        String score1 = "0";
        String score2 = "0";
        int s1 = dataMatch.m_t1score;
        int s2 = dataMatch.m_t2score;

        if ( s1 == 0 ){
            score1 = "0";
        }else if ( s1 == 1 ){
            score1 = "15";
        }else if ( s1 == 2 ){
            score1 = "30";
        }else if ( s1 == 3){
            score1 = "40";
        }else {
            if ( (s1 - s2) <= 0 ){
                score1 = "40";
            }else{
                score1 = "A";
            }
        }

        if ( s2 == 0 ){
            score2 = "0";
        }else if ( s2 == 1 ){
            score2 = "15";
        }else if ( s2 == 2 ){
            score2 = "30";
        }else if ( s2 == 3){
            score2 = "40";
        }else {
            if ( (s2 - s1) <= 0 ){
                score2 = "40";
            }else{
                score2 = "A";
            }
        }

        textScoreT1.setText(score1);
        textScoreT2.setText(score2);

        textWarningDroit.setText(dataMatch.m_t2warning + "");
        textWarningGauche.setText(dataMatch.m_t1warning + "");
        textChallengeDroit.setText(dataMatch.m_t2challenge + "/3");
        textChallengeGauche.setText(dataMatch.m_t1challenge + "/3");

        refreshSet();

    }

    private void refreshService(){
        if ( dataMatch.m_service == 2 ) {
            this.imageServiceGauche.setImageResource(R.drawable.tennis_ball_transparent);
            this.imageServiceDroit.setImageResource(R.drawable.tennis_ball_green_transparent);
        }else{
            this.imageServiceGauche.setImageResource(R.drawable.tennis_ball_green_transparent);
            this.imageServiceDroit.setImageResource(R.drawable.tennis_ball_transparent);
        }
    }

    private void refreshSet(){
        score.clear();
        List<String> temp = new ArrayList<>();
        temp.add(dataMatch.s_s1t1score + "");
        temp.add(dataMatch.s_s1t2score + "");
        score.add(temp);

        temp = new ArrayList<>();
        temp.add(dataMatch.s_s2t1score + "");
        temp.add(dataMatch.s_s2t2score + "");
        score.add(temp);

        temp = new ArrayList<>();
        temp.add(dataMatch.s_s3t1score + "");
        temp.add(dataMatch.s_s3t2score + "");
        score.add(temp);

        temp = new ArrayList<>();
        temp.add(dataMatch.s_s4t1score + "");
        temp.add(dataMatch.s_s4t2score + "");
        score.add(temp);

        temp = new ArrayList<>();
        temp.add(dataMatch.s_s5t1score + "");
        temp.add(dataMatch.s_s5t2score + "");
        score.add(temp);

        setListRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        setListAdpater = new SetListRecyclerViewAdapter(score);
        setListRecycler.setAdapter(setListAdpater);
        setListRecycler.requestLayout();

    }

    private int getFlag(String country){

        switch (country){

            case "NGR":
                return R.drawable.nigeria;
            case "GER":
                return R.drawable.germany;
            case "SUI":
                return R.drawable.switzerland;
            case "ESP":
                return R.drawable.espagne;
            case "MAR":
                return R.drawable.morocco;
            case "GBR":
                return R.drawable.gbr;
            case "CAN":
                return R.drawable.canada;
            case "USA":
                return R.drawable.usa;

            default:
                return R.drawable.andorra;
        }
    }
}
