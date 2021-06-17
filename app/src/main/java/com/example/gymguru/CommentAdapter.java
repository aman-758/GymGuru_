package com.example.gymguru;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private ArrayList<CommentModel> commentModels;
    private Context ctx;
    public CommentAdapter(Context ctx, ArrayList<CommentModel> commentModels) {
        this.commentModels = commentModels;
        this.ctx = ctx;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CommentModel cmnt = commentModels.get(position);
        holder.bind(cmnt,position);
    }
    @Override
    public int getItemCount() {
        return commentModels.size();
    }
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(ctx).inflate(R.layout.comment_single_row,
                parent,false);
        return new MyViewHolder(mItemView);
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cname, cmsg, cdate_time;
        ImageView cimage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cname = itemView.findViewById(R.id.cname);
            cmsg = itemView.findViewById(R.id.cmsg);
            cdate_time = itemView.findViewById(R.id.cdate_time);
            cimage = itemView.findViewById(R.id.cimage);
        }
        @SuppressLint("SetTextI18n")
        public void bind(CommentModel commentModel, int position){
            cname.setText(commentModel.getUsername()); //(it runs)
            cmsg.setText(commentModel.getUsermsg());
            cdate_time.setText("Date :"+commentModel.getDate()+" Time :"+commentModel.getTime());
            Glide.with(ctx).load(commentModel.getImageUrl())
                    .centerCrop().centerCrop().placeholder(R.drawable.ic_baseline_account_circle_24).into(cimage); //(it runs)
        }
    }
}
