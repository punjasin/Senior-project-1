package iwish.iwish;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    Context context = this;

    // Declaring Your View and Variables
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Wishlist","Shopping Cart","Comparison"};
    int Numboftabs =3;
    public static ArrayList<product> add_list = new ArrayList<product>();
    public static ArrayList<String> cate_list = new ArrayList<String>();
    int pos;
    boolean freshWish = true;
    boolean freshCart = false;
    public static boolean check=false;
    public static boolean save=false;
    public static boolean cal=true;
    public static boolean calw=true;



    //nfc
    private NfcAdapter nfcAdpt;
    PendingIntent nfcPendingIntent;
    IntentFilter[] intentFiltersArray;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NFC";
    boolean NFC = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.logo);


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

        NFC();

        try {

            FileInputStream fileInputStream = context.openFileInput("wishlist.dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            wishlist_fragment.list_wish = (ArrayList<product>) objectInputStream.readObject();
            objectInputStream.close();

            FileInputStream fileInputStream1 = context.openFileInput("cart.dat");
            ObjectInputStream objectInputStream1 = new ObjectInputStream(fileInputStream1);
            cart_fragment.list = (ArrayList<product>) objectInputStream1.readObject();
            objectInputStream1.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }



        scheduleUpdate();

    }



//        @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

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
                save=true;

            }
        }

    }

    public void compareUpdate(){
        try {
            ImageView lView = (ImageView) findViewById(R.id.imageView);
            byte[] decodedString = Base64.decode(compare_fragment.list_compare.get(0).getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Bitmap resized = Bitmap.createScaledBitmap(decodedByte, 200, 150, true);
            lView.setImageBitmap(resized);

            ImageView lView1 = (ImageView) findViewById(R.id.imageView2);
            byte[] decodedString1 = Base64.decode(compare_fragment.list_compare.get(1).getImage(), Base64.DEFAULT);
            Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
            Bitmap resized1 = Bitmap.createScaledBitmap(decodedByte1, 200, 150, true);
            lView1.setImageBitmap(resized1);

            TextView lView2 = (TextView) findViewById(R.id.Name1);
            lView2.setText(compare_fragment.list_compare.get(0).getName());

            TextView lView3 = (TextView) findViewById(R.id.Name2);
            lView3.setText(compare_fragment.list_compare.get(1).getName());

            TextView lView4 = (TextView) findViewById(R.id.Description1);
            lView4.setText(compare_fragment.list_compare.get(0).getDescription());
            lView4.setMovementMethod(new ScrollingMovementMethod());


            TextView lView5 = (TextView) findViewById(R.id.Description2);
            lView5.setText(compare_fragment.list_compare.get(1).getDescription());
            lView5.setMovementMethod(new ScrollingMovementMethod());


//            TextView lView6 = (TextView) findViewById(R.id.Categorize1);
//            lView6.setText(compare_fragment.list_compare.get(0).getCategorize());
//
//            TextView lView7 = (TextView) findViewById(R.id.Categorize2);
//            lView7.setText(compare_fragment.list_compare.get(1).getCategorize());

            TextView lView8 = (TextView) findViewById(R.id.Weigh1);
            lView8.setText(compare_fragment.list_compare.get(0).getNetweight().toString());

            TextView lView9 = (TextView) findViewById(R.id.Weigh2);
            lView9.setText(compare_fragment.list_compare.get(1).getNetweight().toString());

            TextView lView10 = (TextView) findViewById(R.id.Price1);
            lView10.setText(compare_fragment.list_compare.get(0).getPrice().toString());

            TextView lView11 = (TextView) findViewById(R.id.Price2);
            lView11.setText(compare_fragment.list_compare.get(1).getPrice().toString());

            TextView lView12 = (TextView) findViewById(R.id.Promotion1);
            lView12.setText(compare_fragment.list_compare.get(0).getAmount().toString() + "\n" + compare_fragment.list_compare.get(0).getPromoprice().toString());

            TextView lView13 = (TextView) findViewById(R.id.Promotion2);
            lView13.setText(compare_fragment.list_compare.get(1).getAmount().toString() + "\n" + compare_fragment.list_compare.get(1).getPromoprice().toString());
        } catch (NullPointerException ex){
            ex.printStackTrace();
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public void getData(String id){

        try {

            String url = "http://192.168.43.96:8080/rest/product/" + id;

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            //restTemplate.setRequestFactory(new CommonsClientHttpRequestFactory());


            // Add the String message converter
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            // Make the HTTP GET request, marshaling the response to a String
            String result = restTemplate.getForObject(url, String.class);

            boolean nw = true;

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonImages = jsonObject.getJSONArray("images");
            JSONObject jsonImage = jsonImages.getJSONObject(0);
            String image = jsonImage.getString("content");

            if( jsonObject.get("amount").toString().equals("null") || jsonObject.get("promoprice").toString().equals("null") ) {
                product p = new product(jsonObject.get("code").toString(), jsonObject.get("name").toString(), jsonObject.get("description").toString(), jsonObject.get("categorize").toString(), Double.valueOf(jsonObject.get("netweight").toString()), Double.valueOf(jsonObject.get("price").toString()), 1, image);

                for (product item : cart_fragment.list) {
                    if (p.getCode().equals(item.getCode())) {

                        nw = false;
                        item.setAmountOf(item.getAmountOf()+1);
                        MainActivity.check=true;
                        MainActivity.cal=true;
                        MainActivity.save=true;

                        break;
                    }
                }
                if(nw) {
                    cart_fragment.list.add(p);
                    MainActivity.check=true;
                    MainActivity.cal=true;
                    MainActivity.save=true;
                }

            }

            else {
                product p = new product(jsonObject.get("code").toString(), jsonObject.get("name").toString(), jsonObject.get("description").toString(), jsonObject.get("categorize").toString(), Double.valueOf(jsonObject.get("netweight").toString()), Double.valueOf(jsonObject.get("price").toString()), Integer.valueOf(jsonObject.get("amount").toString()), Double.valueOf(jsonObject.get("promoprice").toString()), 1,image);
                for (product item : cart_fragment.list) {
                    if (p.getCode().equals(item.getCode())) {
                        nw = false;
                        item.setAmountOf(item.getAmountOf()+1);
                        MainActivity.check=true;
                        MainActivity.cal=true;
                        MainActivity.save=true;
                        break;
                    }
                }
                if(nw) {
                    cart_fragment.list.add(p);
                    MainActivity.check=true;
                    MainActivity.cal=true;
                    MainActivity.save=true;

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (HttpClientErrorException ex){
            ex.printStackTrace();
        } catch (ResourceAccessException ex){
            ex.printStackTrace();
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }


        freshCart=true;
        NFC = false;

    }

    public void getDatas(){
        add_list.clear();

        try {
            String url = "http://192.168.43.96:8080/rest/product/all";

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            //restTemplate.setRequestFactory(new CommonsClientHttpRequestFactory());


            // Add the String message converter
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            // Make the HTTP GET request, marshaling the response to a String
            String result = restTemplate.getForObject(url, String.class);

            JSONArray jArray = new JSONArray(result);

            for(int i=0; i<jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                JSONArray jsonImages = jsonObject.getJSONArray("images");
                JSONObject jsonImage = jsonImages.getJSONObject(0);
                String image = jsonImage.getString("content");
                if (jsonObject.get("amount").toString().equals("null") || jsonObject.get("promoprice").toString().equals("null")) {
                    product p = new product(jsonObject.get("code").toString(), jsonObject.get("name").toString(), jsonObject.get("description").toString(), jsonObject.get("categorize").toString(), Double.valueOf(jsonObject.get("netweight").toString()), Double.valueOf(jsonObject.get("price").toString()), 1, image);
                    add_list.add(p);

                } else {
                    product p = new product(jsonObject.get("code").toString(), jsonObject.get("name").toString(), jsonObject.get("description").toString(), jsonObject.get("categorize").toString(), Double.valueOf(jsonObject.get("netweight").toString()), Double.valueOf(jsonObject.get("price").toString()), Integer.valueOf(jsonObject.get("amount").toString()), Double.valueOf(jsonObject.get("promoprice").toString()), 1, image);
                    add_list.add(p);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ResourceAccessException ex){
            ex.printStackTrace();
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

    }

    public void getCategorizes(){
        cate_list.clear();
        try {
            cate_list.add("All");
            String url = "http://192.168.43.96:8080/rest/product/categorizes";

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            //restTemplate.setRequestFactory(new CommonsClientHttpRequestFactory());


            // Add the String message converter
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            // Make the HTTP GET request, marshaling the response to a String
            String result = restTemplate.getForObject(url, String.class);

            JSONArray jArray = new JSONArray(result);

            for(int i=0; i<jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                    String p = jsonObject.get("categorize").toString();
                    cate_list.add(p);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ResourceAccessException ex){
            ex.printStackTrace();
        }
    }


    public void AddToWish1(View view){
        boolean nw = true;
        try {
            product p = compare_fragment.list_compare.get(0);

            for (product item : wishlist_fragment.list_wish) {
                if (p.getCode().equals(item.getCode())) {

                    nw = false;
                    item.setAmountOf(item.getAmountOf() + 1);
                    MainActivity.check = true;
                    MainActivity.calw = true;
                    MainActivity.save = true;
                    break;
                }
            }
            if (nw) {
                wishlist_fragment.list_wish.add(p); //or some other task
                MainActivity.check = true;
                MainActivity.calw = true;
                MainActivity.save = true;
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        freshWish=true;
    }

    public void AddToWish2(View view){
        boolean nw = true;
        try {
            product p = compare_fragment.list_compare.get(1);

            for (product item : wishlist_fragment.list_wish) {
                if (p.getCode().equals(item.getCode())) {

                    nw = false;
                    item.setAmountOf(item.getAmountOf() + 1);
                    MainActivity.check = true;
                    MainActivity.calw = true;
                    MainActivity.save = true;
                    break;
                }
            }
            if (nw) {
                wishlist_fragment.list_wish.add(p); //or some other task
                MainActivity.check = true;
                MainActivity.calw = true;
                MainActivity.save = true;
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        freshWish=true;
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

    public void AddToWish(final View view) {

        final Context context = this;

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.add_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        getCategorizes();
        final Spinner spinner = (Spinner) promptsView.findViewById(R.id.spinnerSearch);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, cate_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(pos);

        if(pos==0){
            getDatas();
        }
        else{
            selectedCategorizes();
        }

        AddCustomList adapter1 = new AddCustomList(add_list, context);

        ListView lView = (ListView) promptsView.findViewById(R.id.addTextResult);
        lView.setAdapter(adapter1);
        //handle listview and assign adapter

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // The connection URL
                                //add_list.clear();
                                WishCustomList adapter2 = new WishCustomList(wishlist_fragment.list_wish, context);

                                ListView lView = (ListView) findViewById(R.id.wishTextResult);
                                lView.setAdapter(adapter2);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Search",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                pos = spinner.getSelectedItemPosition();

                AddToWish(view);
                dialog.cancel();

            }


        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void Compare(final View view) {

        final Context context = this;

        compare_fragment.list_compare.clear();

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.add_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        getCategorizes();
        cate_list.set(0,"Choose");
        final Spinner spinner = (Spinner) promptsView.findViewById(R.id.spinnerSearch);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, cate_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(pos);

        selectedCategorizes();

        CompareCustomList adapter1 = new CompareCustomList(add_list, context);

        ListView lView = (ListView) promptsView.findViewById(R.id.addTextResult);
        lView.setAdapter(adapter1);
        //handle listview and assign adapter

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // The connection URL
                                //add_list.clear();


                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Search",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        pos = spinner.getSelectedItemPosition();

                        Compare(view);
                        dialog.cancel();

                    }


                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void selectedCategorizes(){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.add_prompt, null);
        final Spinner spinner = (Spinner) promptsView.findViewById(R.id.spinnerSearch);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, cate_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(pos);
        add_list.clear();
        try {
            String url = "http://192.168.43.96:8080/rest/product/categorizes/"+String.valueOf(spinner.getSelectedItem());

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
            //restTemplate.setRequestFactory(new CommonsClientHttpRequestFactory());


            // Add the String message converter
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            // Make the HTTP GET request, marshaling the response to a String
            String result = restTemplate.getForObject(url, String.class);

            JSONArray jArray = new JSONArray(result);

            for(int i=0; i<jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);
                JSONArray jsonImages = jsonObject.getJSONArray("images");
                JSONObject jsonImage = jsonImages.getJSONObject(0);
                String image = jsonImage.getString("content");
                if (jsonObject.get("amount").toString().equals("null") || jsonObject.get("promoprice").toString().equals("null")) {
                    product p = new product(jsonObject.get("code").toString(), jsonObject.get("name").toString(), jsonObject.get("description").toString(), jsonObject.get("categorize").toString(), Double.valueOf(jsonObject.get("netweight").toString()), Double.valueOf(jsonObject.get("price").toString()), 1,image);
                    add_list.add(p);


                } else {
                    product p = new product(jsonObject.get("code").toString(), jsonObject.get("name").toString(), jsonObject.get("description").toString(), jsonObject.get("categorize").toString(), Double.valueOf(jsonObject.get("netweight").toString()), Double.valueOf(jsonObject.get("price").toString()), Integer.valueOf(jsonObject.get("amount").toString()), Double.valueOf(jsonObject.get("promoprice").toString()), 1,image);
                    add_list.add(p);

                }
            }

            //handle listview and assign adapter
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ResourceAccessException ex){
            ex.printStackTrace();
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
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


        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void NFC(){
        NFC = true;
        nfcAdpt = NfcAdapter.getDefaultAdapter(this);

        // Check if the smartphone has NFC
        if (nfcAdpt == null) {
            Toast.makeText(this, "NFC not supported", Toast.LENGTH_LONG).show();
            finish();
        }

        // Check if NFC is enabled
        if (!nfcAdpt.isEnabled()) {
            //Toast.makeText(this, "Enable NFC before using the app", Toast.LENGTH_LONG).show();
            // get prompts.xml view
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.detail_prompt, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            //handle listview and assign adapter
            TextView lView = (TextView) promptsView.findViewById(R.id.textViewdetail);
            TextView lView1 = (TextView) promptsView.findViewById(R.id.textViewdetail1);

            lView1.setText(" Tips");

            lView.setText("   Enable the NFC to using the scanning NFC feature   ");

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // get user input and set it to result
                                    // The connection URL
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }

        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        nfcPendingIntent =
                PendingIntent.getActivity(this, 0, nfcIntent, 0);

        // Create an Intent Filter limited to the URI or MIME type to
        // intercept TAG scans from.
        IntentFilter tagIntentFilter =
                new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            //tagIntentFilter.addDataScheme("http");
            // tagIntentFilter.addDataScheme("vnd.android.nfc");
            //tagIntentFilter.addDataScheme("tel");
            tagIntentFilter.addDataType("text/plain");
            intentFiltersArray = new IntentFilter[]{tagIntentFilter};
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d(TAG, "New intent");
        getTag(intent);
    }

    private void handleIntent(Intent i) {

        Log.d(TAG, "Intent [" + i + "]");

        getTag(i);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(NFC) {

            nfcAdpt.enableForegroundDispatch(
                    this,
                    // Intent that will be used to package the Tag Intent.
                    nfcPendingIntent,
                    // Array of Intent Filters used to declare the Intents you
                    // wish to intercept.
                    intentFiltersArray,
                    // Array of Tag technologies you wish to handle.
                    null);
            handleIntent(getIntent());
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(NFC) {

        nfcAdpt.disableForegroundDispatch(this);
        }
    }




    private void getTag(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }


    public void scheduleUpdate() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try {
                    if (pager.getCurrentItem() == 1) {
                        freshWish =true;

                        if(cal) {
                            Cal();
                            cal = false;
                        }
                        if(freshCart) {
                            MyCustomList adapter1 = new MyCustomList(cart_fragment.list, context);
                            ListView lView = (ListView) findViewById(R.id.editTextResult);
                            lView.setAdapter(adapter1);
                            freshCart=false;
                        }

                    }
                    if (pager.getCurrentItem() == 0) {
                        freshCart=true;

                        if(calw) {
                            Calw();
                            calw = false;
                        }
                        if (check) {
                            check();
                            check = false;
                        }
                        if(freshWish) {
                            WishCustomList adapter2 = new WishCustomList(wishlist_fragment.list_wish, context);
                            ListView lView1 = (ListView) findViewById(R.id.wishTextResult);
                            lView1.setAdapter(adapter2);
                            freshWish=false;

                        }

                    }
                    if (pager.getCurrentItem() == 2) {
                        compareUpdate();
                        freshCart=true;
                        freshWish =true;
                        calw = true;


                    }
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                if(save) {
                    try {
                        FileOutputStream fileOutputStream = context.openFileOutput("wishlist.dat", Context.MODE_PRIVATE);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(wishlist_fragment.list_wish);
                        objectOutputStream.close();

                        FileOutputStream fileOutputStream1 = context.openFileOutput("cart.dat", Context.MODE_PRIVATE);
                        ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(fileOutputStream1);
                        objectOutputStream1.writeObject(cart_fragment.list);
                        objectOutputStream1.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    save = false;
                }


                handler.postDelayed(this, 500);
            }
        }, 500);
    }

    class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                getData(result);
            }
        }
    }




}

