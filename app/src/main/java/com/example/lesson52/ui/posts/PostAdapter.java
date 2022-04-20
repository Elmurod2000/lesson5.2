package com.example.lesson52.ui.posts;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lesson52.data.models.Post;
import com.example.lesson52.databinding.ItemPostBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts = new ArrayList<>();
    private OnClickPost clickPost;
    private HashMap<Integer, String> hashMap = new HashMap<>();

    public void setClickPost(OnClickPost clickPost) {
        this.clickPost = clickPost;
    }

    public void deletePost(int pos) {
        posts.remove(pos);
        notifyItemRemoved(pos);
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public Post getItem(int pos) {
        return posts.get(pos);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false

        );
        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.onBind(posts.get(position));
        holder.hashMap();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    protected class PostViewHolder extends RecyclerView.ViewHolder {

        private ItemPostBinding binding;

        public PostViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(Post post) {
            binding.tvUserId.setText(hashMap.get(post.getUserId()));
            binding.tvTitle.setText(post.getTitle());
            binding.tvContent.setText(post.getContent());

            binding.getRoot().setOnClickListener(view -> {
                clickPost.onClick(post);
            });

            itemView.setOnLongClickListener(view -> {
                clickPost.onLongClick(getAdapterPosition());
                return true;
            });
        }

        public void hashMap() {
            hashMap.put(0, "Elmurod");
        }
    }

    interface OnClickPost {
        void onClick(Post post);

        void onLongClick(int pos);
    }
}
