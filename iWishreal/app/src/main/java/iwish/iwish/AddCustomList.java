package iwish.iwish;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Visava on 7/23/2015.
 */
public class AddCustomList extends BaseAdapter implements ListAdapter {
    private ArrayList<product> list_add = new ArrayList<product>();
    private Context context;


    public AddCustomList(ArrayList<product> list, Context context) {
        this.list_add = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list_add.size();
    }

    @Override
    public Object getItem(int pos) {
        return list_add.get(pos);
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
            view = inflater.inflate(R.layout.add_list, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) view.findViewById(R.id.list_item_add);
        listItemText.setText(list_add.get(position).getName());
        listItemText.setHorizontallyScrolling(true);
        listItemText.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        listItemText.setMovementMethod(new ScrollingMovementMethod());

        //Handle buttons and add onClickListeners
        ImageButton addBtn = (ImageButton) view.findViewById(R.id.add_btn);


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
                lView.setText("Code: "+ list_add.get(position).getCode()+"\n"
                                +"Name: "+ list_add.get(position).getName()+"\n"
                        +"Description: "+ list_add.get(position).getDescription()+"\n"
                        +"Categorize: "+ list_add.get(position).getCategorize()+"\n"
                        +"Net Weight: "+ list_add.get(position).getNetweight()+"\n"
                        +"Price: "+ list_add.get(position).getPrice()+"\n"+
                        "Promotion:"+"\n"
                        +"Amount: "+ list_add.get(position).getAmount()+"\n"
                        +"Promotion price: "+ list_add.get(position).getPromoprice()+"\n");

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
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something for (product item : cart_fragment.list) {
                boolean nw = true;
                product p = list_add.get(position);

                for (product item : wishlist_fragment.list_wish) {
                if (p.equals(item)) {

                    int c = item.getAmountOf();
                    int n = c+1;
                    item.setAmountOf(n);
                    nw = false;
                    break;
                }
            }
            if(nw) {
                wishlist_fragment.list_wish.add(p); //or some other task
            }

                notifyDataSetChanged();
            }
        });

        return view;
    }
}