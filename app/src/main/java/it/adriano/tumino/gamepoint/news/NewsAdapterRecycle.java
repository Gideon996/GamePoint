package it.adriano.tumino.gamepoint.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.ui.news.NewsViewModel;

public class NewsAdapterRecycle extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<GameNews> news;
    private final Activity activity;
    private int currentPage;
    private final NewsViewModel newsViewModel;

    private static final int TYPE_FOOTER = 1;

    public NewsAdapterRecycle(ArrayList<GameNews> news, Activity activity, int currentPage, NewsViewModel newsViewModel) {
        this.news = news;
        this.activity = activity;
        this.currentPage = currentPage;
        this.newsViewModel = newsViewModel;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_layout, parent, false);
            return new ViewHolderContainer.FooterViewHolder(itemView);
        }
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_news_row, parent, false);
        return new ViewHolderContainer.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderContainer.FooterViewHolder) {
            ViewHolderContainer.FooterViewHolder footerViewHolder = (ViewHolderContainer.FooterViewHolder) holder;
            Button visualizzaAltro = footerViewHolder.button;
            visualizzaAltro.setOnClickListener(v -> {
                currentPage++;
                CatchAndShowNews update = new CatchAndShowNews(newsViewModel, this);
                update.execute(currentPage, currentPage);
            });
        } else if (holder instanceof ViewHolderContainer.ItemViewHolder) {
            ViewHolderContainer.ItemViewHolder itemViewHolder = (ViewHolderContainer.ItemViewHolder) holder;
            final GameNews gameNews = news.get(position);
            itemViewHolder.title.setText(gameNews.getTitle());
            itemViewHolder.description.setText(gameNews.getDescription());
            itemViewHolder.data.setText(gameNews.getData());
            itemViewHolder.sito.setText(gameNews.getSito());
            Glide.with(itemViewHolder.context).load(gameNews.getImageUrl()).placeholder(new ColorDrawable(Color.CYAN)).fitCenter().into(itemViewHolder.imageView);
            itemViewHolder.relativeLayout.setOnClickListener(v -> {
                String url = gameNews.getNewsUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                activity.startActivity(intent);
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == news.size()) {
            return TYPE_FOOTER;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return news.size() + 1;
    }
}
