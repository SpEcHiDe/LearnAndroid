package me.shrimadhavuk.thomasbayer;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "me.shrimadhavuk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    public void productsVwAcn(View v){
        // fetch from internet
        String response = getInternetDataAsString("http://thomas-bayer.com/sqlrest");
        String productsurl = response.substring(response.indexOf("PRODUCTList")+11+13, response.indexOf("PRODUCTList")+11+13+31+9);
        // process XML

        String arrayofresponses = getInternetDataAsString(productsurl);
        ArrayList<String> rows = getCount(arrayofresponses, "PRODUCT");
        Log.i(TAG, String.valueOf(rows));

        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("arrayList", rows.toArray(new String[rows.size()]));
        startActivity(intent);

    }

    public void itemsVwAcn(View v){
        // fetch from internet
        String response = getInternetDataAsString("http://thomas-bayer.com/sqlrest");
        String itemsurl = response.substring(response.indexOf("ITEMList")+8+13, response.indexOf("ITEMList")+8+13+31+6);
        // process XML
        String arrayofresponses = getInternetDataAsString(itemsurl);
        ArrayList<String> rows = getCount(arrayofresponses, "ITEM");
        Log.i(TAG, String.valueOf(rows));

        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("arrayList", rows.toArray(new String[rows.size()]));
        //startActivity(intent);

        Toast.makeText(this, "I do not know how to parse this XML!", Toast.LENGTH_LONG).show();

    }

    public void invoiceVwAcn(View v){
        // fetch from internet
        String response = getInternetDataAsString("http://thomas-bayer.com/sqlrest");
        String invoiceurl = response.substring(response.indexOf("INVOICEList")+11+13, response.indexOf("INVOICEList")+11+13+31+9);
        // process XML
        String arrayofresponses = getInternetDataAsString(invoiceurl);
        ArrayList<String> rows = getCount(arrayofresponses, "INVOICE");
        Log.i(TAG, String.valueOf(rows));

        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("arrayList", rows.toArray(new String[rows.size()]));
        startActivity(intent);

    }

    public void customersVwAcn(View v){
        // fetch from internet
        String response = getInternetDataAsString("http://thomas-bayer.com/sqlrest");
        String customerurl = response.substring(response.indexOf("CUSTOMERList")+12+13, response.indexOf("CUSTOMERList")+12+13+31+10);
        // process XML
        String arrayofresponses = getInternetDataAsString(customerurl);
        ArrayList<String> rows = getCount(arrayofresponses, "CUSTOMER");

        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("arrayList", rows.toArray(new String[rows.size()]));
        startActivity(intent);

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

    public ArrayList getCount(String xml, String root){
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
    }

}
