package com.example.myapplication2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.BindView;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;

    JSONObject signupJson = new JSONObject();
    private final String SIGNUP_URL = "http://fratelliminichillows.altervista.org/signup.php";
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void signup() {

        if (!validate()) {
            return;
        }

        // _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Green_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creo il tuo account...");
        progressDialog.show();

        String username = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        try {

            signupJson.put("username", username);
            signupJson.put("password", password);
            signupJson.put("email", email);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(
                StringRequest.Method.POST,
                SIGNUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("Email sent")) {
                            progressDialog.dismiss();
                            onSignupSuccess();
                        } else if(response.equals("DUPLICATE_USERNAME")){
                            progressDialog.dismiss();
                            onSignupFailed("Username");

                        } else if(response.equals("DUPLICATE_EMAIL")){
                            progressDialog.dismiss();
                            onSignupFailed("Email");
                        }

                    }
                },
                new Response.ErrorListener() {
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
                    Log.d("DIO", signupJson.toString());
                    return signupJson.toString().getBytes("utf-8");
                } catch (Exception e) {
                    Log.d("DIO", e.toString());
                }

                return null;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);

    }


    public void onSignupSuccess() {
        setResult(RESULT_OK, null);
        _signupButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), ConfirmationAccountActivity.class);
        intent.putExtra("username", _nameText.getText().toString());
        intent.putExtra("password", _passwordText.getText().toString());
        startActivity(intent);
    }

    public void onSignupFailed(String problem) {
        _signupButton.setEnabled(true);
        Toast.makeText(getBaseContext(), problem + " già in uso da un altro account!", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("Inserisci un username di almeno 3 caratteri!");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Inserisci un indirizzo email valido!");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 8) {
            _passwordText.setError("Inserisci una password di almeno 8 caratteri!");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
