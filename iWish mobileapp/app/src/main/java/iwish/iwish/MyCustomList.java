package iwish.iwish;

import android.content.Context;
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
        listItemText.setText(list.get(position).getDescription());
        listItemText.setHorizontallyScrolling(true);
        listItemText.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        listItemText.setMovementMethod(new ScrollingMovementMethod());

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.delete_btn);
        final EditText addBtn = (EditText) view.findViewById(R.id.amount);
        addBtn.setText(Integer.toString(list.get(position).getAmountOf()));

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task

                notifyDataSetChanged();
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
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        // next stuff
                        break;
                }
                return false;
            }

        });
        return view;
    }
}