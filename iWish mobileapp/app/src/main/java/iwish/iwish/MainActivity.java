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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class MainActivity extends ActionBarActivity {
    public static Context context1;

    // Declaring Your View and Variables
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Wishlist","Shopping Cart","Comparison"};
    int Numboftabs =3;


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
        final TextView textTotal = (TextView)findViewById(R.id.textTotal);

        double total = 0.0;
        for (product item : cart_fragment.list) {
            double t = item.getPrice() * item.getAmountOf();
            total += t;
        }
        textTotal.setText(Double.toString(total));

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


        try {
            JSONObject jsonObject = new JSONObject(result);
            //,Integer.valueOf(jsonObject.get("amount").toString()),Double.valueOf(jsonObject.get("promoprice").toString())

            product p = new product(jsonObject.get("code").toString(), jsonObject.get("name").toString(), jsonObject.get("description").toString(), jsonObject.get("categorize").toString(), Double.valueOf(jsonObject.get("netweight").toString()), Double.valueOf(jsonObject.get("price").toString()), 1);
            cart_fragment.list.add(p);

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
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

}
