package com.example.up.GalleryDirectory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.up.Class_Photo;
import com.example.up.PhotoActivity;
import com.example.up.R;

import java.util.List;

public class MyAdapter extends ArrayAdapter<Class_Photo> {
    private Context mContext;
    private List<Class_Photo> mData;
    private boolean mShowButton = false;
    private Activity mActivity;
    private static final int REQUEST_CODE_SELECT_IMAGE = 100;
    public MyAdapter(Activity activity,Context context, List<Class_Photo> data) {
        super(context, R.layout.gallery_item, data);
        mActivity = activity;
        mContext = context;
        mData = data;
    }
    @Override
    public int getCount() {
        if (mShowButton) {
            return mData.size() + 1;
        } else {
            return mData.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size()) {
            return 1;
        } else {
            return 0;
        }
    }

    static class ViewHolder {
        ImageView image;
        TextView time;
        Button addButton;
    }

    public void setData(List<Class_Photo> data) {
        mData = data;
        notifyDataSetChanged();
    }
    public void setShowButton(boolean showButton) {
        mShowButton = showButton;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.gallery_item, parent, false);

            holder = new ViewHolder();
            holder.image = view.findViewById(R.id.image);
            holder.time = view.findViewById(R.id.time);
            holder.addButton = view.findViewById(R.id.add_btn);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (position == mData.size() && mShowButton) {
            // последний элемент, отображаем кнопку
            holder.image.setVisibility(View.INVISIBLE);
            holder.time.setVisibility(View.INVISIBLE);
            holder.addButton.setVisibility(View.VISIBLE);
            holder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    mActivity.startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
                    //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
                }
            });
        } else {
            // обычный элемент, используем данные фото
            holder.image.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);
            holder.addButton.setVisibility(View.INVISIBLE);
            Class_Photo data = mData.get(position);
            holder.image.setImageBitmap(data.image);
            holder.time.setText(data.timestamp);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PhotoActivity.class);
                    intent.putExtra("image_id", position);
                    mContext.startActivity(intent);
                }
            });
        }
        return view;
    }

}




