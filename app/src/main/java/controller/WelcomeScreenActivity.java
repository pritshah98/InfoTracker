package controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pritshah.infotracker.R;

import java.util.HashMap;

import model.User;

public class WelcomeScreenActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    public static String userEmail = "";
    public static String currentUser = "";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public static HashMap<String, User> userMap;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        Button signIn = (Button) findViewById(R.id.signInButton);
        Button register = (Button) findViewById(R.id.registerButton);
        Button login = (Button) findViewById(R.id.loginButton);
        final EditText username = (EditText) findViewById(R.id.inputUsername);
        final EditText password = (EditText) findViewById(R.id.inputPassword);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        userMap = new HashMap<>();
        layout = (ConstraintLayout) findViewById(R.id.cLayout);

        layout.setBackgroundColor(Color.parseColor("#FFFFE0"));

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }

                return false;
            }
        });

        username.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }

                return false;
            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot users = dataSnapshot.child("Users");
                Iterable<DataSnapshot> usersChildren = users.getChildren();
                for (DataSnapshot user: usersChildren) {
                    User u = user.getValue(User.class);
                    if (u != null) {
                        userMap.put(u.getUsername(), u);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (userMap.get(user) == null) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(WelcomeScreenActivity.this);
                    builder1.setMessage("Username not found.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else if (user.equals("")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(WelcomeScreenActivity.this);
                    builder1.setMessage("Must enter in a username.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else if (pass.equals("")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(WelcomeScreenActivity.this);
                    builder1.setMessage("Must enter in a password.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {
                    if (userMap.get(user).getPassword().equals(pass)) {
                        currentUser = user;
                        Intent intent = new Intent(WelcomeScreenActivity.this, HomeScreenActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreenActivity.this, RegistrationScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(WelcomeScreenActivity.this);
            builder1.setMessage("Unable to login with Google.");
            builder1.setCancelable(true);
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                userEmail = account.getEmail();
                String[] temp = userEmail.split("@");
                currentUser = temp[0];
                myRef.child("Google").child(temp[0]).setValue("Email Account");
            }
            Intent intent = new Intent(WelcomeScreenActivity.this, HomeScreenActivity.class);
            startActivity(intent);
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(WelcomeScreenActivity.this);
            builder1.setMessage("Unable to login with Google.");
            builder1.setCancelable(true);
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

}

