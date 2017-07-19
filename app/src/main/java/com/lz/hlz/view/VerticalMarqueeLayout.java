package com.lz.hlz.view;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccwxf on 2016/8/1.
 */
public class VerticalMarqueeLayout<T> extends ViewGroup {
    //滚动间隔时间 和滚动动画时间
    public static int DURATION_SCROLL = 50;
    public static final int DURATION_ANIMATOR = 1000;
    //实体集合和子控件集合
    private List<T> beans = new ArrayList<T>();
    private List<View> views = new ArrayList<View>(2);
    private int itemLayoutId;
    private Handler handler = new Handler();
    //宽度和高度（包括padding）
    private int width;
    private int height;
    //第一个子View的中点Y坐标
    private int centerY;
    //是否结束滚动
    private boolean isStopScroll = true;
    //当前的索引
    private int current;
    private OnItemClickListener listener;
    private OnItemBuilder builder;

    public VerticalMarqueeLayout(Context context) {
        super(context);
    }

    public VerticalMarqueeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalMarqueeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getCurrentIndex() {
        return current;
    }

    public VerticalMarqueeLayout listener(OnItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    public VerticalMarqueeLayout builder(OnItemBuilder builder) {
        this.builder = builder;
        return this;
    }

    /**
     * 设置实体集合和item布局id
     */
    public VerticalMarqueeLayout datas(List<T> beans, int itemLayoutId) {
        this.beans.clear();
        this.beans.addAll(beans);
        this.itemLayoutId = itemLayoutId;
        return this;
    }

    public void commit() {
        if (builder == null) {
            throw new IllegalStateException("must invoke the method [builder(OnItemBuilder)]");
        }
        this.views.clear();
        if (beans != null && beans.size() != 0) {
            View view = View.inflate(getContext(), itemLayoutId, null);
            //在这里填充布局参数
            if (builder != null) {
                builder.builder(view, beans.get(0));
            }
            this.views.add(view);
            //这里通过手动设置全屏宽度的方式add
            addViewWidthMatchParent(view);
            //如果大于等于2个，初始化第二个View
            if (beans.size() > 1) {
                View view1 = View.inflate(getContext(), itemLayoutId, null);
                if (builder != null) {
                    builder.builder(view1, beans.get(1));
                }
                this.views.add(view1);
                addViewWidthMatchParent(view1);
            }
            //手动触发onMeasure和onDraw
            LayoutParams params = getLayoutParams();
            if (params != null) {
                setLayoutParams(params);
                invalidate();
            }
            current = 0;
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(current);
                    }
                }
            });
        }
    }

    private void addViewWidthMatchParent(View view) {
        LayoutParams params = new LayoutParams(view.getMeasuredWidth(), view.getMeasuredHeight());
        addView(view, params);
    }

    public void startScroll() {
        stopScroll();
        if (views.size() > 1) {
            isStopScroll = false;
            if (!isStopScroll) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroll();
                        if (!isStopScroll) {
                            handler.postDelayed(this, DURATION_SCROLL);
                        }
                    }
                }, DURATION_SCROLL);
            }
        }
    }

    private void scroll() {
        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(
                PropertyValuesHolder.ofInt("centerY", height / 2, -height / 2)).setDuration(DURATION_ANIMATOR);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                centerY = (Integer) animation.getAnimatedValue("centerY");
                //手动布局
                change(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束，进行迭代
                current++;
                //删除第一个View，然后加入到最后一个的位置
                removeViewAt(0);
                View view = views.remove(0);
                views.add(view);
                addViewWidthMatchParent(view);
                //边界检查
                if (current == beans.size() - 1) {
                    if (builder != null) {
                        builder.builder(views.get(0), beans.get(current));
                        builder.builder(view, beans.get(0));
                    }
                } else if (current == beans.size()) {
                    current = 0;
                    if (builder != null) {
                        builder.builder(views.get(0), beans.get(current));
                        builder.builder(view, beans.get(current + 1));
                    }
                } else {
                    if (builder != null) {
                        builder.builder(views.get(0), beans.get(current));
                        builder.builder(view, beans.get(current + 1));
                    }
                }
                //每一次动画结束，都触发onMeasure和onDraw，防止每一个Item的高度发生变化而出现错位
                LayoutParams params = getLayoutParams();
                setLayoutParams(params);
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }

    public void stopScroll() {
        isStopScroll = true;
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (beans.size() == 0 || views.size() == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
            //这里必须再次measure一次，因为此时width才真正意义上有值
            views.get(0).measure(MeasureSpec.makeMeasureSpec(width - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height = views.get(0).getMeasuredHeight() + getPaddingTop() + getPaddingBottom();
            centerY = height / 2;
            views.get(1).measure(MeasureSpec.makeMeasureSpec(width - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //第一次布局时
        if (changed) {
            change(l + getPaddingLeft(), t + getPaddingTop(), r - getPaddingRight(), b - getPaddingBottom());
        }
    }

    private void change(int l, int t, int r, int b) {
        if (views.size() != 0) {
            //布局第一个View
            int dy = height / 2 - centerY;
            views.get(0).layout(l, t - dy, r, b - dy);
            //布局第二个View
            if (views.size() > 1) {
                views.get(1).layout(l, t + height - dy, r, b + height - dy);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public abstract class OnItemBuilder {
        public abstract void assemble(View view, T t);

        private void measure(View view) {
            view.measure(MeasureSpec.makeMeasureSpec(width - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        }

        public void builder(View view, T t) {
            //先装配数据
            assemble(view, t);
            //重新测量
            measure(view);
        }
    }
}
