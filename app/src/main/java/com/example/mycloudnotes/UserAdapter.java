package com.example.mycloudnotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycloudnotes.R;
import com.example.mycloudnotes.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private Context mContext;
    private List<User> mDataList;

    public UserAdapter(Context mContext, List<User> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.design_note,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user=mDataList.get(position);
        holder.mytitle.setText(user.getTitle());
        holder.mydesc.setText(user.getDesc());
        String key=user.getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goEditNote=new Intent(mContext,EditNoteActivity.class);
                goEditNote.putExtra("title",user.getTitle());
                goEditNote.putExtra("desc",user.getDesc());
                goEditNote.putExtra("noteid",user.getId());
                mContext.startActivity(goEditNote);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mytitle;
        TextView mydesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mytitle=itemView.findViewById(R.id.xtitle);
            mydesc=itemView.findViewById(R.id.xdescription);
        }
    }

}