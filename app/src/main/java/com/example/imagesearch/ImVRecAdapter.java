package com.example.imagesearch;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagesearch.pojos.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImVRecAdapter extends RecyclerView.Adapter<ImVRecAdapter.ViewHolder> {

    List<ImageResult> imageResults;

    public ImVRecAdapter(List<ImageResult> imageResults) {
        this.imageResults = imageResults;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rec_im_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.with(MainActivity.getAppContext())
                .load(imageResults.get(position).getOriginal())
                .into(holder.uploadImage);
        holder.uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setImage(holder.uploadImage, position, imageResults);
            }
        });
    }

    public List<ImageResult> getImageResults() {
        return imageResults;
    }

    @Override
    public int getItemCount() {
        return imageResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView uploadImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            uploadImage = itemView.findViewById(R.id.upload_iv);
        }
    }
}
