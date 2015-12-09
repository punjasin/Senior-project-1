package iwish.iwish;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Visava on 7/23/2015.
 */
public class MyCustomList extends BaseAdapter implements ListAdapter {
    private ArrayList<product> list = new ArrayList<product>();
    private Context context;


    public MyCustomList(ArrayList<product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).getName());
        listItemText.setHorizontallyScrolling(true);
        listItemText.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        listItemText.setMovementMethod(new ScrollingMovementMethod());

        ImageView lView3 = (ImageView) view.findViewById(R.id.imageDetail);
        byte[] decodedString = Base64.decode(list.get(position).getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Bitmap resized = Bitmap.createScaledBitmap(decodedByte, 200, 150, true);
        lView3.setImageBitmap(resized);

        TextView textPrice = (TextView) view.findViewById(R.id.textPrice);
        double total = 0.0;
        if (list.get(position).getAmount() != null) {
            int n = list.get(position).getAmountOf() / list.get(position).getAmount();
            int n1 = list.get(position).getAmountOf() % list.get(position).getAmount();
            double t = list.get(position).getPromoprice() * n;
            double t1 = list.get(position).getPrice() * n1;
            double ft = t + t1;
            total += ft;
        } else {
            double t = list.get(position).getPrice() * list.get(position).getAmountOf();
            total += t;
        }
        textPrice.setText(Double.toString(total));

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.delete_btn);
        final EditText addBtn = (EditText) view.findViewById(R.id.amount);
        addBtn.setText(Integer.toString(list.get(position).getAmountOf()));

        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.detail_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                //handle listview and assign adapter
                TextView lView = (TextView) promptsView.findViewById(R.id.textViewdetail);
                TextView lView1 = (TextView) promptsView.findViewById(R.id.textViewdetail1);
                ImageView lView2 = (ImageView) promptsView.findViewById(R.id.imageViewProduct);


                lView1.setText(" Details");

                lView.setText(
                        " Name: "+ list.get(position).getName()+"\n"
                        +" Description: "+ list.get(position).getDescription()+"\n"
                        +" Categorize: "+ list.get(position).getCategorize()+"\n"
                        +" Net Weight: "+ list.get(position).getNetweight()+"\n"
                        +" Price: "+ list.get(position).getPrice()+"\n"+
                        " Promotion:"+"\n"
                        +" Amount: "+ list.get(position).getAmount()+"\n"
                        +" Promotion price: "+ list.get(position).getPromoprice()+"\n");

                byte[] decodedString = Base64.decode(list.get(position).getImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                lView2.setImageBitmap(decodedByte);

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
                try {
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                } catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
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
                                list.remove(position); //or some other task
                                MainActivity.check=true;
                                MainActivity.cal=true;
                                MainActivity.save=true;
                                notifyDataSetChanged();
                            }
                        });
                try {
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                } catch (IllegalStateException e){
                    list.remove(position); //or some other task
                    MainActivity.check=true;
                    notifyDataSetChanged();
                }

            }

        });

        addBtn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        list.get(position).setAmountOf(Integer.parseInt(addBtn.getText().toString()));
                        notifyDataSetChanged();
                        MainActivity.check=true;
                        MainActivity.cal=true;
                        MainActivity.save=true;
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        list.get(position).setAmountOf(Integer.parseInt(addBtn.getText().toString()));
                        notifyDataSetChanged();
                        MainActivity.check=true;
                        MainActivity.cal=true;
                        MainActivity.save=true;
                        break;
                }

                return false;
            }

        });
        return view;
    }
}