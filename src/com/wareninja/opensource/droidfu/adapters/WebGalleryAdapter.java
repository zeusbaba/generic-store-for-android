/* Copyright (c) 2009 Matthias Kaeppler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wareninja.opensource.droidfu.adapters;

import java.util.List;

import com.wareninja.opensource.common.WareNinjaUtils;
import com.wareninja.opensource.droidfu.cachefu.ImageCache;
import com.wareninja.opensource.droidfu.imageloader.ImageLoaderHandler;
import com.wareninja.opensource.droidfu.widgets.WebImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView.ScaleType;


/**
 * Can be used as an adapter for an Android {@link Gallery} view. This adapter loads the images to
 * be shown from the web.
 * 
 * @author Matthias Kaeppler
 */
public class WebGalleryAdapter extends BaseAdapter {

    private List<String> imageUrls;
    private Context mContext;
    private Drawable progressDrawable;
    
    public WebGalleryAdapter(Context context) {
        initialize(context, null, null);
    }

    /**
     * @param context
     *            the current context
     * @param imageUrls
     *            the set of image URLs which are to be loaded and displayed
     */
    public WebGalleryAdapter(Context context, List<String> imageUrls) {
        initialize(context, imageUrls, null);
    }

    /**
     * @param context
     *            the current context
     * @param imageUrls
     *            the set of image URLs which are to be loaded and displayed
     * @param progressDrawableResId
     *            the resource ID of the drawable that will be used for rendering progress
     */
    public WebGalleryAdapter(Context context, List<String> imageUrls, int progressDrawableResId) {
        initialize(context, imageUrls, context.getResources().getDrawable(progressDrawableResId));
    }

    private void initialize(Context context, List<String> imageUrls, Drawable progressDrawable) {
        this.imageUrls = imageUrls;
        this.mContext = context;
        this.progressDrawable = progressDrawable;
        
        //mWebImages = new WebImageView[imageUrls.size()];
    }

    
    // --- added by YG ---
    private int itemBackground = -1;
    private int itemW = -1;
    private int itemH = -1;
	public int getItemBackground() {
		return itemBackground;
	}
	public void setItemBackground(int itemBackground) {
		this.itemBackground = itemBackground;
	}
	public int getItemW() {
		return itemW;
	}
	public void setItemW(int itemW) {
		this.itemW = itemW;
	}
	public int getItemH() {
		return itemH;
	}
	public void setItemH(int itemH) {
		this.itemH = itemH;
	}
	public void setItemWH(int itemW, int itemH) {
		this.itemW = itemW;
		this.itemH = itemH;
	}
	// YG: so that you can pass custom imageCache from your app
    ImageCache preInitImageCache;
	public ImageCache getPreInitImageCache() {
		return preInitImageCache;
	}
	public void setPreInitImageCache(ImageCache preInitImageCache) {
		this.preInitImageCache = preInitImageCache;
	}
	// ---
	
	public int getCount() {
        return imageUrls.size();
    }

    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setProgressDrawable(Drawable progressDrawable) {
        this.progressDrawable = progressDrawable;
    }

    public Drawable getProgressDrawable() {
        return progressDrawable;
    }

    // TODO: both convertView and ViewHolder are pointless at the moment, since there's a framework
    // bug which causes views to not be cached in a Gallery widget:
    // http://code.google.com/p/android/issues/detail?id=3376
    public View getView(int position, View convertView, ViewGroup parent) {

        String imageUrl = (String) getItem(position);

        ViewHolder viewHolder = null;
        //-WebImageView webImageView = null;
        final WebImageView webImageView;

        if (convertView == null) {
            // create the image view
            webImageView = new WebImageView(mContext, null, progressDrawable, false);
            
            if (preInitImageCache!=null)
            	webImageView.setPreInitImageCache(preInitImageCache);
            
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            
            webImageView.setLayoutParams(lp);
            
            // create the container layout for the image view
            FrameLayout container = new FrameLayout(mContext);
            
            if(itemW!=-1&&itemH!=-1) {
	            container.setLayoutParams(new Gallery.LayoutParams(
	            		itemW, itemH
	            		));
            }
            else {
            	container.setLayoutParams(new Gallery.LayoutParams(
	            		LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT
	            		));
            }
            
            container.addView(webImageView, 0);

            convertView = container;

            viewHolder = new ViewHolder();
            viewHolder.webImageView = webImageView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            webImageView = viewHolder.webImageView;
        }

        // calling reset is important to prevent old images from displaying in a recycled view.
        webImageView.reset();

        // added by YG
        if (itemBackground != -1)
        	webImageView.setBackgroundResource(itemBackground);
        
        webImageView.setImageUrl(imageUrl);
        //-webImageView.loadImage();
        webImageView.loadImage(
				//new DefaultImageLoaderHandler(webImageView.getImageView(), webImageView.getImageUrl())
        		new ImageLoaderHandler(webImageView.getImageView(), webImageView.getImageUrl()) {
        			
        			@Override
        	        protected boolean handleImageLoaded(Bitmap bitmap, Message msg) {
        	        	
        	        	//bitmap = MobileUtils.getRoundedCornerBitmap(bitmap);
        	        	
        	        	bitmap = WareNinjaUtils.getBitmapWithReflection(bitmap);
        	        	
        	        	boolean wasUpdated = super.handleImageLoaded(bitmap, msg);
        	        	if (wasUpdated) {
        	        		webImageView.setLoaded(wasUpdated);
        	        		webImageView.setDisplayedChild(1);
        	            }
        	            return wasUpdated;
        	        	//super.getImageView().setImageBitmap(bitmap);
        	        	
        	        }
        		}
				);
        

        onGetView(position, convertView, parent);

        return convertView;
    }

