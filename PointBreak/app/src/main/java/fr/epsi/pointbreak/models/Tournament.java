package fr.epsi.pointbreak.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by frus71323 on 23/01/2018.
 */

public class Tournament {


    private enum ResponseKeys {
        //action de jeu
        id("id"),
        tournamentName("tournamentName"),
        surfaceType("surfaceType"),
        gender("gender"),
        numberOfPlayersInvolved("numberOfPlayersInvolved"),
        tournamentStartingDate("tournamentStartingDate"),
        tournamentEndingDate("tournamentEndingDate");

        public String stringValue;

        private ResponseKeys(String toString) {
            stringValue = toString;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }

    private int id;
    private String tournamentName;
    private String surfaceType;
    private String gender;
    private int numberOfPlayersInvolved;
    private Date tournamentStartingDate;
    private Date tournamentEndingDate;

    public Tournament(String name, int id) {
        this.tournamentName = name;
        this.id = id;
    }

    public Tournament(JSONObject tournamentObject){

        try {
            this.id = tournamentObject.getInt(ResponseKeys.id.name());
            this.tournamentName = tournamentObject.getString(ResponseKeys.tournamentName.name());
            this.surfaceType = tournamentObject.getString(ResponseKeys.surfaceType.name());
            this.gender = tournamentObject.getString(ResponseKeys.gender.name());
            this.numberOfPlayersInvolved = tournamentObject.getInt(ResponseKeys.numberOfPlayersInvolved.name());
            this.tournamentStartingDate = this.getDate( tournamentObject.getLong(ResponseKeys.tournamentStartingDate.name()));
            this.tournamentEndingDate = this.getDate( tournamentObject.getLong(ResponseKeys.tournamentEndingDate.name()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Date getDate(long timeStamp){

        try{
            Date netDate = (new Date(timeStamp));
            return netDate;
        }
        catch(Exception ex){
            return new Date();
        }
    }

    public String getName() {
        return tournamentName;
    }

    public void setName(String name) {
        this.tournamentName = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
