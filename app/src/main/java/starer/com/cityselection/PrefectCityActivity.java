package starer.com.cityselection;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import butterknife.BindView;
import butterknife.OnClick;

import starer.com.cityselection.Base.BaseActivity;

public class PrefectCityActivity extends BaseActivity {

    private ArrayList<PrefectCityModel> demesticList = new ArrayList<>();
    private ArrayList<PrefectCityModel> otherList = new ArrayList<>();

    private ArrayList<PrefectCityModel> sortedList = new ArrayList<>();

    private ArrayList<PrefectCityModel> selectList = new ArrayList<>();

    private HashMap<String, Integer> chinaHeaders = new HashMap<>();
    private HashMap<String, Integer> otherHeaders = new HashMap<>();
    private String[] index = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N",
            "O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    private ArrayList<String> indexList = new ArrayList<>();


    private Hashtable<String, ArrayList<PrefectCityModel>> cityTable = new Hashtable<>();

    private boolean isChina = true;
    private PrefectCityAdapter adapter;
    private SelectCityAdapter selectedAdapter;

    @BindView(R.id.letter_list)
    LetterView letterView;

    @BindView(R.id.rv_city)
    RecyclerView rvCity;

    @BindView(R.id.txt_current_city)
    TextView txtCurrentCity;

    @BindView(R.id.btn_china)
    Button btnChina;

    @BindView(R.id.btn_international)
    Button btnInternational;

    @BindView(R.id.rv_selected)
    RecyclerView rvSelect;

    @BindView(R.id.txt_number)
    TextView txtNumber;


