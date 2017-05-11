package go.fast.fastenvelopes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.interfaces.ITitleLayout;
import go.fast.fastenvelopes.utils.PreferenceUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;



public class TopBarFragment extends Fragment {
    private TextView texLeft;
    private TextView texRight;
    private TextView texTitle;

    private ImageView refushImage;
    private ImageView mRefushView;
    private View mLeftView;
    private View mRightView;
    private TextView mTitleView;
    private ImageView mRightFlagView;
    private ImageView mLeftFlagView;
    private ImageView mCenterImv;

    private ImageView unreadImg;

    private View parentView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);
	mLeftView = view.findViewById(R.id.layout_title_left);
	mRightView = view.findViewById(R.id.layout_title_right);
	mTitleView = (TextView) view.findViewById(R.id.text_title);
	mRefushView = (ImageView) view.findViewById(R.id.refresh);
	unreadImg = (ImageView) view.findViewById(R.id.tixing_iv);
	unreadImg = (ImageView) view.findViewById(R.id.tixing_iv);
	mRightFlagView = (ImageView) view.findViewById(R.id.right_flag_img);
	mLeftFlagView = (ImageView) view.findViewById(R.id.left_flag_img);
	mCenterImv = (ImageView) view.findViewById(R.id.topbar_center_imv);
	parentView = view.findViewById(R.id.topbar);
	parentView.setPadding(0, PreferenceUtil.getInstance(getActivity())
		.getInt("titleH", 40), 0, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.fragmentlayout_topbar, container,
		false);
	return view;
    }

    public void removeTitleView() {
    }

    @Override
    public void onResume() {
	super.onResume();

	if (currentPos == 3) {
	    if (PreferenceUtils.getInstance(getActivity())
		    .getSettingUnreadMsg()) {
		unreadImg.setVisibility(View.VISIBLE);
	    } else {
		unreadImg.setVisibility(View.INVISIBLE);
	    }
	} else {
	    unreadImg.setVisibility(View.INVISIBLE);
	}

    }

    int currentPos;

    public void onCheckedChange(ITitleLayout currentLayout, int pos) {

	currentPos = pos;
	if (pos == 4) {
	    if (PreferenceUtils.getInstance(getActivity())
		    .getSettingUnreadMsg()) {
		unreadImg.setVisibility(View.VISIBLE);
	    } else {
		unreadImg.setVisibility(View.INVISIBLE);
	    }
	} else {
	    unreadImg.setVisibility(View.INVISIBLE);
	}
	// mLeftView.setBackgroundResource(R.drawable.title_left_selector);
	currentLayout.initLeft(
		(ImageView) mLeftView.findViewById(R.id.view_image),
		(TextView) mLeftView.findViewById(R.id.view_text), mLeftView);
	currentLayout
		.initRight(
			(ImageView) mRightView.findViewById(R.id.view_imagea),
			(TextView) mRightView.findViewById(R.id.view_texta),
			mRightView);

	currentLayout.initRightFlag(mRightFlagView);
	currentLayout.initLeftFlag(mLeftFlagView);
	currentLayout.initMiddle(mTitleView);
	currentLayout.initRefushImageView(mRefushView);
	currentLayout.initCenterImv(mCenterImv);
    }

}
