package go.fast.fastenvelopes.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import go.fast.fastenvelopes.R;

public class AboutActivity extends BaseActivity

{
    TextView versionText;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);


        versionText = (TextView) findViewById(R.id.version);

        try {
            versionText.setText("V" + getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void initMiddle(TextView middleView) {
        super.initMiddle(middleView);
        middleView.setText("关于我们");
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }
}
