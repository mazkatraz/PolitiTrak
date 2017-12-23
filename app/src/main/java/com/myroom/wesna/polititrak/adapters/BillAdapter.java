package com.myroom.wesna.polititrak.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myroom.wesna.polititrak.R;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillItemViewHolder>{
    private int numberOfItems;

    public BillAdapter(){
        numberOfItems = 25;
    }

    @Override
    public BillItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        boolean shouldAttachToParentImmediately = false;

        View billListItem = layoutInflater.inflate(R.layout.bill_list_item,
                                            parent,
                                            shouldAttachToParentImmediately);

        return new BillItemViewHolder(billListItem);
    }

    @Override
    public void onBindViewHolder(BillItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return numberOfItems;
    }

    public class BillItemViewHolder extends RecyclerView.ViewHolder{
        TextView billItemTitle;

        public BillItemViewHolder(View itemView) {
            super(itemView);

            billItemTitle = (TextView) itemView.findViewById(R.id.tv_bill_name);
        }

        public void bind(int position){
            billItemTitle.setText("Bill Item " + position);
        }
    }
}
