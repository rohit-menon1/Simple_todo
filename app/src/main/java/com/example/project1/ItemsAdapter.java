package com.example.project1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{
    List<String> items;
    OnClickListener clickListener;
    OnLongClickListener longClickListener;

    public interface OnClickListener{
        void onItemClicked(int position);
    }
    public interface OnLongClickListener{
        void onItemLongClicked(int position);

    }

    public ItemsAdapter(List<String> items,OnLongClickListener longClickListener,OnClickListener clickListener) {
        this.items=items;
        this.longClickListener=longClickListener;
        this.clickListener=clickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //use layout inflator to inflate a view
        View todoView=LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
        return new ViewHolder(todoView);
    }

    @Override
    //binding data to a particular view holder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //grab item
        String item=items.get(position);
        //bind item to specific view holder
        holder.bind(item);
    }
//how many items in the list
    @Override
    public int getItemCount() {
        return items.size();

    }

    //container to provide easy access to viewholder
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem=itemView.findViewById(android.R.id.text1);
        }

        public void bind(String item) {
            //update the view inside viewholder with the data form the stirng holder
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //notfying listener which postion was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());

                    return true;
                }
            });
        }
    }
}
