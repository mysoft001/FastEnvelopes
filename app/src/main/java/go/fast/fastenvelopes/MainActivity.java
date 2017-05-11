package go.fast.fastenvelopes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import go.fast.fastenvelopes.fragments.MainFragment;
import go.fast.fastenvelopes.fragments.TopBarFragment;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.UpgradeObj;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.DataUtils;
import go.fast.fastenvelopes.utils.JumpUtils;
import go.fast.fastenvelopes.widgets.CoolTextDialog;

public class MainActivity extends FragmentActivity {

    MainFragment fragment;

    TopBarFragment fragmentTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
//         getWindow().addFlags(
//         WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        ImageLoader.getInstance().init(
                ImageLoaderConfiguration.createDefault(MainActivity.this));
        fragment = (MainFragment) getSupportFragmentManager().findFragmentById(
                R.id.content_fragment_main);
        fragmentTopic = (TopBarFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_fragment_top);
        // We only create a fragment if it doesn't already exist.

        fragment.setTopBarFragment(fragmentTopic);
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        if (isKitKat)// 如果系统版本大于等于6.0版本
        {
           requestPermission();
        }
        else
        {


        }

       upgradeApp();
    }

    private void upgradeApp() {
        HttpRequest.upgradeApp(this, new HttpRequest.onRequestCallback() {

            @Override
            public void onSuccess(BaseInfo response) {

                UpgradeObj upgradeObj = (UpgradeObj) response;

                showUpgradeAppDialog(upgradeObj);
            }

            @Override
            public void onFailure(String rawJsonData) {
            }
        });
    }

    private void showUpgradeAppDialog(UpgradeObj upgradeObj) {
        if (upgradeObj == null) {
            return;
        }
        int currentVersion = DataUtils.getVersionCode(this);

        if (currentVersion < upgradeObj.version) {
            CoolTextDialog cancelShow = new CoolTextDialog(this);
            cancelShow.setTitle("更新提示");
            cancelShow.setContent(upgradeObj.content);
            if (currentVersion < upgradeObj.mustStartVersion) {
                cancelShow.setCancelable(false);
            }
            else
            {
                cancelShow.setOnNegtiveListener("取消", new View.OnClickListener() {
                    public void onClick(View v) {
                    }
                });
            }


            cancelShow.setOnPostiveListener("确定", new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    JumpUtils.jumpToMarket(MainActivity.this);
                }
            });
            cancelShow.show();
        }

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            // remove掉保存的Fragment
            outState.remove(FRAGMENTS_TAG);
        }
    }

    @SuppressLint({ "InlinedApi", "NewApi" })
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(android.Manifest.permission.READ_PHONE_STATE,
                        PackageManager.PERMISSION_GRANTED);
                // perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                // PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(MainActivity.this, "有权限未被授权，可能会影响软件的正常使用！",
                            Toast.LENGTH_LONG).show();
                }
                // PushManager.getInstance().initialize(this.getApplicationContext());//
                // 初始化个推
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    // final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    // android 6.0权限申请
    @SuppressLint("NewApi")
    private void requestPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList,
                android.Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("用户标识");
        if (!addPermission(permissionsList,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("SD卡写入");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "你需要开启以下权限  " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                message = message + "才能完美地使用本软件。请到手机设置里找到本应用开启相应权限。";
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                requestPermissions(permissionsList
                                                .toArray(new String[permissionsList
                                                        .size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(
                    permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

    }

    private void showMessageOKCancel(String message,
                                     DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this).setMessage(message)
                .setPositiveButton("好的", okListener)
                .setNegativeButton("取消", null).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this **/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("NewApi")
    private boolean addPermission(List<String> permissionsList,
                                  String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }




}
