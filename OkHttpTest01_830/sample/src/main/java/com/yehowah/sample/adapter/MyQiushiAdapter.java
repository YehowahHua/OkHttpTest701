package com.yehowah.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yehowah.sample.R;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/9/12.
 */

public class MyQiushiAdapter extends BaseAdapterHelper<Map<String,String>> {
    private final static int TYPE1 = 0,TYPE2 = 1;
    private Context context;
    private ViewHolder1 mHolder1;
    private ViewHolder2 mHolder2;
    private List<Map<String,String>> list = null;
    public MyQiushiAdapter(List<Map<String, String>> list, Context context) {
        super(list, context);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public int getItemViewType(int position) {
        String imageUrl = getImageUrl(list.get(position).get("image"));
        return super.getItemViewType(position);
    }
    @Override
    public View getItemView(int position, View convertView, ViewGroup parent, List<Map<String, String>> list, LayoutInflater inflater) {
        int type = getItemViewType(position);
        if (convertView == null){
            switch (type){
                case TYPE1:
                    convertView = inflater.inflate(R.layout.item_listview_main1,parent,false);
                    mHolder1 = new ViewHolder1(convertView);
                    convertView.setTag(mHolder1);
                    break;
                case TYPE2:

            }
        }

        return null;
    }

    private String getImageUrl(String image) {
    }

    public void reloadData(List<Map<String,String>> data,boolean isClear){
        if (isClear){
            list.clear();
        }
        list.addAll(data);
        notifyDataSetChanged();
    }


    static class ViewHolder2{
        private TextView textView_item_content;
        private TextView textView_item_login;
        private TextView textView_item_commentCount;

        public ViewHolder2(View convertView){
            textView_item_content = (TextView) convertView.findViewById(R.id.textView_item_content);
            textView_item_login = (TextView) convertView.findViewById(R.id.textView_item_login);
            textView_item_commentCount = (TextView) convertView.findViewById(R.id.textView_item_commentsCount);
        }
    }
    class ViewHolder1{
        private ImageView imageView_item_show;
        private TextView textView_item_content;
        private TextView textView_item_login;
        private TextView textView_item_commentCount;

        public ViewHolder1(View convertView){
            imageView_item_show = (ImageView) convertView.findViewById(R.id.imageView_item_show);
            textView_item_content = (TextView) convertView.findViewById(R.id.textView_item_content);
            textView_item_login = (TextView) convertView.findViewById(R.id.textView_item_login);
            textView_item_commentCount = (TextView) convertView.findViewById(R.id.textView_item_commentsCount);
        }
    }


}