package iwish.iwish;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    public static Context context1;

    // Declaring Your View and Variables
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Wishlist","Shopping Cart","Comparison"};
    int Numboftabs =3;
    public static ArrayList<product> add_list = new ArrayList<product>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        StrictMode.enableDefaults(); //STRICT MODE ENABLED

        scheduleUpdate();

        //resultView = (TextView) findViewById(R.id.result);
    }



        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final Context context = this;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            // set title
            alertDialogBuilder.setTitle("iWish");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Are you sure?")
                    .setCancelable(false)
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            MainActivity.this.finish();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void Cal(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (!imm.isAcceptingText()) {
            final TextView textTotal = (TextView) findViewById(R.id.textTotal);

            double total = 0.0;
            for (product item : cart_fragment.list) {

                if (item.getAmount() != null) {
                    int n = item.getAmountOf() / item.getAmount();
                    int n1 = item.getAmountOf() % item.getAmount();
                    double t = item.getPromoprice() * n;
                    double t1 = item.getPrice() * n1;
                    double ft = t + t1;
                    total += ft;
                } else {
                    double t = item.getPrice() * item.getAmountOf();
                    total += t;
                }
            }
            textTotal.setText(Double.toString(total));
        }

    }

    public void Calw(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (!imm.isAcceptingText()) {
            final TextView textTotal = (TextView) findViewById(R.id.textTotalw);

            double total = 0.0;
            for (product item : wishlist_fragment.list_wish) {

                if (item.getAmount() != null) {
                    int n = item.getAmountOf() / item.getAmount();
                    int n1 = item.getAmountOf() % item.getAmount();
                    double t = item.getPromoprice() * n;
                    double t1 = item.getPrice() * n1;
                    double ft = t + t1;
                    total += ft;
                } else {
                    double t = item.getPrice() * item.getAmountOf();
                    total += t;
                }
            }
            textTotal.setText(Double.toString(total));
        }

    }


    public void check(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (!imm.isAcceptingText()) {
            //do when keyboard not on
            loop:
            for (product item : wishlist_fragment.list_wish) {
                String c = item.getCode();
                for (product item1 : cart_fragment.list) {
                    String c1 = item1.getCode();

                    if (c.equals(c1)) {
                        if(item1.getAmountOf() >= item.getAmountOf()) {
                            item.setStatus(true);
                            WishCustomList adapter1 = new WishCustomList(wishlist_fragment.list_wish, this);

                            ListView lView = (ListView) findViewById(R.id.wishTextResult);
                            lView.setAdapter(adapter1);
                            continue loop;
                        }
                    }

                }
                item.setStatus(false);
                WishCustomList adapter1 = new WishCustomList(wishlist_fragment.list_wish, this);

                ListView lView = (ListView) findViewById(R.id.wishTextResult);
                lView.setAdapter(adapter1);


            }
        }

    }

    public void getData(String id){
        String url = "http://192.168.43.96:8080/rest/product/" + id;

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        //restTemplate.setRequestFactory(new CommonsClientHttpRequestFactory());


        // Add the String message converter
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // Make the HTTP GET request, marshaling the response to a String
        String result = restTemplate.getForObject(url, String.class);

        boolean nw = true;

        try {
            JSONObject jsonObject = new JSONObject(result);

            if( jsonObject.get("amount").toString().equals("null") || jsonObject.get("promoprice").toString().equals("null") ) {
                product p = new product(jsonObject.get("code").toString(), jsonObject.get("name").toString(), jsonObject.get("description").toString(), jsonObject.get("categorize").toString(), Double.valueOf(jsonObject.get("netweight").toString()), Double.valueOf(jsonObject.get("price").toString()), 1);

                for (product item : cart_fragment.list) {
                    if (p.equals(item)) {

                        int c = item.getAmountOf();
                        int n = c+1;
                        item.setAmountOf(n);
                        nw = false;
                        break;
                    }
                }
                if(nw) {
                    cart_fragment.list.add(p);
                }

            }

            else {
                product p = new product(jsonObject.get("code").toString(), jsonObject.get("name").toString(), jsonObject.get("description").toString(), jsonObject.get("categorize").toString(), Double.valueOf(jsonObject.get("netweight").toString()), Double.valueOf(jsonObject.get("price").toString()), Integer.valueOf(jsonObject.get("amount").toString()), Double.valueOf(jsonObject.get("promoprice").toString()), 1);
                for (product item : cart_fragment.list) {
                    if (p.equals(item)) {

                        int c = item.getAmountOf();
                        int n = c+1;
                        item.setAmountOf(n);
                        nw = false;
                        break;
                    }
                }
                if(nw) {
                    cart_fragment.list.add(p);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getDatas(){
        String url = "http://192.168.43.96:8080/rest/product/all";

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        //restTemplate.setRequestFactory(new CommonsClientHttpRequestFactory());


        // Add the String message converter
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // Make the HTTP GET request, marshaling the response to a String
        String result = restTemplate.getForObject(url, String.class);


        try {
            JSONArray jArray = new JSONArray(result);

            for(int i=0; i<jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                if (jsonObject.get("amount").toString().equals("null") || jsonObject.get("promoprice").toString().equals("null")) {
                    product p = new product(jsonObject.get("code").toString(), jsonObject.get("name").toString(), jsonObject.get("description").toString(), jsonObject.get("categorize").toString(), Double.valueOf(jsonObject.get("netweight").toString()), Double.valueOf(jsonObject.get("price").toString()), 1);
                    add_list.add(p);

                } else {
                    product p = new product(jsonObject.get("code").toString(), jsonObject.get("name").toString(), jsonObject.get("description").toString(), jsonObject.get("categorize").toString(), Double.valueOf(jsonObject.get("netweight").toString()), Double.valueOf(jsonObject.get("price").toString()), Integer.valueOf(jsonObject.get("amount").toString()), Double.valueOf(jsonObject.get("promoprice").toString()), 1);
                    add_list.add(p);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void AddToCart(View view) {

        final Context context = this;


        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // The connection URL
                                getData(userInput.getText().toString());

                                MyCustomList adapter1 = new MyCustomList(cart_fragment.list, context);

                                ListView lView = (ListView) findViewById(R.id.editTextResult);
                                lView.setAdapter(adapter1);

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void AddToWish(View view) {

        final Context context = this;


        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.add_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        getDatas();

        AddCustomList adapter1 = new AddCustomList(add_list, this);

        //handle listview and assign adapter
        ListView lView = (ListView) promptsView.findViewById(R.id.addTextResult);
        lView.setAdapter(adapter1);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // The connection URL
                                add_list.clear();
                                WishCustomList adapter2 = new WishCustomList(wishlist_fragment.list_wish, context);

                                ListView lView = (ListView) findViewById(R.id.wishTextResult);
                                lView.setAdapter(adapter2);
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    public void Bar(View view) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final Context context = this;

        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //getData();
            //we have a result
            String scanContent = scanningResult.getContents();

            getData(scanContent);

            MyCustomList  adapter1= new MyCustomList(cart_fragment.list,context);

            ListView lView = (ListView) findViewById(R.id.editTextResult);
            lView.setAdapter(adapter1);


        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void scheduleUpdate() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Cal();
                Calw();
                check();
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }


}
