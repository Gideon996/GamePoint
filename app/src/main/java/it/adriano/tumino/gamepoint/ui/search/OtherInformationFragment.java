package it.adriano.tumino.gamepoint.ui.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.internal.StringUtil;

import it.adriano.tumino.gamepoint.R;
import it.adriano.tumino.gamepoint.data.GameShow;

public class OtherInformationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String mParam1;
    private String mParam2;
    private GameShow mParam3;

    public OtherInformationFragment() {
    }

    public static OtherInformationFragment newInstance(String param1, String param2, GameShow param3) {
        OtherInformationFragment fragment = new OtherInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putParcelable(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getParcelable(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_information, container, false);
        LinearLayout descrizioneLayout = view.findViewById(R.id.descrizioneLayout);
        LinearLayout specificheLayout = view.findViewById(R.id.specificheLayout);
        LinearLayout commentiLayout = view.findViewById(R.id.commentiLayout);

        switch (mParam1) {
            case "Descrizione":
                descrizioneLayout.setVisibility(View.VISIBLE);
                specificheLayout.setVisibility(View.GONE);
                commentiLayout.setVisibility(View.GONE);

                TextView sviluppatori = view.findViewById(R.id.sviluppatoriTextVIew);
                sviluppatori.setText(StringUtil.join(mParam3.getDevelopers(), ", "));

                TextView dev = view.findViewById(R.id.publisherTextView);
                dev.setText(StringUtil.join(mParam3.getPublishers(), ", "));
                TextView descrizione = view.findViewById(R.id.descrizioneTextView);
                String text = mParam3.getDescription();
                descrizione.setText(Html.fromHtml(mParam3.getDescription(), Html.FROM_HTML_MODE_COMPACT));
                break;
            case "Specifiche":
                descrizioneLayout.setVisibility(View.GONE);
                specificheLayout.setVisibility(View.VISIBLE);
                commentiLayout.setVisibility(View.GONE);

                TextView specificheWindows = view.findViewById(R.id.specificheWindows);
                specificheWindows.setText((mParam3.getMinimumWindows().isEmpty() || mParam3.getMinimumWindows().equals("[]")) ? "Non supportato" : Html.fromHtml(mParam3.getMinimumWindows(), Html.FROM_HTML_MODE_COMPACT));

                TextView specificheMac = view.findViewById(R.id.specificheMac);
                specificheMac.setText((mParam3.getMinimumMac().isEmpty() || mParam3.getMinimumLinux().equals("[]")) ? "Non supportato" : Html.fromHtml(mParam3.getMinimumMac(), Html.FROM_HTML_MODE_COMPACT));

                TextView specificheLinux = view.findViewById(R.id.specificheLinux);
                specificheLinux.setText((mParam3.getMinimumLinux().isEmpty() || mParam3.getMinimumLinux().equals("[]")) ? "Non supportato" : Html.fromHtml(mParam3.getMinimumLinux(), Html.FROM_HTML_MODE_COMPACT));
                break;
            case "Commenti":
                descrizioneLayout.setVisibility(View.GONE);
                specificheLayout.setVisibility(View.GONE);
                commentiLayout.setVisibility(View.VISIBLE);
                break;
        }

        return view;
    }

}