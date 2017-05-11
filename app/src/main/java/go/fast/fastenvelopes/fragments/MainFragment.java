package go.fast.fastenvelopes.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import go.fast.fastenvelopes.R;



/**
 * 应用市场主页面Activity
 * 
 */
public class MainFragment extends BaseFragment implements OnClickListener {

    protected ViewPager mViewPager;

    TabPageIndicatorAdapter adapter;

    private SessionFragment friendsFragment;


    private TabPageIndicator indicator;
    
    private ImageView tixingImg;

    private static final String[] TITLE = new String[] { "红包", "正在抢",  "会话",
	    "我" };

    private int[] tabIcon = new int[] { R.drawable.envelope_tab,
	  R.drawable.room_tab, R.drawable.talking_tab,
	    R.drawable.setting_tab };

    public static MainFragment newInstance() {
	return new MainFragment();
    }

    TopBarFragment topBarFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View rootView = inflater.inflate(R.layout.main_fragment, null);
	mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
	mViewPager.setOffscreenPageLimit(5);
	adapter = new TabPageIndicatorAdapter(getFragmentManager());

	mViewPager.setAdapter(adapter);
	// adapter.get
	indicator = (TabPageIndicator) rootView.findViewById(R.id.indicator);
	indicator.setViewPager(mViewPager);
	indicator.setOnPageChangeListener(new OnPageChangeListener() {
	    @Override
	    public void onPageSelected(int arg0) {
		if (topBarFragment != null) {
		    topBarFragment.onCheckedChange(
			    (BaseFragment) adapter.getItem(arg0), arg0);
		}

		((BaseFragment) adapter.getItem(arg0)).firstShowPage();
		
		
		if (arg0 == 2) {
		  //  updateTimer();
		} else {
		    stopRotateLivingTab();
		}
	    }

	    @Override
	    public void onPageScrolled(int arg0, float arg1, int arg2) {
	    }

	    @Override
	    public void onPageScrollStateChanged(int arg0) {

	    }
	});

	indicator.setOnTabReselectedListener(new TabPageIndicator.OnTabReselectedListener() {

	    @Override
	    public void onTabReselected(int position) {
		if (mViewPager.getCurrentItem() == 2) {
//		    if (livingFragment != null) {
//			// livingFragment.refrushLivingInfo();// 手动刷新数据
//
//			startActivity(new Intent(getActivity(),
//				WorkbenchActivity.class));
//			getActivity().overridePendingTransition(
//				R.anim.slide_in_bottom, R.anim.slide_out_top);
//
//		    }
		}
	    }
	});
	mViewPager.setCurrentItem(1);

	return rootView;
    }

    Animation rotateAnim;
   Animation reverRotateAnim;
    /**
     * 在用户输入后延时10秒
     */

    private void rotateLivingTab() {

	View tabView = indicator.getRootView().getChildAt(2);

//	ImageView livingImg = (ImageView) tabView
//		.findViewById(R.id.bottom_w_iv);

	rotateAnim = AnimationUtils.loadAnimation(getActivity(),
		R.anim.rotate_360);
	reverRotateAnim = AnimationUtils.loadAnimation(getActivity(),
		R.anim.rotate_360_rever);
	//livingImg.startAnimation(rotateAnim);
	//livingImg.startAnimation(reverRotateAnim);
    }

    private void stopRotateLivingTab() {
	
//	if (updateTimer != null) {
//	    updateTimer.cancel();
//	}
	if (rotateAnim != null) {
	    rotateAnim.cancel();

	}
//	if (reverRotateAnim != null) {
//	    reverRotateAnim.cancel();
//
//	}
    }

    public void setTopBarFragment(TopBarFragment topBarFragment) {
	this.topBarFragment = topBarFragment;
	
	if (topBarFragment != null && mViewPager.getCurrentItem() == 2) {
	    topBarFragment
		    .onCheckedChange((BaseFragment) adapter.getItem(2), 2);// 首次进入
	}
    }

    public void startAnim() {
    }

//    @Override
//    public void initRight(ImageView imageView, TextView textView, View parent) {
//	// TODO Auto-generated method stub
//	super.initRight(imageView, textView, parent);
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	for (int i = 0; i < adapter.getCount(); i++) {
	    adapter.getItem(i).onActivityResult(requestCode, resultCode, data);
	}

    }

    @Override
    public void onResume() {
	super.onResume();
    }

    class TabPageIndicatorAdapter extends FragmentPagerAdapter implements
			IconPagerAdapter {
	public TabPageIndicatorAdapter(FragmentManager fm) {
	    super(fm);
	}

	@Override
	public Fragment getItem(int position) {
	    BaseFragment fragment = null;

	    switch (position) {
	    case 1:
		fragment = EnvelopeRoomFragment.newInstance();
		break;
	    case 2:
		fragment = SessionFragment.newInstance();
		break;
	    case 3:
		fragment = SettingFragment.newInstance();
		break;
	    default:

			EnvelopesFragment envelopesFragment = EnvelopesFragment
			.newInstance();
		fragment = envelopesFragment;
	    }

	    return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
	    return TITLE[position % TITLE.length];
	}

	@Override
	public int getCount() {
	    return TITLE.length;
	}

	@Override
	public int getIconResId(int index) {
	    // TODO Auto-generated method stub
	    return tabIcon[index % TITLE.length];
	}

	@Override
	public int getBackgroundResId(int index) {
	    // TODO Auto-generated method stub
	    return 0;
	}

	@Override
	public int getTextSize() {
	    // TODO Auto-generated method stub
	    return 12;
	}

	@Override
	public int getTextColor() {
	    // TODO Auto-generated method stub
	    return 0;
	}
    }

    // 更新图库
    private void updateCamera(File file) {
	Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	Uri uri = Uri.fromFile(file);
	intent.setData(uri);
	getActivity().sendBroadcast(intent);
    }

    public void saveToCamera(File copyFile) {

	if (Environment.getExternalStorageState().equals(
		Environment.MEDIA_MOUNTED)) {
	    File cameraFile = new File(
		    Environment.getExternalStorageDirectory() + "/DCIM");

	    if (cameraFile.exists() && cameraFile.isDirectory()) {
		File[] files = cameraFile.listFiles();
		for (int i = 0; i < files.length; i++) {
		    if (files[i].getName().toLowerCase().contains("camera")
			    && files[i].isDirectory()) {

			if (CopySdcardFile(copyFile.getPath(), files[i] + "/"
				+ copyFile.getName()) == 0) {

			} else {
			    updateCamera(copyFile);
			    Toast.makeText(getActivity(), "所选图片保存到系统相册失败~~~~~",
				    Toast.LENGTH_LONG).show();
			}

			break;
		    }
		}

	    }

	}

    }

    // 文件拷贝
    // 要复制的目录下的所有非子目录(文件夹)文件拷贝
    public int CopySdcardFile(String fromFile, String toFile) {

	try {
	    InputStream fosfrom = new FileInputStream(fromFile);
	    OutputStream fosto = new FileOutputStream(toFile);
	    byte bt[] = new byte[1024];
	    int c;
	    while ((c = fosfrom.read(bt)) > 0) {
		fosto.write(bt, 0, c);
	    }
	    fosfrom.close();
	    fosto.close();

	    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    Uri uri = Uri.fromFile(new File(toFile));
	    intent.setData(uri);
	    getActivity().sendBroadcast(intent);
	    return 0;

	} catch (Exception ex) {
	    return -1;
	}
    }

    @Override
    public void onClick(View v) {
	// TODO Auto-generated method stub

    }

}
