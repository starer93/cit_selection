package starer.com.cityselection;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/16.
 */
public class PrefectCityAdapter extends RecyclerView.Adapter {

    private ArrayList<PrefectCityModel> prefectCityModels = new ArrayList<>();
    private OnCitySelectListener onCitySelectListener = null;

    public PrefectCityAdapter(OnCitySelectListener onCitySelectListener) {
        this.onCitySelectListener = onCitySelectListener;
    }

    public void setPrefectCityModels(ArrayList<PrefectCityModel> CityModels) {
        this.prefectCityModels.clear();
        this.prefectCityModels.addAll(CityModels);
        notifyDataSetChanged();
    }

    public void select(int position)
    {
        prefectCityModels.get(position).setSelected(true);
        notifyItemChanged(position);
    }

    public void unSelect(int position)
    {
        prefectCityModels.get(position).setSelected(false);
        notifyItemChanged(position);
    }
    @Override
    public int getItemCount() {
        return prefectCityModels.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PrefectCityViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_perfect_city, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PrefectCityViewHolder prefectCityViewHolder = (PrefectCityViewHolder) holder;
        PrefectCityModel cityModel = prefectCityModels.get(position);
        if (cityModel.isHasHeader())
        {
            prefectCityViewHolder.txtHeader.setVisibility(View.VISIBLE);
            prefectCityViewHolder.txtHeader.setText(cityModel.getPinyin().substring(0,1).toUpperCase());
        }
        else {
            prefectCityViewHolder.txtHeader.setVisibility(View.GONE);
        }
        if(cityModel.isSelected())
        {
            prefectCityViewHolder.radioSelect.setChecked(true);
        }
        else
        {
            prefectCityViewHolder.radioSelect.setChecked(false);
        }
        prefectCityViewHolder.txtName.setText(cityModel.getName());
    }

    public class PrefectCityViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtHeader;
        TextView txtName;
        RadioButton radioSelect;

        public PrefectCityViewHolder(View itemView) {
            super(itemView);
            txtHeader = (TextView) itemView.findViewById(R.id.txt_header);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            radioSelect = (RadioButton) itemView.findViewById(R.id.radio_select);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCitySelectListener.onSelect(getAdapterPosition());
                }
            });
        }
    }
}
