/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dignsys.dsdsp.dsdsp_9100.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;




public class ImageLoader {
    private static final String TAG = ImageLoader.class.getSimpleName();
    private static final ModelCache<String, GlideUrl> urlCache = new ModelCache<>(150);

   // private final BitmapTypeRequest<String> mGlideModelRequest;
   // private final CenterCrop mCenterCrop;

    private int mPlaceHolderResId = -1;

    /**
     * Construct a standard ImageLoader object.
     */
  /*  public ImageLoader(Context context) {
        VariableWidthImageLoader imageLoader = new VariableWidthImageLoader(context);
        mGlideModelRequest = Glide.with(context).using(imageLoader).from(String.class).asBitmap();
        mCenterCrop = new CenterCrop(Glide.get(context).getBitmapPool());
    }
*/
    /**
     * Construct an ImageLoader with a default placeholder drawable.
     */
/*    public ImageLoader(Context context, int placeHolderResId) {
        this(context);
        mPlaceHolderResId = placeHolderResId;
    }*/

/*    public void loadImage(String url, ImageView imageView, RequestListener<String, Bitmap> requestListener) {
        loadImage(url, imageView, requestListener, null, false);
    }*/


   /* public void loadImage(String url, ImageView imageView, RequestListener<String, Bitmap> requestListener,
            Drawable placeholderOverride) {
        loadImage(url, imageView, requestListener, placeholderOverride, false *//*crop*//*);
    }*/


   /* public void loadImage(String url, ImageView imageView, RequestListener<String, Bitmap> requestListener,
                Drawable placeholderOverride, boolean crop) {
        BitmapRequestBuilder request = beginImageLoad(url, requestListener, crop)
                .animate(R.anim.image_fade_in);
        if (placeholderOverride != null) {
            request.placeholder(placeholderOverride);
        } else if (mPlaceHolderResId != -1) {
            request.placeholder(mPlaceHolderResId);
        }
        request.into(imageView);
    }

    public BitmapRequestBuilder beginImageLoad(String url,
            RequestListener<String, Bitmap> requestListener, boolean crop) {
        if (crop){
            return mGlideModelRequest.load(url)
                    .listener(requestListener)
                    .transform(mCenterCrop);
        } else {
            return mGlideModelRequest.load(url)
                    .listener(requestListener);
        }
    }
*/
 /*
    public void loadImage(String url, ImageView imageView) {
        loadImage(url, imageView, false *//*crop*//*);
    }*/



   /* public void loadImage(String url, ImageView imageView, boolean crop) {
        loadImage(url, imageView, null, null, crop);
    }*/

    public void loadImage(Context context, @DrawableRes int drawableResId, ImageView imageView) {
        Glide.with(context).load(drawableResId).into(imageView);
    }

    /*private static class VariableWidthImageLoader extends BaseGlideUrlLoader<String> {
        private static final Pattern PATTERN = Pattern.compile("__w-((?:-?\\d+)+)__");

        public VariableWidthImageLoader(Context context) {
            super(context, urlCache);
        }

        *//**
         * If the URL contains a special variable width indicator (eg "__w-200-400-800__")
         * we get the buckets from the URL (200, 400 and 800 in the example) and replace
         * the URL with the best bucket for the requested width (the bucket immediately
         * larger than the requested width).
         *//*
        @Override
        protected String getUrl(String model, int width, int height) {
            Matcher m = PATTERN.matcher(model);
            int bestBucket = 0;
            if (m.find()) {
                String[] found = m.group(1).split("-");
                for (String bucketStr : found) {
                    bestBucket = Integer.parseInt(bucketStr);
                    if (bestBucket >= width) {
                        // the best bucket is the first immediately bigger than the requested width
                        break;
                    }
                }
                if (bestBucket > 0) {
                    model = m.replaceFirst("w"+bestBucket);
                }
            }
            return model;
        }
    }*/
}
