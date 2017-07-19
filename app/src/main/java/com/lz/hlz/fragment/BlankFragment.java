package com.lz.hlz.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lz.hlz.App;
import com.lz.hlz.R;
import com.lz.hlz.Web_Activity;
import com.lz.hlz.Zxing.CaptureActivity;
import com.lz.hlz.adapter.DetailListAdapter;
import com.lz.hlz.bean.Bean;
import com.lz.hlz.loader.GlideImageLoader;
import com.lz.hlz.util.Utility;
import com.lz.hlz.view.VerticalMarqueeLayout;
import com.lz.hlz.view.WrapContentListView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BlankFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnBannerListener, AdapterView.OnItemClickListener {
    static final int REFRESH_COMPLETE = 0X1112;
    Banner banner;
    ScrollView sc;
    SwipeRefreshLayout mSwipeLayout;
    private List<Map<String, Object>> coursedList;
    private VerticalMarqueeLayout<Bean> vmLayout;
    private List<Bean> beans;
    private WrapContentListView listview;
    private TextView scanner;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    String[] urls = getResources().getStringArray(R.array.url4);
                    List list = Arrays.asList(urls);
                    List arrayList = new ArrayList(list);
                    banner.update(arrayList);
                    mSwipeLayout.setRefreshing(false);
                    break;
            }
        }
    };

    public BlankFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        listview = (WrapContentListView) view.findViewById(R.id.main_lv);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        scanner = (TextView) view.findViewById(R.id.scanner);
        mSwipeLayout.setOnRefreshListener(this);
        banner = (Banner) view.findViewById(R.id.banner);
        sc = (ScrollView) view.findViewById(R.id.sc);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) banner.getLayoutParams();
        linearParams.height = App.H / 4;
        banner.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

        //简单使用
        banner.setImages(App.images)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(BlankFragment.this)
                .start();
        beans = new ArrayList<Bean>();
        beans.add(new Bean(R.mipmap.logo, "2017/07/13 9:34", "小肥羊暖冬新推“三羊大补锅”，为你补全这一年", "肥羊暖冬新推“三羊大补锅”，为你补全这一年 2016年12月1日，上海讯 — 专属于冬日的暖胃暖心滋补味道，来自小肥羊呈现的“三羊大补锅”，精炖细煮，成就这一年的圆满结尾。 小肥羊此番精选了骨大肉多的羊蝎子、羊腱骨和羔羊排，三大部位集结一锅，辅以香气浓郁的胡椒，在餐厅精炖慢煮而成。端上桌的这一大锅里，除了羊肉原汤和羊蝎、羊腱及羊排外，还配上了虫草花、枸杞、桂圆、山药和红枣等适合冬令进补的食材。", "http://www.littlesheep.com/franchise/index.html"));
        beans.add(new Bean(R.mipmap.logo, "2017/07/13 9:34", "豪情蒸腾、初心不忘，小肥羊迎来成立17周年", "情蒸腾、初心不忘，小肥羊迎来成立17周年 2016年7月22日，包头讯 — 1999年8月8日，小肥羊的全球首家门店在内蒙古包头市开幕，17年的火锅传奇由此开启。17年来，坚持呈现“一锅好汤”、“一盘好肉”的原汁美味，从享誉世界的锡林郭勒草原到日本、美国、加拿大，小肥羊的豪情蒸腾在全球华人餐桌上，“天然、健康、珍贵”的初心永葆不忘。 今年8月8日恰逢小肥羊17周年庆，届时将推出低至58", "http://www.littlesheep.com/franchise/index.html"));
        beans.add(new Bean(R.mipmap.logo, "2017/07/13 9:34", "每一口都是“肉麻”的味道！小肥羊盛夏新推“肉麻藤椒锅”", "一口都是“肉麻”的味道！小肥羊盛夏新推“肉麻藤椒锅” 2016年7月4日，上海讯 — 炎炎盛夏，你的胃口是否急需一场酣畅的火锅盛宴来提振？除了非红即白的锅底，小肥羊“肉麻藤椒锅”将在这个夏天带给吃货们不同的体验——视觉上，翠绿绿的藤椒和粉嫩嫩的鱼羊拼盘带来清爽快意；味觉上，麻辣鲜香，点燃夏日里的口感新体验——每一口，都是“肉麻”的味道！“肉麻藤椒锅”，每一口都是“肉麻”的味道 藤椒", "http://www.littlesheep.com/franchise/index.html"));
        beans.add(new Bean(R.mipmap.logo, "2017/07/13 9:34", "小肥羊升级新店形象，启动“草原有肉香”系列活动", "肥羊升级新店形象，启动“草原有肉香”系列活动 — 邂逅“舌尖上的小肥羊”，赢取草原之旅 —\t2016年5月18日，上海讯 — 小肥羊上海莲花广场餐厅开业在即，这家“小而美”的新店宣告着小肥羊旗下餐厅形象的新一轮升级。与此同时，一部由《舌尖上的中国2》导演陈硕团队全力打造的视频短片伴随着“草原有肉香”系列活动与消费者见面，通过微观视角魅力展现了小肥羊火锅食材的大千世界。 高颜值新店揭幕，超美", "http://www.littlesheep.com/franchise/index.html"));
        vmLayout = (VerticalMarqueeLayout<Bean>) view.findViewById(R.id.vmLayout);
        vmLayout.datas(beans, R.layout.item).builder(vmLayout.new OnItemBuilder() {
            @Override
            public void assemble(View view, Bean bean) {

                ImageView icon = (ImageView) view.findViewById(R.id.ico);
                TextView time = (TextView) view.findViewById(R.id.tim);
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView summary = (TextView) view.findViewById(R.id.summar);

                icon.setImageResource(bean.getIcon());
                time.setText(bean.getTime());
                title.setText(bean.getTitle());
                summary.setText(bean.getSummary());

            }
        }).listener(new VerticalMarqueeLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                Intent intent = new Intent(getActivity(), Web_Activity.class);

                //用Bundle携带数据
                Bundle bundle = new Bundle();
                //传递name参数为tinyphp
                bundle.putString("url", beans.get(position).getUrl());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }).commit();

        String[] urls = {"http://img2.imgtn.bdimg.com/it/u=77887371,1020959273&fm=26&gp=0.jpg", "http://img3.imgtn.bdimg.com/it/u=3307908037,129287858&fm=26&gp=0.jpg", "http://img0.imgtn.bdimg.com/it/u=3271581418,1199517348&fm=26&gp=0.jpg", "http://img2.imgtn.bdimg.com/it/u=3083163209,1782300715&fm=26&gp=0.jpg"};
