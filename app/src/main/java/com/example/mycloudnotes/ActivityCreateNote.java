package com.example.mycloudnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityCreateNote extends AppCompatActivity {

    EditText mtitle;
    EditText mdescription;
    Button create_note, cancel;

    FirebaseDatabase mDatabase;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("users");

        mtitle = findViewById(R.id.title);
        mdescription = findViewById(R.id.description);
        create_note = findViewById(R.id.create_note);
        cancel = findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome = new Intent(ActivityCreateNote.this, MainActivity.class);
                startActivity(backHome);
            }
        });

        create_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mtitle.getText().toString();
                String desc = mdescription.getText().toString();
                User user = new User(title, desc);
                String key = myRef.push().getKey();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc)) {
                    myRef.child(key).setValue(user);
                    mtitle.setText(null);
                    mdescription.setText(null);
                    Intent goHome = new Intent(ActivityCreateNote.this, MainActivity.class);
                    startActivity(goHome);
                    finish();
                } else {
                    Snackbar.make(v, "Empty fields", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }
}