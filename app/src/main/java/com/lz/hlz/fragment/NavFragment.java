package com.lz.hlz.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.lz.hlz.R;


import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavFragment extends BaseFragment implements View.OnClickListener {
    NavigationButton mNavNews;
    NavigationButton mNavTweet;
    NavigationButton mNavCart;
    NavigationButton mNavMe;
    private Context mContext;
    private int mContainerId;
    private FragmentManager mFragmentManager;
    private NavigationButton mCurrentNavButton;
    private OnNavigationReselectListener mOnNavigationReselectListener;

    public NavFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nav;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mNavNews = findView(R.id.nav_item_news);
        mNavTweet = findView(R.id.nav_item_tweet);
        mNavCart = findView(R.id.nav_item_explore);
        mNavMe = findView(R.id.nav_item_me);

        mNavNews.setOnClickListener(this);
        mNavTweet.setOnClickListener(this);
        mNavCart.setOnClickListener(this);
        mNavMe.setOnClickListener(this);

//        ShapeDrawable lineDrawable = new ShapeDrawable(new BorderShape(new RectF(0, 1, 0, 0)));
//        lineDrawable.getPaint().setColor(getResources().getColor(R.color.list_divider_color));
//        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
//                new ColorDrawable(getResources().getColor(R.color.white)),
//                lineDrawable
//        });
//        root.setBackgroundDrawable(layerDrawable);

        mNavNews.init(R.drawable.tab_icon_primary,
                R.string.main_tab_name_primary,
                BlankFragment.class);

        mNavTweet.init(R.drawable.tab_icon_cortgory,
                R.string.main_tab_name_category,
                MessageFragment.class);

        mNavCart.init(R.drawable.tab_icon_cart,
                R.string.main_tab_name_cart,
                MessageFragment.class);

        mNavMe.init(R.drawable.tab_icon_user,
                R.string.main_tab_name_user,
                MessageFragment.class);

    }

    @Override
    public void onClick(View v) {
        if (v instanceof NavigationButton) {

            NavigationButton nav = (NavigationButton) v;
            doSelect(nav, "");
        }
    }

    public void setup(Context context, FragmentManager fragmentManager, int contentId, OnNavigationReselectListener listener) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mContainerId = contentId;
        mOnNavigationReselectListener = listener;

        // do clear
        clearOldFragment();
        // do select first
        doSelect(mNavNews, "");
    }

    public void select(int index) {
        switch (index) {
            case 0:
                if (mNavNews != null)
                    doSelect(mNavNews, "");
                break;
            case 1:
                if (mNavTweet != null)
                    doSelect(mNavTweet, "");
                break;
            case 2:
                if (mNavTweet != null)
                    doSelect(mNavCart, "true");
                break;
        }

    }

    @SuppressWarnings("RestrictedApi")
    private void clearOldFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0)
            return;
        boolean doCommit = false;
        for (Fragment fragment : fragments) {
            if (fragment != this) {
                transaction.remove(fragment);
                doCommit = true;
            }
        }
        if (doCommit)
            transaction.commitNow();
    }

    private void doSelect(NavigationButton newNavButton, String flag) {
        // If the new navigation is me info fragment, we intercept it
        /*
        if (newNavButton == mNavMe) {
            if (interceptMessageSkip())
                return;
        }
        */

        NavigationButton oldNavButton = null;
        if (mCurrentNavButton != null) {

            oldNavButton = mCurrentNavButton;
            if (oldNavButton == newNavButton) {
                onReselect(oldNavButton);
                return;
            }
            oldNavButton.setSelected(false);
        }
        newNavButton.setSelected(true);
        doTabChanged(oldNavButton, newNavButton, flag);
        mCurrentNavButton = newNavButton;
    }

    private void doTabChanged(NavigationButton oldNavButton, NavigationButton newNavButton, String flag) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                Fragment fragment = Fragment.instantiate(mContext,
                        newNavButton.getClx().getName(), null);
                ft.add(mContainerId, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                ft.attach(newNavButton.getFragment());

            }
        }
        if (flag.equals("true")) {
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }

    }

    private void onReselect(NavigationButton navigationButton) {


        OnNavigationReselectListener listener = mOnNavigationReselectListener;
        if (listener != null) {
            listener.onReselect(navigationButton);
        }
    }

    public interface OnNavigationReselectListener {
        void onReselect(NavigationButton navigationButton);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
