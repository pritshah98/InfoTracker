package controller;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pritshah.infotracker.R;

import java.util.ArrayList;
import java.util.Collections;

import model.Note;
import model.Task;

public class HomeScreenActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public static ArrayList<Note> noteList;
    public static ArrayList<Task> taskList;
    private ConstraintLayout layout;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Button logout = (Button) findViewById(R.id.logoutButton);
        Button note = (Button) findViewById(R.id.noteButton);
        Button edit = (Button) findViewById(R.id.editNoteButton);
        Button delete = (Button) findViewById(R.id.deleteNoteButton);
        Button addTask = (Button) findViewById(R.id.addTaskButton);
        Button viewTask = (Button) findViewById(R.id.viewTaskList);
        noteList = new ArrayList<>();
        taskList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        layout = (ConstraintLayout) findViewById(R.id.cLayout);

        layout.setBackgroundColor(Color.parseColor("#FFFFE0"));

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot notes = dataSnapshot.child("Notes");
                DataSnapshot user = notes.child(WelcomeScreenActivity.currentUser);
                for (DataSnapshot value: user.getChildren()) {
                    Note n = value.getValue(Note.class);
                    if (!noteList.contains(n)) {
                        noteList.add(n);
                    }
                }
                DataSnapshot tasks = dataSnapshot.child("Tasks");
                DataSnapshot tasksUser = tasks.child(WelcomeScreenActivity.currentUser);
                for (DataSnapshot value: tasksUser.getChildren()) {
                    Task t = value.getValue(Task.class);
                    if (!taskList.contains(t)) {
                        taskList.add(t);
                    }
                }
                if (taskList.size() > 0) {
                    Collections.sort(HomeScreenActivity.taskList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        viewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, ViewTaskActivity.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, EditNoteActivity.class);
                startActivity(intent);
            }
        });

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, EnterNoteActivity.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, DeleteNoteActivity.class);
                startActivity(intent);
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        finish();
                    }
                });
    }
}
