package controller;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pritshah.infotracker.R;

import java.util.Collections;

public class ViewTaskActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter adapter;
    private int index = 0;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ConstraintLayout layout;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Button back = (Button) findViewById(R.id.viewTaskBackButton);
        listView = (ListView) findViewById(R.id.viewTaskListItems);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        layout = (ConstraintLayout) findViewById(R.id.cLayout);

        layout.setBackgroundColor(Color.parseColor("#FFFFE0"));

        Collections.sort(HomeScreenActivity.taskList);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, HomeScreenActivity.taskList);
        listView.setAdapter(adapter);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        delete(index);

                    case DialogInterface.BUTTON_NEGATIVE:
                        //DO NOTHING
                        dialog.dismiss();
                }
            }
        };

        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewTaskActivity.this);
        builder.setMessage("Do you wish to delete this task?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                for (int i = 0; i < HomeScreenActivity.taskList.size(); i++) {
                    if (pos == i) {
                        index = i;
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void delete(int index) {
        myRef.child("Tasks").child(WelcomeScreenActivity.currentUser)
                .child(HomeScreenActivity.taskList.get(index).getItem()).removeValue();
        HomeScreenActivity.taskList.remove(index);
        finish();
    }
}
