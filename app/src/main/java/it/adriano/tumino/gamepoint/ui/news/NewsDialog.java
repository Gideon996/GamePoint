package it.adriano.tumino.gamepoint.ui.news;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.News;
import it.adriano.tumino.gamepoint.utils.Utils;

public class NewsDialog extends DialogFragment {
    private final static String TAG = "NewsDialog";

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
            Log.i(TAG, "Start Action View Intent");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getNewsUrl()));
            requireContext().startActivity(intent);
        });

        copyLink.setOnClickListener(v -> {
            Log.i(TAG, "Copy on clipboard");
            ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("News Url", news.getNewsUrl());
            clipboard.setPrimaryClip(clip);
            Objects.requireNonNull(getDialog()).dismiss();
            Toast.makeText(getContext(), R.string.copy_on_clipboard, Toast.LENGTH_SHORT).show();
        });

        shareNews.setOnClickListener(v -> {
            Log.i(TAG, "Share the content");
            Utils.shareContent(getContext(), news.getImageURL(), news.getTitle() + ": " + news.getNewsUrl());
            Objects.requireNonNull(getDialog()).dismiss();
        });

        return builder.create();
    }
}
