package it.adriano.tumino.gamepoint.ui.news;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.utils.Utils;

public class NewsDialog extends DialogFragment {

    private final News news;

    public NewsDialog(News news) {
        this.news = news;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.news_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);

        LinearLayout openNews = view.findViewById(R.id.openNewsLayout);
        LinearLayout copyLink = view.findViewById(R.id.copyNewsLayout);
        LinearLayout shareNews = view.findViewById(R.id.shareNewsLayout);

        openNews.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getNewsUrl()));
            requireContext().startActivity(intent);
        });

        copyLink.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("News Url", news.getNewsUrl());
            clipboard.setPrimaryClip(clip);
            getDialog().dismiss();
            Toast.makeText(getContext(), "Url copiato negli appunti", Toast.LENGTH_SHORT).show();
        });

        shareNews.setOnClickListener(v -> {
            Utils.shareContent(getContext(), news.getImageURL(), news.getTitle() + ": " + news.getNewsUrl());
            getDialog().dismiss();
        });

        return builder.create();
    }
}