    @OnClick({R.id.btn_china, R.id.btn_international})
    void selectRegion(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_china:
                if(!isChina)
                {
                    isChina = true;
                    btnChina.setBackgroundResource(R.drawable.country_select2);
                    btnChina.setTextColor(Color.WHITE);
                    btnInternational.setTextColor(Color.BLACK);
                    btnInternational.setBackgroundResource(R.drawable.country_select);
                    adapter.setPrefectCityModels(demesticList);
                }break;
            case R.id.btn_international:
                if(isChina)
                {
                    isChina = false;
                    btnChina.setTextColor(Color.BLACK);
                    btnInternational.setTextColor(Color.WHITE);
                    view.setBackgroundResource(R.drawable.country_select2);
                    btnChina.setBackgroundResource(R.drawable.country_select);
                    if(otherList.isEmpty())
                    {MyAsyncTasks asyncTasks = new MyAsyncTasks("global_city.json");
                        asyncTasks.execute();
                        //  loadList("global_city.json");
                    }
                    else
                    {
                        adapter.setPrefectCityModels(otherList);
                    }
                }
                break;
            default: break;
        }
    }


    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initIndex();
        txtNumber.setText(selectList.size()+"");
        initRecycleList();
        MyAsyncTasks asyncTasks = new MyAsyncTasks("demestic_city.json");
        asyncTasks.execute();
       // loadList("demestic_city.json");
        initLetterView();
    }

    private void initIndex()
    {
        for(int i = 0 ; i < index.length; i++)
        {
            indexList.add(index[i]);
        }
    }

    private void initRecycleList()
    {
        adapter = new PrefectCityAdapter( new OnCitySelectListener() {
            @Override
            public void onSelect(int position) {
                if(isChina)
                {
                    selectCity(demesticList, position);
                }
                else {
                    selectCity(otherList, position);
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCity.setLayoutManager(linearLayoutManager);
        rvCity.setAdapter(adapter);

        selectedAdapter = new SelectCityAdapter(selectList, new OnCitySelectListener() {
            @Override
            public void onSelect(int position) {
                adapter.unSelect(selectList.get(position).getIndex());
                selectList.remove(position);
                selectedAdapter.notifyDataSetChanged();
                txtNumber.setText(selectList.size()+"");

            }
        });

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getApplicationContext());
        verticalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvSelect.setAdapter(selectedAdapter);
        rvSelect.setLayoutManager(verticalLayoutManager);
    }

    private void unselectCity(PrefectCityModel prefectCityModel)
    {
        for(int i = 0; i <selectList.size();i++)
        {
            if(prefectCityModel.getIndex() == selectList.get(i).getIndex())
            {
                selectList.remove(i);
                selectedAdapter.notifyDataSetChanged();
                txtNumber.setText(selectList.size()+"");
            }
        }
    }
    private void selectCity(ArrayList<PrefectCityModel> list, int position)
    {
        PrefectCityModel model = list.get(position);
        if(model.isSelected()) //if city is already selected
        {
            list.get(position).setSelected(false);
            adapter.unSelect(position);
            unselectCity(model);
        }
        else
        {
            model.setSelected(true);
            adapter.select(position);
            selectList.add(model);
            selectedAdapter.notifyDataSetChanged();
            txtNumber.setText(selectList.size()+"");

        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_prefect_city;
    }

    private ArrayList<PrefectCityModel> loadList(String filename)
    {
        AssetReader reader = new AssetReader(getBaseContext());
        ArrayList<PrefectCityModel> preloaddList = new ArrayList<>();
        JSONObject dCity = reader.getJson(filename);
        try {
            JSONArray citys = dCity.getJSONArray("city");
            for(int i = 0; i < citys.length(); i++)
            {
                PrefectCityModel city = new PrefectCityModel();
                JSONObject cityItem = citys.getJSONObject(i);
                city.setName(cityItem.getString("city_name"));
                preloaddList.add(city);
            }
            addedPinYin(preloaddList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return preloaddList;
    }


    private void addedPinYin(ArrayList<PrefectCityModel> citys)
    {
        for(PrefectCityModel prefectCityModel: citys)
        {
            String pinyin = Pinyin4jUtil.converterToSpell(prefectCityModel.getName()).toUpperCase();
            prefectCityModel.setPinyin(pinyin);
        }
    }

    private void initLetterView()
    {
        letterView.setAlphabet(indexList);
        letterView.setOnTouchLetterChangedListener(new LetterView.OnTouchLetterChangedListener() {
            @Override
            public void onTouchLetterChangedListener(int index) {
                int position = -1;
                if(isChina)
                {
                   position = chinaHeaders.get(indexList.get(index));
                }
                else {
                    position = otherHeaders.get(indexList.get(index));
                }
                if(position > 0)
                {
                    rvCity.smoothScrollToPosition(position);
                }
            }
        });
        letterView.setOnTouchLetterReleasedListener(new LetterView.OnTouchLetterReleasedListener() {
            @Override
            public void onTouchLetterReleasedListener(String s) {

            }
        });
    }

    private void sortList(ArrayList<PrefectCityModel> preloaddList)
    {
        cityTable.clear();
            for(PrefectCityModel cityModel:preloaddList)
            {
                if(cityTable.containsKey(cityModel.getPinyin().substring(0,1)))
                {
                    cityTable.get(cityModel.getPinyin().substring(0,1)).add(cityModel);
                }
                else
                {
                    ArrayList<PrefectCityModel> citys =  new ArrayList<>();
                    citys.add(cityModel);
                    cityTable.put(cityModel.getPinyin().substring(0,1), citys);
                }
            }

        for(int j = 0; j <indexList.size(); j++)
        {
            boolean hasHeader = true;
            ArrayList<PrefectCityModel> sortedCity = cityTable.get(indexList.get(j));
            if(sortedCity != null)
            {
                for(int i =0; i< sortedCity.size(); i++)
                {
                    PrefectCityModel c = sortedCity.get(i);
                    if(hasHeader)
                    {
                        c.setHasHeader(true);
                        sortedList.add(c);
                        hasHeader = false;
                    }
                    else
                    {
                        sortedList.add(c);
                    }
                }
            }
            else
            {
                indexList.remove(j);
                j--;
            }
        }
        preloaddList.clear();
        for(int i = 0; i < sortedList.size(); i++)
        {
            sortedList.get(i).setIndex(i);
        }
    }

    private void getHeaderIndex(ArrayList<PrefectCityModel> cityModels, HashMap<String, Integer> maps)
    {
        for(int i = 0; i< cityModels.size(); i++)
        {
            if(cityModels.get(i).isHasHeader())
            {
                maps.put(cityModels.get(i).getPinyin().substring(0,1), i);
            }
        }
    }

    private class MyAsyncTasks extends AsyncTask<Void, Void, ArrayList<PrefectCityModel>> {
        String name;
        public MyAsyncTasks(String name) {
            super();
            this.name = name;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(ArrayList<PrefectCityModel> cityModels) {
            super.onPostExecute(cityModels);
            sortList(cityModels);
            if(isChina)
            {
                demesticList.clear();
                demesticList.addAll(sortedList);
                getHeaderIndex(demesticList, chinaHeaders);
                adapter.setPrefectCityModels(demesticList);
            }
            else
            {
                otherList.clear();
                otherList.addAll(sortedList);
                getHeaderIndex(otherList, otherHeaders);
                adapter.setPrefectCityModels(otherList);
            }
            sortedList.clear();
            dismissProgressDialog();
        }

        @Override
        protected ArrayList<PrefectCityModel> doInBackground(Void... voids) {
            return loadList(name);
        }
    }
}
