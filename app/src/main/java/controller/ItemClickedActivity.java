package controller;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
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

public class ItemClickedActivity extends AppCompatActivity {

    private EditText title;
    private EditText note;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_clicked);
        Button cancel = (Button) findViewById(R.id.exitEditing);
        Button done = (Button) findViewById(R.id.finishEditing);
        title = (EditText) findViewById(R.id.editTitle);
        note = (EditText) findViewById(R.id.editNote);
        title.setText(EditNoteActivity.n.getTitle());
        note.setText(EditNoteActivity.n.getNote());
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        layout = (ConstraintLayout) findViewById(R.id.cLayout);

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

        layout.setBackgroundColor(Color.parseColor("#FFFFE0"));

        myRef.child("Notes").child(WelcomeScreenActivity.currentUser)
                .child(EditNoteActivity.n.getTitle()).removeValue();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String changedNote = note.getText().toString();
                String changedTitle = title.getText().toString();

                HomeScreenActivity.noteList.get(EditNoteActivity.index).setNote(changedNote);
                HomeScreenActivity.noteList.get(EditNoteActivity.index).setTitle(changedTitle);

                myRef.child("Notes").child(WelcomeScreenActivity.currentUser)
                        .child(HomeScreenActivity.noteList.get(EditNoteActivity.index).toString())
                        .setValue(HomeScreenActivity.noteList.get(EditNoteActivity.index));
                finish();
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
