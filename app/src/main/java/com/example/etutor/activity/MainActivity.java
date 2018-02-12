package com.example.etutor.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.etutor.InitApplication;
import com.example.etutor.R;
import com.example.etutor.adpter.GridViewAdapter;
import com.example.etutor.adpter.Model;
import com.example.etutor.adpter.ViewPagerAdapter;
import com.example.etutor.fragment.CommunityFragment;
import com.example.etutor.fragment.HomeFragment;
import com.example.etutor.fragment.PersonalFragment;
import com.example.etutor.gson.TeacherInfo;
import com.example.etutor.adpter.TeaInfoAdapter;
import com.example.etutor.util.GlideImageLoader;
import com.example.etutor.util.ToastUtil;
import com.example.etutor.util.UpdateUITools;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.AccordionTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class MainActivity extends Activity implements OnBannerListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private Activity activity;
    private Handler handler;
    private FragmentManager fragmentManager;
    private CommunityFragment communityFragment;
    private HomeFragment homeFragment;
    private PersonalFragment personalFragment;
    private ImageView imageViewHome, imageViewCommunity, imageViewPersonal;

    ViewPager mPager;
    private String[] titles = {"语文", "数学", "英语", "物理", "化学", "生物",
            "政治", "历史", "地理", "其他"};
    private List<Model> mDatas;
    private int pageSize = 5;//每一页的个数
    private int curIndex = 0;//当前显示的事第几页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        handler = new Handler();
        fragmentManager = getFragmentManager();
        initViews();

        EMClient.getInstance().addConnectionListener(new MyConnectionListener());


    }

    private void initViews() {

        imageViewHome = findViewById(R.id.imhome);
        imageViewCommunity = findViewById(R.id.imcommunity);
        imageViewPersonal = findViewById(R.id.impersonal);


        //切换fragment点击事件
        findViewById(R.id.home).setOnClickListener(this);
        findViewById(R.id.community).setOnClickListener(this);
        findViewById(R.id.personal).setOnClickListener(this);

        setTabSelection(2);
        setTabSelection(1);
        setTabSelection(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initHome();
        initCommunity();
        initPersonal();
    }

    private void initHome() {
        View headerView = View.inflate(MainActivity.this, R.layout.listviewhader, null);
        ListView listView = homeFragment.getView().findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        if (listView.getHeaderViewsCount() < 1)
            listView.addHeaderView(headerView);
        TeacherInfo info1 = new TeacherInfo("吉林大学珠海学院", "计算机科学与技术", 5, "经验丰富", "fluidicon", "1");
        TeacherInfo info2 = new TeacherInfo("吉林大学珠海学院", "计算机科学与技术", 4, "经验丰富", "fluidicon", "2");
        TeacherInfo info3 = new TeacherInfo("吉林大学珠海学院", "计算机科学与技术", 3, "经验丰富", "fluidicon", "3");
        ArrayList<TeacherInfo> data = new ArrayList<>();
        TeaInfoAdapter adapter = new TeaInfoAdapter(MainActivity.this, R.layout.teainfo, data);
        data.add(info1);
        data.add(info2);
        listView.setAdapter(adapter);
        data.add(info3);
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.b1);
        list.add(R.mipmap.b2);
        list.add(R.mipmap.b3);
        List<?> images = new ArrayList<>(list);
        List<String> titles = new ArrayList<>(Arrays.asList(new String[]{"标题一", "标题二", "标题三"}));
        mPager = headerView.findViewById(R.id.viewGage);
        Banner banner = headerView.findViewById(R.id.banner);
        banner.setImages(images)
                .setBannerTitles(titles)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(MainActivity.this)
                .start();
        banner.setBannerAnimation(AccordionTransformer.class);
        banner.setDelayTime(3000);
        banner.updateBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);

        initDatas();

        LayoutInflater inflater = LayoutInflater.from(this);
        //总页数=总数/每页的个数，取整
        int pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);

        List<View> mPagerList = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            //每个页面都是inflate出的一个新实例
            @SuppressLint("InflateParams") GridView gridView = (GridView) inflater.inflate(R.layout.gridview, null);
            gridView.setAdapter(new GridViewAdapter(this, mDatas, i, pageSize));
            mPagerList.add(gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + curIndex * pageSize;
                    ToastUtil.showMessage(MainActivity.this, mDatas.get(pos).getName());
                }
            });
        }
        //设置viewpageAdapter
        mPager.setAdapter(new ViewPagerAdapter(mPagerList));
        CircleIndicator indicator = homeFragment.getView().findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
    }


    private void initCommunity() {


    }

    private void initPersonal() {
        View view=personalFragment.getView();
        view.findViewById(R.id.logout).setOnClickListener(this);
        view.findViewById(R.id.personal_info).setOnClickListener(this);
        ((TextView)view.findViewById(R.id.userName)).setText(InitApplication.getUserInfo().getName());
        ((TextView)view.findViewById(R.id.userPhone)).setText(InitApplication.getUserInfo().getPhone());

    }

    /**
     * 初始化数据源
     */
    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            //动态获取资源ID，第一个参数是资源名，第二个参数是资源类型例如drawable，string等，第三个参数包名
            int imageId = getResources().getIdentifier("ic_category_" + i, "mipmap", getPackageName());
            mDatas.add(new Model(titles[i], imageId));
        }

    }

    private void setTabSelection(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                imageViewHome.setImageResource(R.drawable.homes);
                imageViewCommunity.setImageResource(R.drawable.earth);
                imageViewPersonal.setImageResource(R.drawable.my);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content, homeFragment);
                } else
                    transaction.show(homeFragment);

                break;
            case 1:
                imageViewHome.setImageResource(R.drawable.home);
                imageViewCommunity.setImageResource(R.drawable.earths);
                imageViewPersonal.setImageResource(R.drawable.my);

                if (communityFragment == null) {
                    communityFragment = new CommunityFragment();
                    transaction.add(R.id.content, communityFragment);
                } else
                    transaction.show(communityFragment);
                break;
            case 2:
                imageViewHome.setImageResource(R.drawable.home);
                imageViewCommunity.setImageResource(R.drawable.earth);
                imageViewPersonal.setImageResource(R.drawable.mys);

                if (personalFragment == null) {
                    personalFragment = new PersonalFragment();
                    transaction.add(R.id.content, personalFragment);
                } else {
                    transaction.show(personalFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (communityFragment != null) {
            transaction.hide(communityFragment);
        }
        if (personalFragment != null) {
            transaction.hide(personalFragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                setTabSelection(0);
                break;
            case R.id.community:
                setTabSelection(1);
                break;
            case R.id.personal:
                setTabSelection(2);
                break;
            case R.id.logout:
                SharedPreferences preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showMessage(MainActivity.this, "无法连接到服务器，请重新登陆！");
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            case R.id.personal_info:
                startActivity(new Intent(activity,PersonalInfoActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void OnBannerClick(int position) {

    }

    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(final int error) {
            if (error == EMError.USER_REMOVED) {
                handler.post(new UpdateUITools(activity, "发生错误",
                        "您的环信账号被服务器强制下线，请尝试联系客服！", UpdateUITools.ForceClose));
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                handler.post(new UpdateUITools(activity, "警告！", "您的账号在其他设备登陆，您已被迫离线，" +
                        "如果这不是您的自己的操作，请及时修改密码或联系客服", UpdateUITools.ForceClose));
            } else {
                if (NetUtils.hasNetwork(MainActivity.this)) {
                    handler.post(new UpdateUITools("当前无法连接到环信聊天服务器，稍后会尝试重新连接"));
                } else {
                    handler.post(new UpdateUITools("网络无连接，检查您的网络设置"));
                }
            }
        }
    }
}
