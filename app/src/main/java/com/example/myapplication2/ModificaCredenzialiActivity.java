package com.example.myapplication2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModificaCredenzialiActivity extends AppCompatActivity {

    @BindView(R.id.input_vecchia_password) EditText _oldPassword;
    @BindView(R.id.input_nuova_password) EditText _newPassword;
    @BindView(R.id.btn_go) Button _goButton;

    JSONObject updateAuthJson = new JSONObject();
    private final String SIGNUP_URL = "http://fratelliminichillows.altervista.org/editPassword.php";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_credenziali);
        ButterKnife.bind(this);

        _goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go();
            }
        });

    }

    public void go(){

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _goButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ModificaCredenzialiActivity.this,
                R.style.AppTheme_Green_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Modifico le credenziali...");
        progressDialog.show();


        Bundle data = getIntent().getExtras();
        final String username = data.getString("username");
        String oldPassword = _oldPassword.getText().toString();
        String newPassword = _newPassword.getText().toString();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        try {
            updateAuthJson.put("username", username);
            updateAuthJson.put("oldPassword", oldPassword);
            updateAuthJson.put("newPassword", newPassword);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(StringRequest.Method.POST, SIGNUP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("SUCCESS")){
                    progressDialog.dismiss();
                    _goButton.setEnabled(true);
                    Toast.makeText(getBaseContext(), "Password cambiata!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }

                else if(response.equals("WRONG_PASSWORD")) {
                    progressDialog.dismiss();
                    _goButton.setEnabled(true);
                    Toast.makeText(getBaseContext(), "Errore: la vecchia password non è corretta!", Toast.LENGTH_LONG).show();
                }

                else if(response.equals("SAME_PASSWORD")) {
                    progressDialog.dismiss();
                    _goButton.setEnabled(true);
                    Toast.makeText(getBaseContext(), "Errore: la vecchia password e la nuova coincidono!", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "C'è stato un problema di connessione, riprova!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return updateAuthJson.toString().getBytes("utf-8");
                } catch (Exception e) {
                }

                return null;
            }
        };
        requestQueue.add(request);

    }

    public void onSignupFailed() {
        _goButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String password = _newPassword.getText().toString();

        if (password.isEmpty() || password.length() < 8) {
            _newPassword.setError("La nuova password deve contenere almeno 8 caratteri!");
            valid = false;
        } else {
            _newPassword.setError(null);
        }

        return valid;
    }

}
