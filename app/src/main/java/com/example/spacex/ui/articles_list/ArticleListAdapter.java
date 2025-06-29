package com.example.spacex.ui.articles_list;

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
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {

    @NonNull
    private final Consumer<String> onItemClick;

    @NonNull
    private final BiConsumer<String, Boolean> onFavouriteClick;

    @NonNull
    private final Consumer<String> onLikeClick;

    @NonNull
    private final Consumer<String> onDislikeClick;


    private final List<ItemArticleEntity> data = new ArrayList<>();

    private Map<String, ReactionType> reactionMap = new HashMap<>();

    public ArticleListAdapter(
            @NonNull Consumer<String> onItemClick,
            @NonNull BiConsumer<String, Boolean> onFavouriteClick,
            @NonNull Consumer<String> onLikeClick,
            @NonNull Consumer<String> onDislikeClick
    ) {
        this.onItemClick = onItemClick;
        this.onFavouriteClick = onFavouriteClick;
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

    @Override
    public int getItemCount() {
        return data.size();
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

            if (item.isFavourite()) {
                binding.favouritesButton.setImageResource(R.drawable.ic_favourites_yellow);
            } else {
                binding.favouritesButton.setImageResource(R.drawable.ic_favourites_transparent);
            }

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

            binding.getRoot().setOnClickListener(v -> onItemClick.accept(item.getId()));

            binding.favouritesButton.setOnClickListener(v -> {
                onFavouriteClick.accept(item.getId(), item.isFavourite());
            });

            binding.likeButton.setOnClickListener(v -> onLikeClick.accept(item.getId()));
            binding.dislikeButton.setOnClickListener(v -> onDislikeClick.accept(item.getId()));

        }
    }
}
