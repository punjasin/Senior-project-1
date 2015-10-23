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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Visava on 7/23/2015.
 */
public class WishCustomList extends BaseAdapter implements ListAdapter {
    private ArrayList<product> list_wish = new ArrayList<product>();
    private Context context;


    public WishCustomList(ArrayList<product> list, Context context) {
        this.list_wish = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list_wish.size();
    }

    @Override
    public Object getItem(int pos) {
        return list_wish.get(pos);
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
            view = inflater.inflate(R.layout.wish_list, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_wish);
        listItemText.setText(list_wish.get(position).getName());
        listItemText.setHorizontallyScrolling(true);
        listItemText.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        listItemText.setMovementMethod(new ScrollingMovementMethod());

        //Handle checkbox
        CheckBox cb = (CheckBox) view.findViewById(R.id.checkWish);
        cb.setChecked(list_wish.get(position).isStatus());



        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.deleteWish_btn);
        final EditText addBtn = (EditText) view.findViewById(R.id.amount_wish);
        addBtn.setText(Integer.toString(list_wish.get(position).getAmountOf()));

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
                lView.setText("Code: "+ list_wish.get(position).getCode()+"\n"
                        +"Name: "+ list_wish.get(position).getName()+"\n"
                        +"Description: "+ list_wish.get(position).getDescription()+"\n"
                        +"Categorize: "+ list_wish.get(position).getCategorize()+"\n"
                        +"Net Weight: "+ list_wish.get(position).getNetweight()+"\n"
                        +"Price: "+ list_wish.get(position).getPrice()+"\n"+
                        "Promotion:"+"\n"
                        +"Amount: "+ list_wish.get(position).getAmount()+"\n"
                        +"Promotion price: "+ list_wish.get(position).getPromoprice()+"\n");

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
                                list_wish.remove(position); //or some other task

                                notifyDataSetChanged();                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });

        addBtn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        list_wish.get(position).setAmountOf(Integer.parseInt(addBtn.getText().toString()));
                        notifyDataSetChanged();
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        list_wish.get(position).setAmountOf(Integer.parseInt(addBtn.getText().toString()));
                        notifyDataSetChanged();
                        break;
                }
                return false;
            }

        });
        return view;
    }
}