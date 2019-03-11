package io.stanwood.framework.dialog;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.stanwood.framework.analytics.BaseAnalyticsTracker;
import io.stanwood.framework.base.Intents;

public class RatingDialog extends DialogFragment {

    private String text1;
    private String text2;
    private String text3;
    private String text4;
    @IdRes private int bannerRes;
    private String bannerUrl;
    @IdRes private int faceRes;
    private String faceUrl;
    private String cancelText;
    private String okText;
    private BaseAnalyticsTracker analyticsTracker;
    private boolean okPressed = false;

    public static RatingDialog createInstance(Builder builder) {
        RatingDialog f = createInstance(
                builder.text1,
                builder.text2,
                builder.text3,
                builder.text4,
                builder.bannerUrl,
                builder.faceUrl,
                builder.cancelText,
                builder.okText);
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

    public static Builder builder() {
        return new Builder();
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
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20);
            }
        });
        view.setClipToOutline(true);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Preferences.storeDialogShown(getActivity(), true);
        setCancelable(false);
        processArguments();
        initViews(view);
        track("rating_dialog_shown");
        return view;
    }

    private void initViews(View view) {
        //set texts
        ((TextView) view.findViewById(R.id.txt1)).setText(text1);
        ((TextView) view.findViewById(R.id.txt2)).setText(text2);
        ((TextView) view.findViewById(R.id.txt3)).setText(text3);
        ((TextView) view.findViewById(R.id.txt4)).setText(text4);

        //head images
        final ImageView imgShadow = view.findViewById(R.id.imgShadow);
        imgShadow.setClipToOutline(true);
        imgShadow.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, imgShadow.getWidth(), imgShadow.getHeight());
            }
        });
        final ImageView imgDeveloper = view.findViewById(R.id.imgDeveloper);
        imgDeveloper.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, imgDeveloper.getWidth(), imgDeveloper.getHeight());
            }
        });
        imgDeveloper.setClipToOutline(true);
        ImageView imgBanner = view.findViewById(R.id.imgBanner);
        Glide.with(this).load(bannerUrl).into(imgBanner);
        Glide.with(this).load(faceUrl).into(imgDeveloper);

        Button btnOk = view.findViewById(R.id.btn_ok);
        btnOk.setText(okText);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null) {
                    Intents.INSTANCE.createPlayStoreIntent(activity);
                    Preferences.storeRated(activity, true);
                }
                okPressed = true;
                dismiss();
            }
        });

        Button btnCancel = view.findViewById(R.id.btn_cancel);
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
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        track(okPressed ? "rating_dialog_yes_pressed" : "rating_dialog_no_pressed");
        analyticsTracker = null;
    }

    public void setAnalytics(@Nullable BaseAnalyticsTracker baseAnalyticsTracker) {
        analyticsTracker = baseAnalyticsTracker;
    }

    private void track(String s) {
        if (analyticsTracker != null) {
            analyticsTracker.trackScreenView(s);
        }
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

        public Builder setParagraph1(String val) {
            text1 = val;
            return this;
        }

        public Builder setParagraph2(String val) {
            text2 = val;
            return this;
        }

        public Builder setParagraph3(String val) {
            text3 = val;
            return this;
        }

        public Builder setParagraph4(String val) {
            text4 = val;
            return this;
        }

        public Builder setCancelText(String val) {
            cancelText = val.toUpperCase();
            return this;
        }

        public Builder setOkText(String val) {
            okText = val.toUpperCase();
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