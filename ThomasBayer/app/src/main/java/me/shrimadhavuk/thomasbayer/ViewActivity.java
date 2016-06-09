package me.shrimadhavuk.thomasbayer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ViewActivity extends ListActivity {

    private static final String TAG = "me.shrimadhavuk";
    TextView outputTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        outputTxt = (TextView) findViewById(R.id.output);

        String[] rows = intent.getStringArrayExtra("arrayList");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rows);

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // ListView Clicked item index
        int itemPosition     = position;
        // ListView Clicked item value
        String  itemValue    = (String) l.getItemAtPosition(position);
        String response = getInternetDataAsString(itemValue);
        outputTxt.setText(response);

        Toast.makeText(this, "I do not know how to parse this XML!", Toast.LENGTH_LONG).show();
    }

    public String getInternetDataAsString(String base_url) {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(base_url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            //urlConnection.setDoInput(true);
            //urlConnection.setDoOutput(true);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseOutput.append(line);
            }
            br.close();

            Log.i(TAG, String.valueOf(responseOutput));
            return String.valueOf(responseOutput);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    /*public ArrayList formatXML(String xml){
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));

            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName(root);
            ArrayList<String> responses = new ArrayList<String>();
            for (int i = 0; i < nodes.getLength(); i++) {
                // Get element
                Element element = (Element) nodes.item(i);
                Node nNode = nodes.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String attribute = eElement.getAttribute("xlink:href");
                    responses.add(attribute);
                }
            }
            return responses;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }*/

}
