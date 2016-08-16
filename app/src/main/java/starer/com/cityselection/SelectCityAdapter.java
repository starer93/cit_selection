package starer.com.cityselection;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/16.
 */
public class SelectCityAdapter extends RecyclerView.Adapter {

    private ArrayList<PrefectCityModel> prefectCityModels = new ArrayList<>();
    private OnCitySelectListener onCitySelectListener = null;

    public SelectCityAdapter(ArrayList<PrefectCityModel> prefectCityModels,OnCitySelectListener onCitySelectListener) {
        this.onCitySelectListener = onCitySelectListener;
        this.prefectCityModels = prefectCityModels;
    }

    @Override
    public int getItemCount() {
        return prefectCityModels.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectCityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_city,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SelectCityViewHolder viewHolder = (SelectCityViewHolder) holder;
        viewHolder.txtCity.setText(prefectCityModels.get(position).getName());
    }

    public class SelectCityViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtCity;
        public SelectCityViewHolder(View itemView) {
            super(itemView);
            txtCity = (TextView) itemView.findViewById(R.id.txt_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCitySelectListener.onSelect(getAdapterPosition());
                }
            });
        }
    }

}
