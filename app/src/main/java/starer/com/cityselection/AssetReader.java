package starer.com.cityselection;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Xinghang Yu on 2016/6/6.
 */
public class AssetReader {

    private Context context;

    public AssetReader(Context context) {
        this.context = context;
    }

    public JSONObject getJson(String name)
    {
        AssetManager assetManager = context.getAssets();
        String result = "";
        try {
            InputStream is = assetManager.open(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer stringBuffer = new StringBuffer();
            String str = null;
            while((str = br.readLine())!=null){
                stringBuffer.append(str);
            }
            return  new JSONObject(stringBuffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
