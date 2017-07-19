package com.lz.hlz.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lz.hlz.R;
import com.lz.hlz.loadimage.util.ImageLoader;
import java.util.List;
import java.util.Map;

/**
 * 首页多布局列表adapter
 */
public class DetailListAdapter extends BaseAdapter {
    private ImageLoader mImageLoader;
    private Activity mContext;                        //运行上下文
    private List<Map<String, Object>> listItems;    //列表信息集合
    private LayoutInflater inflater;           //视图容器
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;

    public DetailListAdapter(Activity context, List<Map<String, Object>> listItems) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);   //创建视图容器并设置上下文
        mImageLoader = ImageLoader.getInstance(2, ImageLoader.Type.LIFO);
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 根据数据列表的position返回需要展示的layout的对应的type
     * type的值必须从0开始
     */
    @Override
    public int getItemViewType(int position) {
        int p = position;
        if (p == 0)
            return TYPE_1;
        else
            return TYPE_2;
    }

    /**
     * 该方法返回多少个不同的布局
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            inflater = LayoutInflater.from(mContext);
            // 按当前所需的样式，确定new的布局
            switch (type) {
                case TYPE_1:
                    convertView = inflater.inflate(R.layout.ls_mainlist_1_item,
                            parent, false);
                    holder1 = new ViewHolder1();
                    holder1.title_1 = (TextView) convertView
                            .findViewById(R.id.title_1);
                    holder1.title_2 = (TextView) convertView
                            .findViewById(R.id.title_2);
                    holder1.title_3 = (TextView) convertView
                            .findViewById(R.id.title_3);
                    holder1.title_4 = (TextView) convertView
                            .findViewById(R.id.title_4);
                    holder1.jg1 = (TextView) convertView.findViewById(R.id.jg1);
                    holder1.jg2 = (TextView) convertView.findViewById(R.id.jg2);
                    holder1.jg3 = (TextView) convertView.findViewById(R.id.jg3);
                    holder1.jg4 = (TextView) convertView.findViewById(R.id.jg4);
                    holder1.img1 = (ImageView) convertView.findViewById(R.id.img1);
                    holder1.img2 = (ImageView) convertView.findViewById(R.id.img2);
                    holder1.img3 = (ImageView) convertView.findViewById(R.id.img3);
                    holder1.img4 = (ImageView) convertView.findViewById(R.id.img4);
                    convertView.setTag(holder1);
                    break;
                case TYPE_2:
                    convertView = inflater.inflate(R.layout.ls_mainlist_2_item,
                            parent, false);
                    holder2 = new ViewHolder2();
                    holder2.lo2_title_1 = (TextView) convertView
                            .findViewById(R.id.lo2_title_1);
                    holder2.lo2_title_2 = (TextView) convertView
                            .findViewById(R.id.lo2_title_2);
                    holder2.lo2_img = (ImageView) convertView.findViewById(R.id.lo2_img);
                    convertView.setTag(holder2);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case TYPE_1:
                    holder1 = (ViewHolder1) convertView.getTag();
                    break;
                case TYPE_2:
                    holder2 = (ViewHolder2) convertView.getTag();
                    break;
            }
        }
        // 设置资源
        switch (type) {
            case TYPE_1:
                String[] urls = (String[]) listItems.get(position).get("urls");
                String[] titles = (String[]) listItems.get(position).get("titles");
                String[] nrs = (String[]) listItems.get(position).get("nrs");
                holder1.title_1.setText(titles[0]);
                holder1.title_2.setText(titles[1]);
                holder1.title_3.setText(titles[2]);
                holder1.title_4.setText(titles[3]);
                holder1.jg1.setText(nrs[0]);
                holder1.jg2.setText(nrs[1]);
                holder1.jg3.setText(nrs[2]);
                holder1.jg4.setText(nrs[3]);
                holder1.img1.setImageResource(R.drawable.pictures_no);
                holder1.img2.setImageResource(R.drawable.pictures_no);
                holder1.img3.setImageResource(R.drawable.pictures_no);
                holder1.img4.setImageResource(R.drawable.pictures_no);
                mImageLoader.loadImage(urls[0], holder1.img1, true);
                mImageLoader.loadImage(urls[1], holder1.img2, true);
                mImageLoader.loadImage(urls[2], holder1.img3, true);
                mImageLoader.loadImage(urls[3], holder1.img4, true);
                break;
            case TYPE_2:
                String url = (String) listItems.get(position).get("url");
                String nr = (String) listItems.get(position).get("nr");
                String title = (String) listItems.get(position).get("title");
                holder2.lo2_img.setImageResource(R.drawable.pictures_no);
                holder2.lo2_title_1.setText(title);
                holder2.lo2_title_2.setText(nr);
                mImageLoader.loadImage(url, holder2.lo2_img, true);
                break;
        }
        return convertView;
    }

    public class ViewHolder1 {
        TextView title_1, title_2, title_3, title_4, jg1, jg2, jg3, jg4;
        ImageView img1, img2, img3, img4;
    }

    public class ViewHolder2 {
        TextView lo2_title_1, lo2_title_2;
        ImageView lo2_img;
    }

}