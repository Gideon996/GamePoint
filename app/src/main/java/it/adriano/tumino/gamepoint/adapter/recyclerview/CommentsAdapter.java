package it.adriano.tumino.gamepoint.adapter.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.Comment;
import it.adriano.tumino.gamepoint.databinding.CommentLayoutBinding;
import it.adriano.tumino.gamepoint.holder.recyclerview.CommentHolder;

public class CommentsAdapter extends RecyclerView.Adapter<CommentHolder> {
    private static final String TAG = "CommentsAdapter";

    private final List<Comment> comments;

    public CommentsAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NotNull
    @Override
    public CommentHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        CommentLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.comment_layout, parent, false);
        return new CommentHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull CommentHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
