package com.example.mathu.trashtalk;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{
   public static int[] avatars = {
            R.drawable.avatar,
            R.drawable.avatar1,
            R.drawable.avatar2,
            R.drawable.avatar3,
            R.drawable.avatar4,
            R.drawable.avatar5
    };
    private Context context;
    public ImageAdapter(Context applicationContext)
    {
        context = applicationContext;
    }
    @Override
    public int getCount() {
        return avatars.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv;
        if(convertView != null)
        {
            iv = (ImageView) convertView;
        }
        else
        {
            iv = new ImageView(context);
            iv.setLayoutParams(new GridView.LayoutParams(100,100));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setPadding(8,8,8,8);
        }
        iv.setImageResource(avatars[position]);
        return iv;
    }
}
