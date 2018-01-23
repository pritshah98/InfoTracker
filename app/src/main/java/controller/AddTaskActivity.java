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

import model.Task;

public class AddTaskActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Button done = (Button) findViewById(R.id.taskDoneButton);
        Button cancel = (Button) findViewById(R.id.cancelTaskButton);
        final EditText priority = (EditText) findViewById(R.id.enterPriority);
        final EditText task = (EditText) findViewById(R.id.enterTask);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        layout = (ConstraintLayout) findViewById(R.id.cLayout);

        layout.setBackgroundColor(Color.parseColor("#FFFFE0"));

        priority.setOnKeyListener(new View.OnKeyListener() {
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

        task.setOnKeyListener(new View.OnKeyListener() {
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = priority.getText().toString();
                String t = task.getText().toString();
                int p = Integer.parseInt(str);
                if (p < 1 || p > 9) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AddTaskActivity.this);
                    builder1.setMessage("Priority not in range of 1-9.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {
                    Task newTask = new Task(t, p);
                    myRef.child("Tasks").child(WelcomeScreenActivity.currentUser).child(newTask.getItem()).setValue(newTask);
                    HomeScreenActivity.taskList.add(newTask);
                    finish();
                }
            }
        });
    }
}
