package com.example.lesson52.ui.posts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.lesson52.App;
import com.example.lesson52.R;
import com.example.lesson52.data.models.Post;
import com.example.lesson52.databinding.FragmentPostsBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsFragment extends Fragment {

    private FragmentPostsBinding binding;
    private PostAdapter adapter;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PostAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentPostsBinding.inflate(
                inflater,
                container,
                false
        );
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setAdapter(adapter);

        binding.btnOpen.setOnClickListener(view1 -> navController.navigate(R.id.formFragment));

        adapter.setClickPost(new PostAdapter.OnClickPost() {
            @Override
            public void onClick(Post post) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", post);
                navController.navigate(R.id.formFragment, bundle);
            }

            @Override
            public void onLongClick(int pos) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                alertDialog.setPositiveButton("yes", (dialogInterface, i)
                        -> App.api.deletePost(adapter.getItem(pos).getId()).enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            adapter.deletePost(pos);
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {

                    }
                })).setNegativeButton("NO", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).show();

            }

        });

        App.api.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setPosts(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });

    }
}
