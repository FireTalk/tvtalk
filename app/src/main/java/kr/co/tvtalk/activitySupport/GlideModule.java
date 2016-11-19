package kr.co.tvtalk.activitySupport;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;

/**
 * Created by kwongyo on 2016-10-03.
 */

public class GlideModule implements com.bumptech.glide.module.GlideModule{
    private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private final int cacheSize = maxMemory / 8 ;
    private final int DISK_CACHE_SIZE = 1024*1024*10;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context,"cache",DISK_CACHE_SIZE))
                .setMemoryCache(new LruResourceCache(cacheSize))
                .setDecodeFormat(DecodeFormat.PREFER_ARGB_8888); // default가 4444인데 8888로 변경.
    }
    @Override
    public void registerComponents(Context context,Glide glide) {
        //아직 없음.
        return ;

    }

}
