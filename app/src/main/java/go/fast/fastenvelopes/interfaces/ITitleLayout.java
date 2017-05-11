package go.fast.fastenvelopes.interfaces;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public interface ITitleLayout {
	void initMiddle(TextView middleView);
	void initLeft(ImageView imageView, TextView textView, View parent);
	void initRight(ImageView imageView, TextView textView, View parent);
	void initRefushImageView(ImageView imageView);
	void initCenterImv(ImageView imageView);

    void initRightFlag(ImageView imageView);
    void initLeftFlag(ImageView imageView);
}
