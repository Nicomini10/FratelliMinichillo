package com.example.myapplication2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.app.ProgressDialog;
import android.util.Log;

import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.input_username)
    EditText _usernameText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    @BindView(R.id.checkbox_remember_me)
    CheckBox _remember_me_box;

    JSONObject loginJson = new JSONObject();
    private final String LOGIN_URL = "http://fratelliminichillows.altervista.org/login.php";
    private RequestQueue requestQueue;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            _usernameText.setText(loginPreferences.getString("username", ""));
            _passwordText.setText(loginPreferences.getString("password", ""));
            _remember_me_box.setChecked(true);
        }



        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });


    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Green_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Accesso in corso...");
        progressDialog.show();

        final String username = _usernameText.getText().toString();
        final String password = _passwordText.getText().toString();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        try {

            loginJson.put("username", username);
            loginJson.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(StringRequest.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("ACCESS_FAILURE")) {
                    progressDialog.dismiss();
                    onLoginFailed();
                } else if (response.equals("NOT_ACTIVE_ACCOUNT")) {
                    progressDialog.dismiss();
                    onAccountToConfirm();
                } else {
                    progressDialog.dismiss();
                    onLoginSuccess();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _loginButton.setEnabled(true);
                Toast.makeText(getBaseContext(), "C'è stato un problema di connessione, riprova!" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return loginJson.toString().getBytes("utf-8");
                } catch (Exception e) {
                }

                return null;
            }
        };
        requestQueue.add(request);

    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (_remember_me_box.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("username", _usernameText.getText().toString());
        startActivity(intent);
        this.finish();
    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Login non riuscito. Controlla nome utente e password!", Toast.LENGTH_LONG).show();
    }

    public void onAccountToConfirm() {
        _loginButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Account non attivo!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), ConfirmationAccountActivity.class);
        intent.putExtra("username", _usernameText.getText().toString());
        intent.putExtra("password", _passwordText.getText().toString());
        startActivity(intent);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            _usernameText.setError("Inserisci un username valido di almeno 3 caratteri!");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 8) {
            _passwordText.setError("Inserisci una password valida di almeno 8 caratteri!");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void onCheckboxClicked(View view) {
        if(_remember_me_box.isChecked())
            Toast.makeText(getBaseContext(), "Ricorderò i tuoi dati!", Toast.LENGTH_SHORT).show();
        else Toast.makeText(getBaseContext(), "Non ricorderò i tuoi dati!", Toast.LENGTH_SHORT).show();

    }
}
