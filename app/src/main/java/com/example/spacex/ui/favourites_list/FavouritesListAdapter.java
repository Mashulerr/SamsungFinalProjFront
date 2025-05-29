package com.example.spacex.ui.favourites_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacex.R;
import com.example.spacex.databinding.ItemArticleBinding;
import com.example.spacex.domain.entity.ItemArticleEntity;
import com.example.spacex.domain.entity.ReactionType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FavouritesListAdapter extends RecyclerView.Adapter<FavouritesListAdapter.ViewHolder> {

    @NonNull
    private final Consumer<String> onItemClick;

    @NonNull
    private final Consumer<String> onFavouriteRemoveClick;

    @NonNull
    private final Consumer<String> onLikeClick;

    @NonNull
    private final Consumer<String> onDislikeClick;

    private Map<String, ReactionType> reactionMap = new HashMap<>();

    private final List<ItemArticleEntity> data = new ArrayList<>();

    public FavouritesListAdapter(
            @NonNull Consumer<String> onItemClick,
            @NonNull Consumer<String> onFavouriteRemoveClick,
            @NonNull Consumer<String> onLikeClick,
            @NonNull Consumer<String> onDislikeClick
    ) {
        this.onItemClick = onItemClick;
        this.onFavouriteRemoveClick = onFavouriteRemoveClick;
        this.onLikeClick = onLikeClick;
        this.onDislikeClick = onDislikeClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemArticleBinding.inflate(
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

    public void updateData(List<ItemArticleEntity> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public void setReactionMap(Map<String, ReactionType> newMap) {
        this.reactionMap = newMap;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemArticleBinding binding;

        public ViewHolder(@NonNull ItemArticleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ItemArticleEntity item) {
            binding.articleTitle.setText(item.getTitle());
            binding.userNickname.setText(item.getUsername());
            binding.amountOfDislikes.setText(item.getDislikes().toString());
            binding.amountOfLikes.setText(item.getLikes().toString());

            if (item.getPhotoUrl() != null) {
                Picasso.get().load(item.getPhotoUrl()).into(binding.userImage);
            } else {
                binding.userImage.setImageResource(R.drawable.ic_default_user_avatar);
            }

            binding.favouritesButton.setImageResource(R.drawable.ic_favourites_yellow);
            binding.favouritesButton.setOnClickListener(v -> {
                onFavouriteRemoveClick.accept(item.getId());
            });

            ReactionType reactionType = reactionMap.getOrDefault(item.getId(), ReactionType.none);
            switch (reactionType) {
                case like:
                    binding.likeButton.setImageResource(R.drawable.ic_like_gray);
                    binding.dislikeButton.setImageResource(R.drawable.ic_dislike_transparent);
                    break;
                case dislike:
                    binding.likeButton.setImageResource(R.drawable.ic_like_transparent);
                    binding.dislikeButton.setImageResource(R.drawable.ic_dislike_gray);
                    break;
                case none:
                default:
                    binding.likeButton.setImageResource(R.drawable.ic_like_transparent);
                    binding.dislikeButton.setImageResource(R.drawable.ic_dislike_transparent);
                    break;
            }
            binding.likeButton.setOnClickListener(v -> onLikeClick.accept(item.getId()));
            binding.dislikeButton.setOnClickListener(v -> onDislikeClick.accept(item.getId()));
            binding.getRoot().setOnClickListener(v -> onItemClick.accept(item.getId()));
        }
    }
}
