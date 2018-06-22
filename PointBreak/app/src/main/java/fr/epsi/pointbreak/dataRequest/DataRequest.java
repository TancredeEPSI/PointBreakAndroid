package fr.epsi.pointbreak.dataRequest;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.epsi.pointbreak.models.DataMatch;
import fr.epsi.pointbreak.models.Match;
import fr.epsi.pointbreak.models.Player;
import fr.epsi.pointbreak.models.Tournament;

public class DataRequest {

    private RequestQueue queue ;
    private final String url = "http://10.0.2.2:8080/";
    private static DataRequest instance;
    private TournamentInterface tournamentInterface;
    private MatchInterface matchInterface;
    private PlayerInterface playerInterface;
    private ActionInterface actionInterface;
    private LoginInterface loginInterface;

    public Match currentMatch;
    public String tournamentName;
    private int refereeId;

    private DataRequest(){}

    public static DataRequest getInstance(){
        return DataRequest.getInstance(null);
    }

    public static DataRequest getInstance(Context context){
        if (instance == null){
            instance = new DataRequest();
        }

        if ( context != null) {
            instance.queue = Volley.newRequestQueue(context);
        }

        return instance;
    }

    // Interfaces
    public interface TournamentInterface {
        void onTournamentReceived( List<Tournament> tournaments);
        void errorLoadingTournaments();
    }

    public interface MatchInterface {
        void onMatchReceived( List<Match> matchList);
        void errorLoadingMatch();
    }

    public interface PlayerInterface{
        void onPlayerReceiveid(Player player);
        void errorLoadingPlayer();
    }

    public interface ActionInterface{
        void onActionCallback(DataMatch dataMatch);
        void errorActionCallback();
    }

    public interface LoginInterface{
        void onLoginSucceed();
        void onLoginFail();
    }

    public void getTournament(Context context) {

        tournamentInterface = (TournamentInterface) context;

        String urlRequest = url + "tournamentlist/" + refereeId;

        final List<Tournament> tournamentList = new ArrayList<>();


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, urlRequest, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0 ; i < response.length() ; i ++){
                            try {
                                tournamentList.add(new Tournament(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("GET TOURNAMENT");
                        System.out.println( response );
                        tournamentInterface.onTournamentReceived(tournamentList);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("GET TOURNAMENT ERROR");
                        System.out.println(error);
                        tournamentInterface.errorLoadingTournaments();
                    }
                });

        queue.add(jsonObjectRequest);

    }

    public void getMatchForTournament(Context context, int idTournament) {

        matchInterface = ( MatchInterface ) context;

        String urlRequest = url + "matchlist/" + refereeId + "/" + idTournament;

        final List<Match> matchList = new ArrayList<>();


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, urlRequest, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("GET Match for TOURNAMENT");
                        System.out.println( response );

                        for (int i = 0 ; i < response.length() ; i ++){
                            try {
                                matchList.add(new Match(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        matchInterface.onMatchReceived(matchList);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("GET Match for TOURNAMENT ERROR");
                        System.out.println(error);
                        matchInterface.errorLoadingMatch();
                    }
                });

        queue.add(jsonObjectRequest);

    }

    public void logReferee(Context context, String log, String password){

        loginInterface = (LoginInterface) context;

        String urlRequest = url + "process/" + log + "/" + password;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlRequest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("GET logReferee");
                        System.out.println( response );
                        refereeId = Integer.parseInt(response);
                        if(refereeId == 0){
                            loginInterface.onLoginFail();
                        }else{
                            loginInterface.onLoginSucceed();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("GET logReferee Error");
                System.out.println(error);
                loginInterface.onLoginFail();
            }
        });

        queue.add(stringRequest);

    }

    public void getPlayerFromId(Context context, int idPlayer, int indexMatch){

        this.playerInterface = (PlayerInterface) context;

        String urlRequest = url + "player/" + idPlayer ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlRequest, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("GET getPlayerFromId");
                System.out.println( response );
                playerInterface.onPlayerReceiveid(new Player(response));
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("GET getPlayerFromId");
                System.out.println(error);
                playerInterface.errorLoadingPlayer();
            }
        });

        queue.add(jsonObjectRequest);

    }

    public void sendAction(Activity activity, int idMatch, int idAction, int numTeam){

        this.actionInterface = (ActionInterface) activity;

        String urlRequest = url + "action/" + idMatch + "/" + idAction + "/" + numTeam ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlRequest, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("GET sendAction");
                System.out.println( response );
                actionInterface.onActionCallback(new DataMatch(response));
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("GET sendAction");
                System.out.println(error);
                actionInterface.errorActionCallback();
            }
        });

        queue.add(jsonObjectRequest);

    }
}
