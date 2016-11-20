package hu.eke.colorlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private static final String EMAIL = "foo@bar.com";
    private static final String PASS = "pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Storage storage = new Storage(this);
        if(storage.getEmail() != null){
            startMainActivity();
        }

        final EditText emailEdit = (EditText) findViewById(R.id.email_editText);
        final EditText passEdit = (EditText) findViewById(R.id.password_editText);
        Button loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passEdit.setError(null);

                String email = emailEdit.getText().toString();
                String pass = passEdit.getText().toString();
                if (EMAIL.equals(email) && PASS.equals(pass)) {
                    storage.setEmail(email);
                    startMainActivity();
                } else {
                    passEdit.setError("Hibás bejelentkezés");
                }
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
