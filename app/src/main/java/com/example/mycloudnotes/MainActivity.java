package com.example.mycloudnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.ims.ImsMmTelManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.snackbar.SnackbarContentLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView username;
    private CircleImageView username_img;
    private EditText search;
    private LottieAnimationView empty;
    private List<User> mDataList;
    private UserAdapter mUserAdapter;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;


    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("users");

        recyclerView = findViewById(R.id.recyclerview);
        username = findViewById(R.id.Username);
        username_img = findViewById(R.id.Username_img);
        search = findViewById(R.id.search);
        empty = findViewById(R.id.empty);


        floatingActionButton = findViewById(R.id.add_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityCreateNote.class);
                startActivity(intent);
            }
        });

        username_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goProfile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(goProfile);
            }
        });

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            String person_name = account.getDisplayName();
            String name = "Hello.. " + person_name;
            username.setText(name);
            Uri person_img = account.getPhotoUrl();
            Glide.with(this).load(person_img).into(username_img);
        }
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showAll();

    }

    public void searchData(View view) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
            Query query = ref.orderByChild("title").equalTo(search.getText().toString());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mDataList.clear();
                    for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                        User user = postSnapShot.getValue(User.class);
                        mDataList.add(user);
                    }
                    mUserAdapter = new UserAdapter(MainActivity.this, mDataList);
                    recyclerView.setAdapter(mUserAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    mUserAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().isEmpty()) {
                        Refresh(1000);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

    private void Refresh(int time) {
        Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                showAll();
            }
        };
        handler.postDelayed(runnable, time);
    }

    private void showAll() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setId(dataSnapshot.getKey());
                    mDataList.add(user);
                }
                mUserAdapter = new UserAdapter(MainActivity.this, mDataList);
                recyclerView.setAdapter(mUserAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                mUserAdapter.notifyDataSetChanged();

                if (mUserAdapter.getItemCount() == 0) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}