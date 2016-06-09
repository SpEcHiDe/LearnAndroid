package me.shrimadhavuk.thomasbayerui;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends ListActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "me.shrimadhavuk";
    TextView outputTxt;
    ProgressDialog barProgressDialog;
    Handler updateBarHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        outputTxt = (TextView) findViewById(R.id.output);

        if(isOnline() == true){

            outputTxt.setMovementMethod(new ScrollingMovementMethod());

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            //setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "developed By https://shrimadhavuk.me/", Snackbar.LENGTH_LONG).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
        else{
            outputTxt.setText("no network connectivity\n Please enable Internet connection and reLoad the application!");
        }

        updateBarHandler = new Handler();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
//            String[] psrf = new String[1];
//            ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, psrf);
//            setListAdapter(ada);
            outputTxt.setText("API OUTPUT");
            // view customers
            // fetch from internet
            String response = getInternetDataAsString("http://thomas-bayer.com/sqlrest");
            if(response != null && !response.equals("")){
                String customerurl = response.substring(response.indexOf("CUSTOMERList")+12+13, response.indexOf("CUSTOMERList")+12+13+31+10);
                // process XML
                String arrayofresponses = getInternetDataAsString(customerurl);
                ArrayList<String> rows = getCount(arrayofresponses, "CUSTOMER");
                String[] frsp = rows.toArray(new String[rows.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, frsp);
                setListAdapter(adapter);
            }
            else{
                outputTxt.setText("invalid internet!");
            }
        } else if (id == R.id.nav_gallery) {
//            String[] psrf = new String[1];
//            ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, psrf);
//            setListAdapter(ada);
            outputTxt.setText("API OUTPUT");
            // view invoice
            // fetch from internet
            String response = getInternetDataAsString("http://thomas-bayer.com/sqlrest");
            if(response != null && !response.equals("")){
                String invoiceurl = response.substring(response.indexOf("INVOICEList")+11+13, response.indexOf("INVOICEList")+11+13+31+9);
                // process XML
                String arrayofresponses = getInternetDataAsString(invoiceurl);
                ArrayList<String> rows = getCount(arrayofresponses, "INVOICE");
                String[] frsp = rows.toArray(new String[rows.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, frsp);
                setListAdapter(adapter);
            }
            else{
                outputTxt.setText("invalid internet!");
            }

        } else if (id == R.id.nav_slideshow) {
//            String[] psrf = new String[1];
//            ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, psrf);
//            setListAdapter(ada);
            outputTxt.setText("API OUTPUT");
            // view sold items
            // fetch from internet
            String response = getInternetDataAsString("http://thomas-bayer.com/sqlrest");
            if(response != null && !response.equals("")){
                String itemsurl = response.substring(response.indexOf("ITEMList")+8+13, response.indexOf("ITEMList")+8+13+31+6);
                // process XML
                String arrayofresponses = getInternetDataAsString(itemsurl);
                ArrayList<String> rows = getCount(arrayofresponses, "ITEM");
                String[] frsp = rows.toArray(new String[rows.size()]);

                Toast.makeText(this, "I do not know how to parse this XML!", Toast.LENGTH_LONG).show();
            }
            else{
                outputTxt.setText("invalid internet!");
            }
        } else if (id == R.id.nav_manage) {
//            String[] psrf = new String[1];
//            ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, psrf);
//            setListAdapter(ada);
            outputTxt.setText("API OUTPUT");
            // view products
            // fetch from internet
            String response = getInternetDataAsString("http://thomas-bayer.com/sqlrest");
            if(response != null && !response.equals("")){
                String productsurl = response.substring(response.indexOf("PRODUCTList")+11+13, response.indexOf("PRODUCTList")+11+13+31+9);
                // process XML
                String arrayofresponses = getInternetDataAsString(productsurl);
                ArrayList<String> rows = getCount(arrayofresponses, "PRODUCT");
                String[] frsp = rows.toArray(new String[rows.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, frsp);
                setListAdapter(adapter);
            }
            else{
                outputTxt.setText("invalid internet!");
            }
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "share button", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "send button", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public String getInternetDataAsString(String base_url) {

        ProgressDialog ringProgressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Fetching from API ...", true);
        ringProgressDialog.setCancelable(true);
        StringBuilder responseOutput = null;
        // Here you should write your time consuming task...

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
            responseOutput = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseOutput.append(line);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        // Let the progress ring for 1 seconds...
        //Thread.sleep(1000);

        ringProgressDialog.dismiss();

        String t = responseOutput.toString();
        return t;
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


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // ListView Clicked item index
        int itemPosition     = position;
        // ListView Clicked item value
        String  itemValue    = (String) l.getItemAtPosition(position);
        String response = getInternetDataAsString(itemValue);
        String fr = "";

        // product
        String pid = getElement(response, "ID");
        String name = getElement(response, "NAME");
        String price = getElement(response, "PRICE");

        if((pid != null) && (name != null) && (price != null)){
            fr = "ID: " + pid + "\nNAME: " + name + "\nPRICE: " + price + "\n";
        }

        // invoice
        String iid = getElement(response, "ID");
        String cid = getElement(response, "CUSTOMERID");
        String total = getElement(response, "TOTAL");

        if((iid != null) && (cid != null) && (total != null)){
            fr = "ID: " + iid;
            String cresp = getInternetDataAsString("http://thomas-bayer.com/sqlrest/CUSTOMER/" + getElement(response, "CUSTOMERID"));
            // customer
            String rcid = getElement(cresp, "ID");
            String fname = getElement(cresp, "FIRSTNAME");
            String lname = getElement(cresp, "LASTNAME");
            String street = getElement(cresp, "STREET");
            String city = getElement(cresp, "CITY");

            if((rcid != null) && (fname != null) && (lname != null) && (street != null) && (city != null)){
                fr += "\nCUSTOMER ID: " + rcid + "\nFIRST NAME: " + fname + "\nLAST NAME: " + lname + "\nSTREET: " + street + "\nCITY: " + city + "\n";
            }
            fr += "TOTAL: " + total + "\n";

        }

        // customer
        String rcid = getElement(response, "ID");
        String fname = getElement(response, "FIRSTNAME");
        String lname = getElement(response, "LASTNAME");
        String street = getElement(response, "STREET");
        String city = getElement(response, "CITY");

        if((rcid != null) && (fname != null) && (lname != null) && (street != null) && (city != null)){
            fr = "\nCUSTOMER ID: " + rcid + "\nFIRST NAME: " + fname + "\nLAST NAME: " + lname + "\nSTREET: " + street + "\nCITY: " + city + "\n";
        }

        //Toast.makeText(this, "I do not know how to parse this XML!", Toast.LENGTH_LONG).show();
        if(fr.equals("")){
            outputTxt.setText("unknown error!");
        }
        else{
            outputTxt.setText(fr);
        }

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        final Menu menu = navigationView.getMenu();
//        menu.addSubMenu("asd");

    }

    public String getElement(String xml, String tag){
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));

            Document doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName(tag);
            String response = null;
            if (nodes.getLength() == 1) {
                // Get element
                Element element = (Element) nodes.item(0);
                Node nNode = nodes.item(0);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    response = eElement.getFirstChild().getNodeValue();
                }
            }
            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /*private class DownloadFilesTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
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
                //Log.i(TAG, String.valueOf(responseOutput));

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

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
            outputTxt.setText(progress[0]);
        }

        protected void onPostExecute(String result) {
            //showDialog("Downloaded " + result + " bytes");
            outputTxt.setText(result);
        }
    }*/

}
