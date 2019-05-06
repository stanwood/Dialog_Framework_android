/*
 * Copyright (c) 2018 stanwood Gmbh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.stanwood.framework.dialog;


import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class ImagePreloader implements LifecycleObserver {

    private boolean isDelivered;
    private Lifecycle lifecycle;
    private LoaderCallback callback;
    private int counter = 0;
    private RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            counter--;
            if (counter == 0) {
                enable();
            }
            return false;
        }
    };

    private ImagePreloader(Context context, List<String> urls, Lifecycle lifecycle, LoaderCallback callback) {
        counter = urls.size();
        for (int size = urls.size(), i = 0; i < size; i++) {
            Glide.with(context).load(urls.get(i)).listener(requestListener).preload();
        }
        this.lifecycle = lifecycle;
        this.callback = callback;
        lifecycle.addObserver(this);
    }

    static ImagePreloader create(Context context, List<String> urls, Lifecycle lifecycle, LoaderCallback callback) {
        return new ImagePreloader(context, urls, lifecycle, callback);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destroy() {
        lifecycle.removeObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void resume() {
        enable();
    }

    void enable() {
        if (counter == 0 && !isDelivered && lifecycle.getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
            isDelivered = true;
            callback.onLoadFinished();
        }
    }

    public interface LoaderCallback {
        void onLoadFinished();
    }
}