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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Visava on 7/23/2015.
 */
public class CompareCustomList extends BaseAdapter implements ListAdapter {
    private ArrayList<product> list_compare = new ArrayList<product>();
    private Context context;
    int count=0;


    public CompareCustomList(ArrayList<product> list, Context context) {
        this.list_compare = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list_compare.size();
    }

    @Override
    public Object getItem(int pos) {
        return list_compare.get(pos);
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
            view = inflater.inflate(R.layout.compare_list, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_compare);
        listItemText.setText(list_compare.get(position).getName());
        listItemText.setHorizontallyScrolling(true);
        listItemText.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        listItemText.setMovementMethod(new ScrollingMovementMethod());

        ImageView lView3 = (ImageView) view.findViewById(R.id.imageDetailCompare);
        byte[] decodedString = Base64.decode(list_compare.get(position).getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Bitmap resized = Bitmap.createScaledBitmap(decodedByte, 200, 150, true);
        lView3.setImageBitmap(resized);

        //Handle checkbox
        CheckBox cb = (CheckBox) view.findViewById(R.id.checkCompare);



        //Handle buttons and add onClickListeners
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
                        " Name: "+ list_compare.get(position).getName()+"\n"
                        +" Description: "+ list_compare.get(position).getDescription()+"\n"
                        +" Categorize: "+ list_compare.get(position).getCategorize()+"\n"
                        +" Net Weight: "+ list_compare.get(position).getNetweight()+"\n"
                        +" Price: "+ list_compare.get(position).getPrice()+"\n"+
                        " Promotion:"+"\n"
                        +" Amount: "+ list_compare.get(position).getAmount()+"\n"
                        +" Promotion price: "+ list_compare.get(position).getPromoprice()+"\n");

                byte[] decodedString = Base64.decode(list_compare.get(position).getImage(), Base64.DEFAULT);
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

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton checkboxView, boolean isChecked) {

                product p = list_compare.get(position);

                if (isChecked) {
                    count++;
                    compare_fragment.list_compare.add(p); //or some other task

                } else if (!isChecked) {
                    count--;
                    compare_fragment.list_compare.remove(p); //or some other task

                }
                if (count >= 3)// it will allow 2 checkboxes only
                {
                    Toast.makeText(context, "Choose only 2 items", Toast.LENGTH_LONG).show();
                    checkboxView.setChecked(false);
                    count--;
               }
                notifyDataSetChanged();


            }
        });

        return view;
    }
}