package com.adriano.buscacep;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import java.util.List;
 
public class MainActivity extends AppCompatActivity {
 
    private ProgressDialog pDialog;
    private ListView lv;
 
    List<HashMap<String, String>> cepList;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        cepList = new ArrayList<>();
 
        lv = findViewById(R.id.list_view);
	}
		
	public void btnBuscarCep(View view) {
		EditText editUf = findViewById(R.id.edit_uf);
		EditText editLoc = findViewById(R.id.edit_loc);
		EditText editLog = findViewById(R.id.edit_log);
		new LerCepJsonTask().execute(        
		 "http://viacep.com.br/ws/"
		 +editUf.getEditableText().toString() 
		 +"/"+editLoc.getEditableText().toString()
		 +"/"+editLog.getEditableText().toString()
		 +"/json/");
    }

    private class LerCepJsonTask extends AsyncTask<String, Void, Void> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
			
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Conectando ao ViaCep...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        @Override
        protected Void doInBackground(String... args) {
            HttpConnection conn = new HttpConnection();
			
			String url = args[0];
            String jsonStr = conn.getHttpData(url);
 
            if (jsonStr != null) {
                try {
					JSONArray cepJson = new JSONArray(jsonStr);

					for (int i = 0; i < cepJson.length(); i++) {
						JSONObject jsonObject = new JSONObject(cepJson.getString(i));
						
                        String cep = jsonObject.getString("cep");
                        String logradouro = jsonObject.getString("logradouro");
                        String bairro = jsonObject.getString("bairro");
                        String localidade = jsonObject.getString("localidade");
						String uf = jsonObject.getString("uf");
 
                        HashMap<String, String> h = new HashMap<>();
                        	h.put("cep", cep);
                        	h.put("logradouro", logradouro);
                        	h.put("bairro", bairro);
							h.put("localidade", localidade);
							h.put("uf", uf);
 
						cepList.add(h);
					}
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json file.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
			
            if (pDialog.isShowing())
                pDialog.dismiss();
            
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, cepList,
                    R.layout.item_layout, new String[]{"cep", "logradouro",
                    "bairro", "localidade", "uf"},
				new int[]{R.id.txt_cep, R.id.txt_logradouro, R.id.txt_bairro, 
							R.id.txt_localidade, R.id.txt_uf});
 
            lv.setAdapter(adapter);
        }
    }
}
