package com.example.apichaya.addrealmsudent.adepter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apichaya.addrealmsudent.Model.ChemicalObjectBase;
import com.example.apichaya.addrealmsudent.R;

import java.util.ArrayList;

/**
 * Created by apple on 07/23/17.
 */
public class ChemicalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChemicalObjectBase> data = new ArrayList<>();

    private Context context;
    private OnItemClickListener onItemClickListener;

    public ChemicalAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        LayoutInflater inflater = null;
        ListHeaderViewHolder viewHolder;
        ListChildViewHolder childViewHolder;

        switch (type) {
            case ChemicalObjectBase.TYPE_HEADER:
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item_chemecal_header, parent, false);
                viewHolder = new ListHeaderViewHolder(view);
                return viewHolder;
            case ChemicalObjectBase.TYPE_BODY:
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item_chemecal_body, parent, false);
                childViewHolder = new ListChildViewHolder(view);
                return childViewHolder;
            case ChemicalObjectBase.TYPE_FOOTER:
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item_chemecal_footer, parent, false);
                childViewHolder = new ListChildViewHolder(view);
                return childViewHolder;
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ChemicalObjectBase chemicalObjectBase = data.get(position);
        switch (chemicalObjectBase.getType()) {
            case ChemicalObjectBase.TYPE_HEADER:
                final ListHeaderViewHolder viewHolder = (ListHeaderViewHolder) holder;
                viewHolder.txtTitle.setText(chemicalObjectBase.getName());
                viewHolder.txtPPM.setText(context.getString(R.string.ppm, chemicalObjectBase.getPpm()));
                viewHolder.imgAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener == null) {
                            return;
                        }
                        onItemClickListener.addItemClick(view, chemicalObjectBase.getId());
                    }
                });

                viewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener == null) {
                            return;
                        }
                        onItemClickListener.editItemClick(view, chemicalObjectBase.getId(), position);
                    }
                });
                break;
            case ChemicalObjectBase.TYPE_BODY:
                final ListChildViewHolder childViewHolder = (ListChildViewHolder) holder;
                childViewHolder.txtNumber.setText(String.valueOf(chemicalObjectBase.getNumber()));
                childViewHolder.txtRed.setText(String.valueOf(chemicalObjectBase.getRedValue()));
                childViewHolder.txtGreen.setText(String.valueOf(chemicalObjectBase.getGreenValue()));
                childViewHolder.txtBlue.setText(String.valueOf(chemicalObjectBase.getBlueValue()));

                childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener == null) {
                            return;
                        }
                        childViewHolder.itemView.setSelected(!childViewHolder.itemView.isSelected());
                        onItemClickListener.onItemClick(view, data.get(position).getId());
                    }
                });
                break;
            case ChemicalObjectBase.TYPE_FOOTER:
                final ListChildViewHolder footerViewHolder = (ListChildViewHolder) holder;
                footerViewHolder.txtNumber.setText(chemicalObjectBase.getName());
                footerViewHolder.txtRed.setText(String.format("%.2f", chemicalObjectBase.getAveRed()));
                footerViewHolder.txtGreen.setText(String.format("%.2f", chemicalObjectBase.getAveGreen()));
                footerViewHolder.txtBlue.setText(String.format("%.2f", chemicalObjectBase.getAveBlue()));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtPPM;
        private ImageView imgAdd;
        private ImageView imgEdit;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.textviewTitle);
            txtPPM = (TextView) itemView.findViewById(R.id.textviewPPM);
            imgAdd = (ImageView) itemView.findViewById(R.id.imageviewAdd);
            imgEdit = (ImageView) itemView.findViewById(R.id.imageviewEdit);
        }
    }

    private static class ListChildViewHolder extends RecyclerView.ViewHolder {
        private TextView txtRed;
        private TextView txtGreen;
        private TextView txtBlue;
        private TextView txtNumber;

        public ListChildViewHolder(View itemView) {
            super(itemView);
            txtRed = (TextView) itemView.findViewById(R.id.textviewRed);
            txtGreen = (TextView) itemView.findViewById(R.id.textviewGreen);
            txtBlue = (TextView) itemView.findViewById(R.id.textviewBlue);
            txtNumber = (TextView) itemView.findViewById(R.id.textviewNumber);
        }
    }

    public void setData(ArrayList<ChemicalObjectBase> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void updateChemicalName(int position, String name, int ppm) {
        this.data.get(position).setName(name);
        this.data.get(position).setPpm(ppm);
        notifyDataSetChanged();
    }

    public void removeAtPosition(int position) {
        this.data.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int itemId);

        void onItemRemove(View view, int itemId, int position);

        void addItemClick(View view, int chemicalId);

        void editItemClick(View view, int chemicalId, int position);

    }

    public void setOnClickListener(OnItemClickListener OnItemClickListener) {
        this.onItemClickListener = OnItemClickListener;
    }
}
