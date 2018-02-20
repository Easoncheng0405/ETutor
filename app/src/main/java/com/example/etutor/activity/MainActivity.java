package com.example.etutor.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.etutor.InitApplication;
import com.example.etutor.R;
import com.example.etutor.Service.MessageReceiver;
import com.example.etutor.adpter.GridViewAdapter;
import com.example.etutor.adpter.Model;
import com.example.etutor.adpter.TeaInfoAdapter;
import com.example.etutor.adpter.ViewPagerAdapter;
import com.example.etutor.fragment.HomeFragment;
import com.example.etutor.fragment.PersonalFragment;
import com.example.etutor.gson.LoginResult;
import com.example.etutor.gson.TeacherInfo;
import com.example.etutor.gson.UserInfo;
import com.example.etutor.util.GlideImageLoader;
import com.example.etutor.util.Server;
import com.example.etutor.util.ToastUtil;
import com.example.etutor.util.UpdateUITools;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.yalantis.phoenix.PullToRefreshView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.AccordionTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class MainActivity extends AppCompatActivity implements OnBannerListener, View.OnClickListener
        , AdapterView.OnItemClickListener {
    private Activity activity;
    private Handler handler;
    private FragmentManager fragmentManager;
    private EaseConversationListFragment conversationListFragment;
    private HomeFragment homeFragment;
    private PersonalFragment personalFragment;
    private ImageView imageViewHome, imageViewCommunity, imageViewPersonal;
    private ArrayList<TeacherInfo> data;
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
        startService(new Intent(this, MessageReceiver.class));
        activity = this;
        handler = new Handler();
        fragmentManager = getSupportFragmentManager();
        initViews();
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

        findViewById(R.id.selection).addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int keyHeight = activity.getResources().getDisplayMetrics().heightPixels / 3;
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    findViewById(R.id.selection).setVisibility(View.INVISIBLE);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    findViewById(R.id.selection).setVisibility(View.VISIBLE);
                }
            }
        });
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
        initList();
        initPersonal();
    }

    private void initHome() {
        View headerView = View.inflate(MainActivity.this, R.layout.listviewhader, null);
        final PullToRefreshView pullToRefreshView = findViewById(R.id.refresh);
        final ListView listView = homeFragment.getView().findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = listView.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        pullToRefreshView.setEnabled(true);
                    } else
                        pullToRefreshView.setEnabled(false);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //do nothing
            }

        });
        if (listView.getHeaderViewsCount() < 1)
            listView.addHeaderView(headerView);
        data = new ArrayList<>();
        data.addAll(InitApplication.getTeaInfoList());
        TeaInfoAdapter adapter = new TeaInfoAdapter(MainActivity.this, R.layout.teainfo, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
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
        //设置viewPageAdapter
        mPager.setAdapter(new ViewPagerAdapter(mPagerList));
        CircleIndicator indicator = homeFragment.getView().findViewById(R.id.indicator);
        indicator.setViewPager(mPager);


        refreshTeaInfo(pullToRefreshView);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToastUtil.showMessage(getApplicationContext(), "正在加载");
                refreshTeaInfo(pullToRefreshView);
            }
        });
    }

    private void refreshTeaInfo(final PullToRefreshView pullToRefreshView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<TeacherInfo> res = Server.getTeaInfoList(handler);
                if (res != null) {
                    data.clear();
                    data.addAll(res);
                } else
                    handler.post(new UpdateUITools(" 加载失败，要不再试一次？"));
                handler.post(new UpdateUITools(pullToRefreshView));
            }
        }).start();
    }

    private void initList() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                conversationListFragment.refresh();
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });
    }

    private void initPersonal() {
        View view = personalFragment.getView();
        view.findViewById(R.id.logout).setOnClickListener(this);
        view.findViewById(R.id.personal_info).setOnClickListener(this);
        view.findViewById(R.id.head).setOnClickListener(this);

        ImageView header = view.findViewById(R.id.head);

        Glide.with(activity).load(Server.getURL() + "image/" + InitApplication.getUserInfo().getPhone())
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).into(header);
        ((TextView) view.findViewById(R.id.userName)).setText(InitApplication.getUserInfo().getName());
        ((TextView) view.findViewById(R.id.userPhone)).setText(InitApplication.getUserInfo().getPhone());

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
                startService(new Intent(this, MessageReceiver.class));
                imageViewHome.setImageResource(R.drawable.homes);
                imageViewCommunity.setImageResource(R.drawable.friends);
                imageViewPersonal.setImageResource(R.drawable.my);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content, homeFragment);
                } else
                    transaction.show(homeFragment);

                break;
            case 1:
                stopService(new Intent(this, MessageReceiver.class));
                imageViewHome.setImageResource(R.drawable.home);
                imageViewCommunity.setImageResource(R.drawable.friends_blue);
                imageViewPersonal.setImageResource(R.drawable.my);

                if (conversationListFragment == null) {
                    conversationListFragment = new EaseConversationListFragment();
                    setEaseUser();
                    conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
                        @Override
                        public void onListItemClicked(EMConversation conversation) {
                            Intent intent = new Intent(activity, ChatActivity.class);
                            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                            intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
                            startActivity(intent);
                        }
                    });
                    transaction.add(R.id.content, conversationListFragment);
                } else
                    transaction.show(conversationListFragment);
                break;
            case 2:
                startService(new Intent(this, MessageReceiver.class));

                imageViewHome.setImageResource(R.drawable.home);
                imageViewCommunity.setImageResource(R.drawable.friends);
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
        if (conversationListFragment != null) {
            transaction.hide(conversationListFragment);
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
                startPersonalInfoAty();
                break;
            case R.id.head:
                startPersonalInfoAty();
                break;
            default:
                break;
        }
    }

    private void startPersonalInfoAty() {
        Intent intent = new Intent(activity, PersonalInfoActivity.class);
        intent.putExtra("info", InitApplication.getUserInfo());
        intent.putExtra("teaInfo", InitApplication.getTeacherInfo());
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
        final TeacherInfo info = (TeacherInfo) parent.getAdapter().getItem(position);
        final String phone = info.getPhone();
        final RxDialogLoading dialogLoading = new RxDialogLoading(activity);
        dialogLoading.setLoadingText("拼命加载中...");
        dialogLoading.show();
        dialogLoading.setCancelable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = Server.getUserInfo(handler, phone);
                    handler.post(new UpdateUITools(dialogLoading));
                    if (json != null) {
                        LoginResult result = new Gson().fromJson(json, LoginResult.class);
                        UserInfo userInfo = result.getUserInfo();
                        TeacherInfo teacherInfo = result.getTeaInfo();
                        Intent intent = new Intent(activity, PersonalInfoActivity.class);
                        intent.putExtra("info", userInfo);
                        intent.putExtra("teaInfo", teacherInfo);
                        startActivity(intent);
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    handler.post(new UpdateUITools("服务器竟然出错了！"));
                }
            }
        }).start();
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


    private void setEaseUser() {
        EaseUI easeUI = EaseUI.getInstance();
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });
    }

    private EaseUser getUserInfo(String username) {

        EaseUser easeUser = new EaseUser(username);

        easeUser.setNickname(InitApplication.getName(username));
        easeUser.setAvatar(Server.getURL() + "image/" + username);
        return easeUser;
    }


}
