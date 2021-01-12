package com.example.mycloudnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditNoteActivity extends AppCompatActivity {

    EditText title_edit, description_edit;
    Button edit_note, cancel_note;
    DatabaseReference reference;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        v = findViewById(R.id.content);

        title_edit = findViewById(R.id.title_edit);
        description_edit = findViewById(R.id.description_edit);
        edit_note = findViewById(R.id.edit_note);
        cancel_note = findViewById(R.id.cancel_note);

        title_edit.setText(getIntent().getStringExtra("title"));
        description_edit.setText(getIntent().getStringExtra("desc"));
        String key = getIntent().getStringExtra("noteid");

        edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if ((networkInfo == null) || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

                    Snackbar.make(v, "No Internet", Snackbar.LENGTH_SHORT).show();
                }

                    reference = FirebaseDatabase.getInstance().getReference("users").child(key);
                    final Map<String, Object> notemap = new HashMap<>();
                    notemap.put("title", title_edit.getText().toString());
                    notemap.put("desc", description_edit.getText().toString());

                    reference.updateChildren(notemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditNoteActivity.this, "Note Edited", Toast.LENGTH_SHORT).show();
                                Intent goHome = new Intent(EditNoteActivity.this, MainActivity.class);
                                overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
                                startActivity(goHome);
                                finish();

                            } else {
                                if (task.getException() != null) {
                                    Toast.makeText(EditNoteActivity.this, "ERROR:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

            }
        });

        cancel_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("users").child(key);
                ref.removeValue();
                Toast.makeText(EditNoteActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                Intent backtoHome = new Intent(EditNoteActivity.this, MainActivity.class);
                startActivity(backtoHome);
            }
        });
    }
}