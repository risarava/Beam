package com.example.apichaya.addrealmsudent.adepter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apichaya.addrealmsudent.Model.RgbColorObject;
import com.example.apichaya.addrealmsudent.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context mContext;
    private CardView cvItem;
    private OnItemDelete onItemDelete;

    private ArrayList<RgbColorObject> rgbColorObjectArrayList = new ArrayList<>();

    public MyAdapter(Context context, ArrayList<RgbColorObject> rgbColorObjectArrayList) {
        this.mContext = context;
        this.rgbColorObjectArrayList = rgbColorObjectArrayList;
    }


    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {
        holder.txtNumber.setText(String.valueOf(position + 1));
        holder.tvRedvalue.setText(String.valueOf(rgbColorObjectArrayList.get(position).getRedValue()));
        holder.tvGreenvalue.setText(String.valueOf(rgbColorObjectArrayList.get(position).getGreenValue()));
        holder.tvBluevalue.setText(String.valueOf(rgbColorObjectArrayList.get(position).getBlueValue()));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemDelete == null) {
                    return;
                }
                onItemDelete.onClicked(position, rgbColorObjectArrayList.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return rgbColorObjectArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNumber;
        private TextView tvRedvalue;
        private TextView tvGreenvalue;
        private TextView tvBluevalue;
        private ImageView imgDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtNumber = (TextView) itemView.findViewById(R.id.textviewNumber);
            tvRedvalue = (TextView) itemView.findViewById(R.id.textviewRed);
            tvGreenvalue = (TextView) itemView.findViewById(R.id.textviewGreen);
            tvBluevalue = (TextView) itemView.findViewById(R.id.textviewBlue);
            imgDelete = (ImageView) itemView.findViewById(R.id.imageviewDelete);
        }
    }

    public void setRgbColorObjectArrayList(ArrayList<RgbColorObject> rgbColorObjectArrayList) {
        this.rgbColorObjectArrayList = rgbColorObjectArrayList;
        notifyDataSetChanged();
    }

    public void addRgbColor(RgbColorObject rgbColorObjectArrayList) {
        this.rgbColorObjectArrayList.add(rgbColorObjectArrayList);
        notifyDataSetChanged();
    }

    public void removePosition(int position) {
        if (position < this.rgbColorObjectArrayList.size()) {
            this.rgbColorObjectArrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public interface OnItemDelete {
        void onClicked(int position, int id);
    }

    public void setOnItemDelete(OnItemDelete onItemDelete) {
        this.onItemDelete = onItemDelete;
    }
}