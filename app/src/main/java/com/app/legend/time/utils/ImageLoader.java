package com.app.legend.time.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.app.legend.time.R;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.PAINT_FLAGS;

/**
 * 图片加载器
 * 负责图片的加载与优化
 */
public class ImageLoader {

    private static volatile ImageLoader imageLoader;
    private LruCache<String,Bitmap> lruCache;
    private static String CACHE_PATH= "";//文件缓存


    private ImageLoader(Context context) {
        if (CACHE_PATH.isEmpty()){
            CACHE_PATH=context.getCacheDir().getPath();//获取缓存地址
        }

        int maxMemory= (int) (Runtime.getRuntime().maxMemory()/1024);
        int cacheSize=maxMemory/8;

        lruCache=new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {

                return bitmap.getRowBytes()*bitmap.getHeight()/1024;
            }
        };


    }

    public static ImageLoader getDefault(Context context){
        if (imageLoader==null){

            synchronized (ImageLoader.class){
                imageLoader=new ImageLoader(context);
            }

        }

        return imageLoader;
    }


    //清除缓存
    public void clean(){
        lruCache.evictAll();

        File file=new File(CACHE_PATH);

        if (file.exists()&&file.isDirectory()){

            for (File file1:file.listFiles()){
                file1.delete();
            }
        }
    }

    /**
     * 本地缓存
     * @param bitmap 缓存的图片
     * @param url MD5加密命名
     */
    private void cacheImageInDisk(Bitmap bitmap,String url,int w,int h){
        String name=getUniqueId(url,w,h);

        try {


            File file=new File(CACHE_PATH,name);

            File parentFile=file.getParentFile();

            if (!parentFile.exists()){
                parentFile.mkdirs();
            }

            bitmap.compress(Bitmap.CompressFormat.WEBP,100,new FileOutputStream(file));

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

    }


    /**
     * 从本地读取缓存
     * @param url 本地文件的地址
     * @return 返回bitmap
     */
    private Bitmap getBitmapFromDisk(String url,int reqWidth,int reqHeight){
        Bitmap bitmap=null;
        String name=getUniqueId(url,reqWidth,reqHeight);

        File file=new File(CACHE_PATH).getAbsoluteFile();

        String file_path=file+"/"+name;

        try {

            bitmap=BitmapFactory.decodeFile(file_path);

            bitmap=getScaleBitmap(bitmap,reqWidth,reqHeight);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null!=bitmap) {
                //还需要进行内存缓存
                cacheInMemory(bitmap, url,reqWidth,reqHeight);
            }

            return bitmap;
        }
    }


    /**
     * 写入内存缓存
     * @param bitmap 写入的图片
     * @param url 写入的唯一标识
     */
    private void cacheInMemory(Bitmap bitmap,String url,int w,int h){
        String name=getUniqueId(url,w,h);

        if (name==null){
            return;
        }

        lruCache.put(name,bitmap);
    }

    /**
     * 读取内存缓存
     * @param url 读取的唯一标识
     * @return 返回图片
     */
    private Bitmap getBitmapFromMemory(String url,int reqWidth,int reqHeight){
        String name=getUniqueId(url,reqWidth,reqHeight);

        if (name==null){
            return null;
        }

        return lruCache.get(name);
    }


    //md5加密改名
    private String getMd5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuilder buf = new StringBuilder("");
            for (byte aB : b) {
                i = aB;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    private int reSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        int size=1;

        int width=options.outWidth;

        int height=options.outHeight;



        if (height>reqHeight/2||width>reqWidth/2){
            int halfHeight=height/2;

            int halfWidth=width/2;

            while ((halfHeight/size)>=reqHeight&&
                    (halfWidth/size)>=reqWidth){
                size *=2;
            }
        }

        return size;
    }

    /**
     * 获取图片唯一标识
     * @param url 图片路径
     * @param width 图片宽度
     * @param height 图片高度
     * @return 返回md5(url+'-image-'+width+height)
     */
    private String getUniqueId(String url,int width,int height){


        String name=url+"-image-"+width+height;

        return getMd5(name);
    }

    /**
     * 外部设置图片
     * @param imageView 需要设置的imageview
     * @param w 宽度
     * @param h 高度
     * @param url 图片地址
     */
    public void load(ImageView imageView,int w,int h,String url){

        loadImage(imageView,w,h,url);
    }

    /**
     * 内部私有设置方法
     * @param imageView 需要设置的imageview
     * @param w 宽度
     * @param h 高度
     * @param url 图片地址
     */
    private void loadImage(ImageView imageView,int w,int h,String url){

        Observable
                .create((ObservableOnSubscribe<Result>) e -> {

                    Result result=getResult(imageView,w,h,url);

                    e.onNext(result);

                    e.onComplete();

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable=d;
                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.bitmap!=null&&imageView.getTag().equals(url)){
                            imageView.setImageBitmap(result.bitmap);

                        }else {//可设置默认图

                            imageView.setImageResource(R.drawable.ic_image_black_24dp);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (!disposable.isDisposed()){
                            disposable.dispose();
                        }
                    }
                });


    }


    private Result getResult(ImageView imageView,int w,int h,String url){

        imageView.setTag(url);//设置tag



        if (w<=0||h<=0){//如果获取到的宽高为0


            int reW = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int reH = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

            imageView.measure(reW,reH);

            int viewW=imageView.getMeasuredWidth();
            int viewH=imageView.getMeasuredHeight();

            Bitmap bitmap=getBitmap(url,viewW,viewH);

            return new Result(bitmap,viewW,viewH);

        }else {//宽高明确的情况下


            Bitmap bitmap=getBitmap(url,w,h);
            return new Result(bitmap,w,h);

        }

    }

    /**
     * 普通获取Bitmap
     * 如果存在则需要缓存
     * @param url 路径
     * @param w 宽度
     * @param h 高度
     * @return 返回Bitmap
     */
    private Bitmap getBitmap(String url,int w,int h){

        Bitmap bitmap=null;

        bitmap=getBitmapFromMemory(url,w,h);//获取内存缓存

        if (bitmap==null){//获取不到 从磁盘缓存获取

            bitmap=getBitmapFromDisk(url,w,h);//获取磁盘缓存

            if (bitmap!=null){

                cacheInMemory(bitmap,url,w,h);
            }
        }

        if (bitmap==null){//获取不到 从原始URL获取



            bitmap=BitmapFactory.decodeFile(url);

            bitmap=getScaleBitmap(bitmap,w,h);

        }

        return bitmap;

    }

    /**
     * 获取缩放后的Bitmap
     * @param reW 需要的宽度
     * @param reH 需要的高度
     * @return 返回Bitmap
     */
    private Bitmap getScaleBitmap(Bitmap bitmap,int reW,int reH){

        if (bitmap==null){
            return null;
        }

        if (bitmap.getWidth()==reW&&bitmap.getHeight()==reH){
            return bitmap;
        }

        float scale;

        float dx = 0, dy = 0;
        Matrix m = new Matrix();
        if (bitmap.getWidth() * reH > reW * bitmap.getHeight()) {
            scale = (float) reH / (float) bitmap.getHeight();
            dx = (reW - bitmap.getWidth() * scale) * 0.5f;
        } else {
            scale = (float) reW / (float) bitmap.getWidth();
            dy = (reH - bitmap.getHeight() * scale) * 0.5f;
        }

        m.setScale(scale, scale);
        m.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
        final Bitmap result = Bitmap.createBitmap(reW, reH, Bitmap.Config.ARGB_8888);


        // We don't add or remove alpha, so keep the alpha setting of the Bitmap we were given.
        TransformationUtils.setAlpha(bitmap, result);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(PAINT_FLAGS);
        canvas.drawBitmap(bitmap, m, paint);
        return result;

    }


    static class Result{

        Bitmap bitmap;
        int w;
        int h;

        public Result(Bitmap bitmap, int w, int h) {
            this.bitmap = bitmap;
            this.w = w;
            this.h = h;
        }

        public Result() {
        }
    }



}
