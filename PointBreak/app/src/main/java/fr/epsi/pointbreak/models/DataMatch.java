package fr.epsi.pointbreak.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tancrède on 17/05/2018.
 */

public class DataMatch {
    public int m_t1score;
    public int m_t2score;
    public int m_t1challenge;
    public int m_t2challenge;
    public int m_t1warning;
    public int m_t2warning;
    public int s_s1t1score;
    public int s_s1t2score;
    public int s_s2t1score;
    public int s_s2t2score;
    public int s_s3t1score;
    public int s_s3t2score;
    public int s_s4t1score;
    public int s_s4t2score;
    public int s_s5t1score;
    public int s_s5t2score;
    public int m_service;


    public DataMatch(JSONObject objectDataMatch){
        try {
            this.m_t1score = objectDataMatch.getInt("m_t1score");
            this.m_t2score = objectDataMatch.getInt("m_t2score");
            this.m_t1challenge = objectDataMatch.getInt("m_t1challenge");
            this.m_t2challenge = objectDataMatch.getInt("m_t2challenge");
            this.m_t1warning = objectDataMatch.getInt("m_t1warning");
            this.m_t2warning = objectDataMatch.getInt("m_t2warning");
            this.s_s1t1score = objectDataMatch.getInt("s_s1t1score");
            this.s_s1t2score = objectDataMatch.getInt("s_s1t2score");
            this.s_s2t1score = objectDataMatch.getInt("s_s2t1score");
            this.s_s2t2score = objectDataMatch.getInt("s_s2t2score");
            this.s_s3t1score = objectDataMatch.getInt("s_s3t1score");
            this.s_s3t2score = objectDataMatch.getInt("s_s3t2score");
            this.s_s4t1score = objectDataMatch.getInt("s_s4t1score");
            this.s_s4t2score = objectDataMatch.getInt("s_s4t2score");
            this.s_s5t1score = objectDataMatch.getInt("s_s5t1score");
            this.s_s5t2score = objectDataMatch.getInt("s_s5t2score");
            this.m_service = objectDataMatch.getInt("m_service");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
