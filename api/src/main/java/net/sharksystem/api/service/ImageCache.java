package net.sharksystem.api.service;

import android.graphics.Bitmap;
import android.util.LruCache;

import net.sharkfw.knowledgeBase.SemanticTag;

/**
 * Created by j4rvis on 5/9/17.
 */

public class ImageCache extends LruCache<SemanticTag, Bitmap> {
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public ImageCache(int maxSize) {
        super(maxSize);
    }

    public Bitmap getImage(SemanticTag tag){
        Bitmap tmp = get(tag);
        if(tmp==null){

        }
        return null;
    }
}
