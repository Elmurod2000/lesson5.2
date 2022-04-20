package com.example.lesson52.ui.form;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lesson52.App;
import com.example.lesson52.R;
import com.example.lesson52.data.models.Post;
import com.example.lesson52.databinding.FragmentFormBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FormFragment extends Fragment {

    private static final int userId = 0;
    private static final int groupId = 41;

    private FragmentFormBinding binding;

    public FormFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFormBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            Post post = (Post) requireArguments().getSerializable("data");
            binding.etTitle.setText(post.getTitle());
            binding.etContent.setText(post.getContent());
        }

        binding.btnSend.setOnClickListener(view1 -> {
            String title = binding.etTitle.getText().toString();
            String content = binding.etContent.getText().toString();
            if (getArguments() != null) {
                Post post = (Post) requireArguments().getSerializable("data");
                post.setTitle(title);
                post.setContent(content);
                updatePost(post);
            } else {
                Post post = new Post(
                        title,
                        content,
                        userId,
                        groupId
                );
                createPost(post);
            }
        });

    }

    private void updatePost(Post post) {
        App.api.updatePost(post.getId(), post).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    requireActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    private void createPost(Post post) {
        App.api.createPost(post).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    requireActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }
}