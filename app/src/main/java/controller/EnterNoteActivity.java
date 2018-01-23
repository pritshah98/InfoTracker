package controller;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pritshah.infotracker.R;

import model.Note;

public class EnterNoteActivity extends AppCompatActivity {

    private EditText note;
    private EditText title;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_note);
        note = (EditText) findViewById(R.id.newNote);
        title = (EditText) findViewById(R.id.titleEdit);
        Button done = (Button) findViewById(R.id.doneButton);
        Button cancel = (Button) findViewById(R.id.cancelButton);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        layout = (ConstraintLayout) findViewById(R.id.cLayout);

        layout.setBackgroundColor(Color.parseColor("#FFFFE0"));

        title.setOnKeyListener(new View.OnKeyListener() {
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

        note.setOnKeyListener(new View.OnKeyListener() {
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

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = note.getText().toString();
                String titleString = title.getText().toString();

                if (titleString.equals("")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(EnterNoteActivity.this);
                    builder1.setMessage("Must enter in a title.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else if (text.equals("")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(EnterNoteActivity.this);
                    builder1.setMessage("Must enter in a note.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {
                    Note n = new Note(titleString, text);
                    myRef.child("Notes").child(WelcomeScreenActivity.currentUser).child(n.toString()).setValue(n);
                    HomeScreenActivity.noteList.add(n);
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