//        String[] urls = {"", "", "", ""};
        String[] titles = {"↓精品羊肉双人套餐↓", "↓羊肉家庭三人套餐↓", "↓羊肉家庭四人套餐↓", "↓精品羊肉五人套餐↓"};
        String[] nrs = {"套餐价：220元  省50元", "套餐价：320元  省150元", "套餐价：350元  省150元", "套餐价：500元  省280元"};
        String url = "http://img1.imgtn.bdimg.com/it/u=2428294413,1726320754&fm=26&gp=0.jpg";
        String title = "众星捧月";
        String nr = "满满的一盘小鲜肉都在抱大腿，至于吗？要我说：放开那腿肉，让我来！";
        coursedList = new ArrayList<>();
        Map<String, Object> courseTopMap = new HashMap<>();
        courseTopMap.put("urls", urls);
        courseTopMap.put("titles", titles);
        courseTopMap.put("nrs", nrs);
        coursedList.add(courseTopMap);
        Map<String, Object> courseMap = new HashMap<>();
        courseMap.put("url", url);
        courseMap.put("title", title);
        courseMap.put("nr", nr);
        coursedList.add(courseMap);
        DetailListAdapter detailListAdapter = new DetailListAdapter(getActivity(), coursedList);

        listview.setFocusable(false);//在代码里去掉listview的焦点
        listview.setAdapter(detailListAdapter);
        Utility.setListViewHeightBasedOnChildren(listview);
        sc.smoothScrollTo(0, 20);//需在listview数据加载完成后调用
        scanner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, 100);

            }
        });
        return view;
    }


    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getContext(), "你点击了第" + position + "张轮播图片", Toast.LENGTH_SHORT).show();
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onResume() {
        super.onResume();
        VerticalMarqueeLayout.DURATION_SCROLL = 50;
        vmLayout.startScroll();
        vmLayout.setVisibility(View.VISIBLE);
        VerticalMarqueeLayout.DURATION_SCROLL = 3000;
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (vmLayout != null) {
            vmLayout.stopScroll();
        }
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onActivityResult(int arg0, int arg1, Intent data) {
        super.onActivityResult(arg0, arg1, data);

        /**
         * 拿到解析完成的字符串
         */
        if(data!=null){
        Toast.makeText(getContext(), data.getStringExtra("result"), Toast.LENGTH_SHORT).show();}

    }
}



