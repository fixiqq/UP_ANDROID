package com.example.up;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_RW extends RecyclerView.Adapter<Adapter_RW.ViewHolder> {
    private List<Class_Feel> myObjects; // Список объектов для отображения


    public Adapter_RW(List<Class_Feel> myObjects) {
        this.myObjects = myObjects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Class_Feel myObject = myObjects.get(position);
        holder.textView.setText(myObject.title);
        Picasso.get().load(myObject.image).into(holder.imageView);
    }

    // Получение количества элементов списка
    @Override
    public int getItemCount() {
        return myObjects.size();
    }

    // Класс ViewHolder, содержит ссылки на виджеты элемента списка
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
