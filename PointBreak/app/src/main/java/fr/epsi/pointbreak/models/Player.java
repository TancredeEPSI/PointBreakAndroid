package fr.epsi.pointbreak.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by frus71323 on 25/01/2018.
 */

public class Player {

    private enum ResponseKeys {
        //action de jeu
        id("id"),
        firstName("firstName"),
        lastName("lastName"),
        hand("hand"),
        country("country"),
        birthdate("birthdate");

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
    private String firstName;
    private String lastName;
    private String hand;
    private Date birthdate;
    private String country;

    public Player(JSONObject jsonPlayer){
        try {
            this.id = jsonPlayer.getInt(ResponseKeys.id.name());
            this.firstName = jsonPlayer.getString(ResponseKeys.firstName.name());
            this.lastName = jsonPlayer.getString(ResponseKeys.lastName.name());
            this.hand = jsonPlayer.getString(ResponseKeys.hand.name());
            this.birthdate = this.getDate(jsonPlayer.getLong(ResponseKeys.birthdate.name()));
            this.country = jsonPlayer.getString(ResponseKeys.country.name());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHand() {
        return hand;
    }

    public void setHand(String hand) {
        this.hand = hand;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

}
