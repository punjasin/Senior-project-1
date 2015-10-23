package iwish.iwish;

/**
 * Created by Visava on 7/22/2015.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class wishlist_fragment extends Fragment {
    public static ArrayList<product> list_wish = new ArrayList<product>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.wishlist,container,false);

        //instantiate custom adapter
       WishCustomList adapter1 = new WishCustomList(list_wish, getActivity().getApplicationContext());

        //handle listview and assign adapter
        ListView lView = (ListView) v.findViewById(R.id.wishTextResult);
        lView.setAdapter(adapter1);
        return v;
    }
}