package com.example.spacex.ui.article_and_comments.comments_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacex.R;
import com.example.spacex.databinding.ItemCommentBinding;
import com.example.spacex.domain.entity.CommentEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder>{

    private final List<CommentEntity> data = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemCommentBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    public void updateData(List<CommentEntity> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ItemCommentBinding binding;

        public ViewHolder(@NonNull ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CommentEntity item){
            binding.commentContent.setText(item.getContent());
            binding.userNickname.setText(item.getUsername());
            if (item.getPhotoUrl() != null) {
                Picasso.get().load(item.getPhotoUrl()).into(binding.userImage);
            } else {
                binding.userImage.setImageResource(R.drawable.ic_default_user_avatar);
            }
        }
    }

}
