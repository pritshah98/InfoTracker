package controller;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.pritshah.infotracker.R;

import model.Note;

public class EditNoteActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter adapter;
    public static Note n = null;
    public static int index = 0;
    private ConstraintLayout layout;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        listView = (ListView) findViewById(R.id.viewNotesList);
        Button back = (Button) findViewById(R.id.deleteCancelButton);
        layout = (ConstraintLayout) findViewById(R.id.cLayout);

        layout.setBackgroundColor(Color.parseColor("#FFFFE0"));

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, HomeScreenActivity.noteList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                for (int i = 0; i < HomeScreenActivity.noteList.size(); i++) {
                    if (pos == i) {
                        index = i;
                        n = HomeScreenActivity.noteList.get(i);
                        Intent intent = new Intent(EditNoteActivity.this, ItemClickedActivity.class);
                        startActivityForResult(intent, 0);
                        finish();
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

}
