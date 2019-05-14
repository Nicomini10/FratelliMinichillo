package com.example.myapplication2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ConfirmationAccountActivity extends AppCompatActivity {

    @BindView(R.id.input_code) EditText _inputCode;
    @BindView(R.id.btn_confirm) Button _confirmButton;

    JSONObject confirmAccountJson = new JSONObject();
    private final String CONFIRM_CODE_URL = "http://fratelliminichillows.altervista.org/confirmation.php";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_account);
        ButterKnife.bind(this);

        _confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }

    public void signup() {

        if (!validate()) {
            _confirmButton.setEnabled(true);
            return;
        }

        _confirmButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ConfirmationAccountActivity.this,
                R.style.AppTheme_Green_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Confermo il tuo account...");
        progressDialog.show();

        String code = _inputCode.getText().toString();
        Bundle data = getIntent().getExtras();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        try {

            confirmAccountJson.put("username", data.getString("username"));
            confirmAccountJson.put("password", data.getString("password"));
            confirmAccountJson.put("key", code);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(StringRequest.Method.POST, CONFIRM_CODE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("CORRECT_KEY")) {
                    progressDialog.dismiss();
                    _confirmButton.setEnabled(true);
                    Toast.makeText(getBaseContext(), "Account confermato!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    progressDialog.dismiss();
                    _confirmButton.setEnabled(true);
                    Toast.makeText(getBaseContext(), "Il codice non corrisponde!", Toast.LENGTH_LONG).show();
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
                    return confirmAccountJson.toString().getBytes("utf-8");
                } catch (Exception e) {
                }

                return null;
            }
        };
        requestQueue.add(request);

    }

    public boolean validate() {
        boolean valid = true;

        String code = _inputCode.getText().toString();

        if (code.isEmpty()) {
            _inputCode.setError("Inserisci il codice!");
            valid = false;
        } else {
            _inputCode.setError(null);
        }

        return valid;
    }
}



