package io.stanwood.framework.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Spanned;
import android.text.SpannedString;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import io.stanwood.framework.analytics.generic.AnalyticsTracker;
import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingEvent;
import io.stanwood.framework.content.IntentCreator;

public class RatingDialog extends DialogFragment {
    private List<CharSequence> texts = new ArrayList<>();
    private String bannerUrl;
    private String faceUrl;
    private String cancelText;
    private String okText;
    private AnalyticsTracker analyticsTracker;
    private boolean okPressed = false;

    private static RatingDialog createInstance(Builder builder) {
        Bundle bundle = new Bundle();
        bundle.putCharSequenceArrayList("texts", builder.texts);
        bundle.putString("bannerUrl", builder.bannerUrl);
        bundle.putString("faceUrl", builder.faceUrl);
        bundle.putString("cancelText", builder.cancelText);
        bundle.putString("okText", builder.okText);
        RatingDialog f = new RatingDialog();
        f.setAnalyticsTracker(builder.analyticsTracker);
        f.setArguments(bundle);
        return f;
    }

    public static Builder builder() {
        return new Builder();
    }

    private void processArguments() {
        Bundle b = getArguments();
        if (b != null) {
            texts = b.getCharSequenceArrayList("texts");
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
        setCancelable(false);
        processArguments();
        initViews(view);
        track("rating_dialog_shown");
        Preferences.storeDialogShown(getActivity(), true);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
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

    private void initViews(View view) {
        setTexts(view);
        setImages(view);
        setButtons(view);
    }

    private void setTexts(View view) {
        ViewGroup viewGroup = view.findViewById(R.id.textContainer);
        for (int size = texts.size(), i = 0; i < size; i++) {
            TextView textView = new TextView(getContext(), null, 0, R.style.DialogText);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = i > 0 ? (int) getResources().getDimension(R.dimen.item_bottom_margin) : 0;
            textView.setLayoutParams(lp);
            textView.setText(texts.get(i));
            viewGroup.addView(textView);
        }
    }

    private void setImages(View view) {
        Glide.with(this).load(bannerUrl).into(view.<ImageView>findViewById(R.id.imgBanner));
        Glide.with(this).load(faceUrl).apply(RequestOptions.circleCropTransform()).into(view.<ImageView>findViewById(R.id.imgDeveloper));
    }

    private void setButtons(View view) {
        Button btnOk = view.findViewById(R.id.btn_ok);
        btnOk.setText(okText);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null) {
                    try {
                        Intent intent = IntentCreator.createPlayStoreIntent(activity);
                        activity.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        //rooted phone, no need to do anything here
                    }
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
                if (getShowsDialog()) {
                    getDialog().cancel();
                } else {
                    dismiss();
                }
            }
        });
    }

    private void setAnalyticsTracker(AnalyticsTracker analyticsTracker) {
        this.analyticsTracker = analyticsTracker;
    }

    private void track(String s) {
        if (analyticsTracker != null) {
            analyticsTracker.trackEvent(TrackerParams.builder(TrackingEvent.VIEW_ITEM).setName(s).build());
        }
    }

    public static final class Builder {
        private ArrayList<CharSequence> texts = new ArrayList<>();
        private String bannerUrl;
        private String faceUrl;
        private String cancelText;
        private String okText;
        private AnalyticsTracker analyticsTracker;

        private Builder() {
        }

        private static Spanned formatNewLines(@Nullable String s) {
            return new SpannedString(s != null ? s.replaceAll("\\\\n", "\n") : "");
        }

        public Builder addParagraph(CharSequence val) {
            texts.add(val);
            return this;
        }

        public Builder addParagraph(String val) {
            addParagraph(formatNewLines(val));
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

        public Builder setAnalyticsTracker(AnalyticsTracker analyticsTracker) {
            this.analyticsTracker = analyticsTracker;
            return this;
        }

        public RatingDialog build() {
            return RatingDialog.createInstance(this);
        }

        public void preloadAndShow(final FragmentActivity activity) {
            if (activity.getSupportFragmentManager().findFragmentByTag("stanwood_rating_dialog") != null) {
                return;
            }
            List<String> lst = new ArrayList<>();
            lst.add(bannerUrl);
            lst.add(faceUrl);
            ImagePreloader.create(activity, lst, activity.getLifecycle(), new ImagePreloader.LoaderCallback() {
                @Override
                public void onLoadFinished() {
                    RatingDialog.createInstance(Builder.this).show(activity.getSupportFragmentManager(), "stanwood_rating_dialog");
                }
            });
        }
    }
}