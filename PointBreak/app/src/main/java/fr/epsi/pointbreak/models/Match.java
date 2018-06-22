package fr.epsi.pointbreak.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by frus71323 on 25/01/2018.
 */

public class Match {

    private enum ResponseKeys {
        //action de jeu
        id("id"),
        playerOneName("playerOneName"),
        playerTwoName("playerTwoName"),
        playerThreeName("playerThreeName"),
        playerFourName("playerFourName"),
        playerOneCountry("playerOneCountry"),
        playerTwoCountry("playerTwoCountry"),
        playerThreeCountry("playerThreeCountry"),
        playerFourCountry("playerFourCountry"),
        dateMatch("dateMatch"),
        bestOf("bestOf"),
        roundTournament("roundTournament"),
        court("court"),
        tournamentType("tournamentType");

        public String stringValue;

        private ResponseKeys(String toString) {
            stringValue = toString;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

    public int id;
    public boolean isStarded = false;
    public boolean isSimple;
    public String playerOneName;
    public String playerTwoName;
    public String playerThreeName;
    public String playerFourName;
    public String playerOneCountry;
    public String playerTwoCountry;
    public String playerThreeCountry;
    public String playerFourCountry;
    public Date dateMatch;
    public Integer bestOf;
    public String roundTournament;
    public String court;
    public String tournamentType;
    public Date dateDebut;

    public Match(JSONObject matchObject) {
        try {
            this.id = matchObject.getInt(ResponseKeys.id.name());
            this.playerOneName = matchObject.getString(ResponseKeys.playerOneName.name());
            this.playerTwoName = matchObject.getString(ResponseKeys.playerTwoName.name());
            this.isSimple = this.playerTwoName.equals("null");
            this.playerThreeName = matchObject.getString(ResponseKeys.playerThreeName.name());
            this.playerFourName = matchObject.getString(ResponseKeys.playerFourName.name());
            this.playerOneCountry = matchObject.getString(ResponseKeys.playerOneCountry.name());
            this.playerTwoCountry = matchObject.getString(ResponseKeys.playerTwoCountry.name());
            this.playerThreeCountry = matchObject.getString(ResponseKeys.playerThreeCountry.name());
            this.playerFourCountry = matchObject.getString(ResponseKeys.playerFourCountry.name());
            this.dateMatch = getDate(matchObject.getString(ResponseKeys.dateMatch.name()));
            this.bestOf = matchObject.getInt(ResponseKeys.bestOf.name());
            this.roundTournament = matchObject.getString(ResponseKeys.roundTournament.name());
            this.court = matchObject.getString(ResponseKeys.court.name());
            this.tournamentType = matchObject.getString(ResponseKeys.tournamentType.name());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Date getDate(String dateString){

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = new Date();
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);
        return date;
    }
}
