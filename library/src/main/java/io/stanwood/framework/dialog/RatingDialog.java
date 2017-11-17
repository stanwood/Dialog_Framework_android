package io.stanwood.framework.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.stanwood.framework.dialog.glide.GlideApp;
import static io.stanwood.framework.dialog.glide.CropCircleTransformation.circleCrop;

public class RatingDialog extends DialogFragment {

    private final String LOG_TAG = RatingDialog.class.getSimpleName();

    String text1;
    String text2;
    String text3;
    String text4;
    String bannerUrl;
    String faceUrl;
    String cancelText;
    String okText;

    public static RatingDialog createInstance(Builder builder) {
        RatingDialog f = createInstance(builder.text1, builder.text2, builder.text3, builder.text4,
                builder.bannerUrl, builder.faceUrl, builder.cancelText, builder.okText);
        return f;
    }

    public static RatingDialog createInstance(String txt1, String txt2, String txt3, String txt4,
                                              String bannerUrl, String faceUrl,
                                              String cancelText, String okText) {
        Bundle bundle = new Bundle();
        bundle.putString("txt1", txt1);
        bundle.putString("txt2", txt2);
        bundle.putString("txt3", txt3);
        bundle.putString("txt4", txt4);
        bundle.putString("bannerUrl", bannerUrl);
        bundle.putString("faceUrl", faceUrl);
        bundle.putString("cancelText", cancelText);
        bundle.putString("okText", okText);
        RatingDialog f = new RatingDialog();
        f.setArguments(bundle);
        return f;
    }

    private void processArguments() {
        Bundle b = getArguments();
        if (b != null) {
            text1 = b.getString("txt1");
            text2 = b.getString("txt2");
            text3 = b.getString("txt3");
            text4 = b.getString("txt4");
            bannerUrl = b.getString("bannerUrl");
            faceUrl = b.getString("faceUrl");
            cancelText = b.getString("cancelText");
            okText = b.getString("okText");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_dialog, container, false);

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();


        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setCancelable(false);

        processArguments();

        initViews(view);

        return view;
    }

    private void initViews(View view) {

        //set texts
        ((AppCompatTextView) view.findViewById(R.id.txt1)).setText(text1);
        ((AppCompatTextView) view.findViewById(R.id.txt2)).setText(text2);
        ((AppCompatTextView) view.findViewById(R.id.txt3)).setText(text3);
        ((AppCompatTextView) view.findViewById(R.id.txt4)).setText(text4);

        //load images
        AppCompatImageView imgBanner = view.findViewById(R.id.imgBanner);
        AppCompatImageView imgDeveloper = view.findViewById(R.id.imgDeveloper);
        GlideApp.with(this).load(bannerUrl).into(imgBanner);
        GlideApp.with(this).load(faceUrl).transform(circleCrop()).into(imgDeveloper);

        AppCompatTextView btnOk = view.findViewById(R.id.btn_ok);
        btnOk.setText(okText);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = IntentHelper.createIntent(getActivity());
                getActivity().startActivity(intent);
                Preferences.storeRated(getActivity(),true);
                dismiss();
            }
        });

        AppCompatTextView btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setText(cancelText);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getShowsDialog())
                    getDialog().cancel();
                else
                    dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getShowsDialog()) {
            DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
            int dialogWidth = Math.min(metrics.widthPixels, metrics.heightPixels);
            float margin = getResources().getDimension(R.dimen.dialog_margin);
            getDialog().getWindow().setLayout(dialogWidth - (int) (margin * 2), android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.v(LOG_TAG, "onCancel");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.v(LOG_TAG, "onDismiss");
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String text1;
        private String text2;
        private String text3;
        private String text4;
        private String bannerUrl;
        private String faceUrl;
        private String cancelText;
        private String okText;

        private Builder() {
        }

        public Builder setText1(String val) {
            text1 = val;
            return this;
        }

        public Builder setText2(String val) {
            text2 = val;
            return this;
        }

        public Builder setText3(String val) {
            text3 = val;
            return this;
        }

        public Builder setText4(String val) {
            text4 = val;
            return this;
        }

        public Builder setCancelText(String val) {
            cancelText = val;
            return this;
        }

        public Builder setOkText(String val) {
            okText = val;
            return this;
        }

        public Builder setBannerUrl(String val) {
            bannerUrl = val;
            return this;
        }

        public Builder setFaceUrl(String val) {
            faceUrl = val;
            return this;
        }

        public RatingDialog build() {
            return RatingDialog.createInstance(this);
        }
    }
}