    private class DefaultImageLoaderHandler extends ImageLoaderHandler {

        public DefaultImageLoaderHandler(ImageView imgView, String imgUrl) {
            super(imgView, imgUrl);
        }

        @Override
        protected boolean handleImageLoaded(Bitmap bitmap, Message msg) {
        	
        	//bitmap = MobileUtils.getRoundedCornerBitmap(bitmap);
        	
        	bitmap = WareNinjaUtils.getRoundedCornerBitmap(bitmap);
        	//TODO: we are here
        	
        	boolean wasUpdated = super.handleImageLoaded(bitmap, msg);
        	/*if (wasUpdated) {
                super.setLoaded(wasUpdated);
                super.setDisplayedChild(1);
            }*/
            return wasUpdated;
        	//super.getImageView().setImageBitmap(bitmap);
        	
        }
    }
    
    protected void onGetView(int position, View convertView, ViewGroup parent) {
        // for extension
    }

    private static final class ViewHolder {
        private WebImageView webImageView;
    }

    /*
    // --- from CoverFlowWidget ---
    private WebImageView[] mWebImages;
    public boolean createReflectedImages() {
        //The gap we want between the reflection and the original image
        final int reflectionGap = 4;
        
        WebImageView webImageView = null;
        
        int index = 0;
        for (String imageUrl : imageUrls) {
        	
        	webImageView = new WebImageView(mContext, null, progressDrawable, false);
            if (preInitImageCache!=null)
            	webImageView.setPreInitImageCache(preInitImageCache);
            if (itemBackground != -1)
            	webImageView.setBackgroundResource(itemBackground);
            webImageView.setImageUrl(imageUrl);
            //webImageView.loadImage();
            webImageView.loadImage(
    				new ImageLoaderHandler(webImageView.getImageView(), webImageView.getImageUrl()) {
    					@Override
    			        protected boolean handleImageLoaded(Bitmap bitmap, Message msg) {
    			        	
    			        	//bitmap = MobileUtils.getRoundedCornerBitmap(bitmap);
    						Bitmap originalImage = bitmap;
    						
    				         int width = originalImage.getWidth();
    				         int height = originalImage.getHeight();
    				         
    				   
    				         //This will not scale but will flip on the Y axis
    				         Matrix matrix = new Matrix();
    				         matrix.preScale(1, -1);
    				         
    				         //Create a Bitmap with the flip matrix applied to it.
    				         //We only want the bottom half of the image
    				         Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);
    				             
    				         //Create a new bitmap with same width but taller to fit reflection
    				         Bitmap bitmapWithReflection = Bitmap.createBitmap(width 
    				           , (height + height/2), Config.ARGB_8888);
    				       
    				        //Create a new Canvas with the bitmap that's big enough for
    				        //the image plus gap plus reflection
    				        Canvas canvas = new Canvas(bitmapWithReflection);
    				        //Draw in the original image
    				        canvas.drawBitmap(originalImage, 0, 0, null);
    				        //Draw in the gap
    				        Paint deafaultPaint = new Paint();
    				        canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
    				        //Draw in the reflection
    				        canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);
    				        
    				        //Create a shader that is a linear gradient that covers the reflection
    				        Paint paint = new Paint(); 
    				        LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, 
    				          bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, 
    				          TileMode.CLAMP); 
    				        //Set the paint to use this shader (linear gradient)
    				        paint.setShader(shader); 
    				        //Set the Transfer mode to be porter duff and destination in
    				        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN)); 
    				        //Draw a rectangle using the paint with our linear gradient
    				        canvas.drawRect(0, height, width, 
    				          bitmapWithReflection.getHeight() + reflectionGap, paint); 
    				        
    				        
    				        ImageView imageView = new ImageView(mContext);
    				        imageView.setImageBitmap(bitmapWithReflection);
    				        //imageView.setLayoutParams(new CoverFlow.LayoutParams(120, 180));
    				        if(itemW!=-1&&itemH!=-1) {
    				        	imageView.setLayoutParams(new Gallery.LayoutParams(
    				            		itemW, itemH
    				            		));
    			            }
    			            else {
    			            	imageView.setLayoutParams(new Gallery.LayoutParams(
    				            		LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT
    				            		));
    			            }
    				        imageView.setScaleType(ScaleType.MATRIX);
    				        mWebImages[index++] = imageView;
    						
    			            return true;
    			        }
    				}
    			);
        }
        return true;
    }
*/
}
