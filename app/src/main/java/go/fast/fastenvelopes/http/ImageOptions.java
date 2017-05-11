package go.fast.fastenvelopes.http;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import go.fast.fastenvelopes.R;

public class ImageOptions {
    public static DisplayImageOptions getOptions() {
	DisplayImageOptions options;
	options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.img_default_load_fail)// 设置图片在下载期间显示的图片
		.showImageForEmptyUri(R.drawable.img_default_load_fail)// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.img_default_load_fail)// 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)// 是否緩存都內存中
		//.displayer(new RoundedBitmapDisplayer(20))
		// .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
		.cacheOnDisc(true)// 是否緩存到sd卡上
		.build();
	return options;
    }
    
    public static DisplayImageOptions getOptionsByRound(int round) {
	DisplayImageOptions options;
	options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.img_default_load_fail)// 设置图片在下载期间显示的图片
		.showImageForEmptyUri(R.drawable.img_default_load_fail)// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.img_default_load_fail)// 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)// 是否緩存都內存中
		.displayer(new RoundedBitmapDisplayer(round))
		// .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
		.cacheOnDisc(true)// 是否緩存到sd卡上
		.build();
	return options;
    }
    
    public static DisplayImageOptions getOptionsTouxiangByRound(int round) {
	DisplayImageOptions options;
	options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.author_head_default)// 设置图片在下载期间显示的图片
		.showImageForEmptyUri(R.drawable.author_head_default)// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.author_head_default)// 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)// 是否緩存都內存中
		.displayer(new RoundedBitmapDisplayer(round))
		// .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
		.cacheOnDisc(true)// 是否緩存到sd卡上
		.build();
	return options;
    }

    public static DisplayImageOptions getOptionsByDefaultImg(int defImgId) {
	DisplayImageOptions options;
	options = new DisplayImageOptions.Builder().showStubImage(defImgId)// 设置图片在下载期间显示的图片
		.showImageForEmptyUri(defImgId)// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(defImgId)// 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)// 是否緩存都內存中
		// .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
		.cacheOnDisc(true)// 是否緩存到sd卡上
		.build();
	return options;
    }

    public static DisplayImageOptions getOptionsByDefaultImgRound(int defImgId,int round) {
   	DisplayImageOptions options;
   	options = new DisplayImageOptions.Builder().showStubImage(defImgId)// 设置图片在下载期间显示的图片
   		.showImageForEmptyUri(defImgId)// 设置图片Uri为空或是错误的时候显示的图片
   		.showImageOnFail(defImgId)// 设置图片加载/解码过程中错误时候显示的图片
   		.cacheInMemory(true)// 是否緩存都內存中
   		.displayer(new RoundedBitmapDisplayer(round))
   		// .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
   		.cacheOnDisc(true)// 是否緩存到sd卡上
   		.build();
   	return options;
       }
    
    
    public static DisplayImageOptions getOptionsRoundByDefaultImg(int defImgId) {
	DisplayImageOptions options;
	options = new DisplayImageOptions.Builder().showStubImage(defImgId)// 设置图片在下载期间显示的图片
		.showImageForEmptyUri(defImgId)// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(defImgId)// 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)// 是否緩存都內存中
		.displayer(new RoundedBitmapDisplayer(20))
		// .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
		.cacheOnDisc(true)// 是否緩存到sd卡上
		.build();
	return options;
    }

    public static DisplayImageOptions getOptionsNoDefImg() {
	DisplayImageOptions options;
	options = new DisplayImageOptions.Builder().cacheInMemory(true)// 是否緩存都內存中
		.displayer(new RoundedBitmapDisplayer(20)).cacheOnDisc(true)// 是否緩存到sd卡上
		.build();
	return options;
    }

    public static DisplayImageOptions getOptionsNoRound() {
	DisplayImageOptions options;
	options = new DisplayImageOptions.Builder()
	 .showStubImage(R.drawable.img_default_load_fail)// 设置图片在下载期间显示的图片
        .showImageForEmptyUri(R.drawable.img_default_load_fail)//
	// 设置图片Uri为空或是错误的时候显示的图片
	.showImageOnFail(R.drawable.img_default_load_fail)//
	// 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)// 是否緩存都內存中
		.cacheOnDisc(true)// 是否緩存到sd卡上
		// .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
		.build();
	return options;
    }

    public static DisplayImageOptions get_gushi_Options() {
	DisplayImageOptions options;
	options = new DisplayImageOptions.Builder().cacheInMemory(true)// 是否緩存都內存中
		.cacheOnDisc(true)// 是否緩存到sd卡上
		.build();
	return options;
    }

    public static DisplayImageOptions get_shenqing_Options() {
	DisplayImageOptions options;
	options = new DisplayImageOptions.Builder().cacheInMemory(true)// 是否緩存都內存中
		.cacheOnDisc(true)// 是否緩存到sd卡上
		.build();
	return options;
    }
}
