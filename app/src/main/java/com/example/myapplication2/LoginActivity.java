package com.example.myapplication2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.app.ProgressDialog;
import android.util.Log;

import android.content.Intent;
import android.widget.Button;
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
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_username)
    EditText _usernameText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    JSONObject loginJson = new JSONObject();
    private final String LOGIN_URL = "http://fratelliminichillows.altervista.org/login.php";
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

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
                startActivityForResult(intent, REQUEST_SIGNUP);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
}
