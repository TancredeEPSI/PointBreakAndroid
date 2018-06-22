package fr.epsi.pointbreak.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import fr.epsi.pointbreak.R;
import fr.epsi.pointbreak.dataRequest.DataRequest;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, DataRequest.LoginInterface {

    private Button mConnectButton;
    private EditText mLoginText;
    private EditText mPasswordText;

    private ProgressDialog progress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mConnectButton = findViewById(R.id.activity_login_button);
        mLoginText = findViewById(R.id.activity_login_e_mail);
        mPasswordText = findViewById(R.id.activity_login_password);

        mConnectButton.setOnClickListener(this);

        // loading des données du début
        progress = new ProgressDialog(this);
        progress.setTitle("Authentification");
        progress.setMessage("Veuillez patienter pendant l'anthentification");
        progress.setCancelable(false);
    }

    @Override
    public void onClick(View v) {

        if (v == mConnectButton) {

            if( !mLoginText.getText().toString().equals("")  && !mPasswordText.getText().toString().equals("") ){
                try {

                    DataRequest.getInstance(this).logReferee(this,mLoginText.getText().toString(),encrypte(mPasswordText.getText().toString()));
                    progress.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this,"Veuillez rentrer des identifiants",Toast.LENGTH_LONG).show();
            }


        }
    }

    public String encrypte(String input) {
        String key = "testtesttesttest";
        byte[] crypted = null;
        try {

            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();

        String coded = encoder.encodeToString(crypted);
        System.out.println("test : " +  coded);
        return coded;
    }

    public static String decrypt(String input, String key) {
        byte[] output = null;
        try {
            java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(decoder.decode(input));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println("test Decrypt : " + new String(output));
        return new String(output);
    }

    @Override
    public void onLoginSucceed() {
        progress.dismiss();
        Intent intent = new Intent(this, TournamentsListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onLoginFail() {
        progress.dismiss();
        Toast.makeText(this,"Echec de connexion",Toast.LENGTH_LONG).show();
    }
}

