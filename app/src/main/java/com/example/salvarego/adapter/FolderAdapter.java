package com.example.salvarego.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salvarego.R;
import com.example.salvarego.model.folder_item;
import com.example.salvarego.model.FileItem;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private List<folder_item> folderList;

    public FolderAdapter(List<folder_item> folderList) {
        this.folderList = folderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_item, parent, false);
        return new ViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(folder_item folderItem);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        folder_item folderItem = folderList.get(position);
        holder.folderNameText.setText(folderItem.getFoldername());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(folderItem);
            }
        });
    }

    public void updateData(List<folder_item> newFolderList) {
        this.folderList = newFolderList;
        notifyDataSetChanged(); // Notify the adapter of data changes
    }


    @Override
    public int getItemCount() {
        return folderList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderNameText;

        ViewHolder(View itemView) {
            super(itemView);
            folderNameText = itemView.findViewById(R.id.folderNameTextView);
        }
    }
}
