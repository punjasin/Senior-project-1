package iwish.iwish;

/**
 * Created by Visava on 7/22/2015.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class compare_fragment extends Fragment {
    public static ArrayList<product> list_compare = new ArrayList<product>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.compare,container,false);

        try {
            ImageView lView = (ImageView) v.findViewById(R.id.imageView);
            byte[] decodedString = Base64.decode(list_compare.get(0).getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Bitmap resized = Bitmap.createScaledBitmap(decodedByte, 200, 150, true);
            lView.setImageBitmap(resized);

            ImageView lView1 = (ImageView) v.findViewById(R.id.imageView2);
            byte[] decodedString1 = Base64.decode(list_compare.get(1).getImage(), Base64.DEFAULT);
            Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
            Bitmap resized1 = Bitmap.createScaledBitmap(decodedByte1, 200, 150, true);
            lView1.setImageBitmap(resized1);

            TextView lView2 = (TextView) v.findViewById(R.id.Name1);
            lView2.setText(list_compare.get(0).getName());

            TextView lView3 = (TextView) v.findViewById(R.id.Name2);
            lView3.setText(list_compare.get(1).getName());

            TextView lView4 = (TextView) v.findViewById(R.id.Description1);
            lView4.setText(list_compare.get(0).getDescription());
            lView4.setMovementMethod(new ScrollingMovementMethod());

            TextView lView5 = (TextView) v.findViewById(R.id.Description2);
            lView5.setText(list_compare.get(1).getDescription());
            lView5.setMovementMethod(new ScrollingMovementMethod());

//            TextView lView6 = (TextView) v.findViewById(R.id.Categorize1);
//            lView6.setText(list_compare.get(0).getCategorize());
//
//            TextView lView7 = (TextView) v.findViewById(R.id.Categorize2);
//            lView7.setText(list_compare.get(1).getCategorize());

            TextView lView8 = (TextView) v.findViewById(R.id.Weigh1);
            lView8.setText(list_compare.get(0).getNetweight().toString());

            TextView lView9 = (TextView) v.findViewById(R.id.Weigh2);
            lView9.setText(list_compare.get(1).getNetweight().toString());

            TextView lView10 = (TextView) v.findViewById(R.id.Price1);
            lView10.setText(list_compare.get(0).getPrice().toString());

            TextView lView11 = (TextView) v.findViewById(R.id.Price2);
            lView11.setText(list_compare.get(1).getPrice().toString());

            TextView lView12 = (TextView) v.findViewById(R.id.Promotion1);
            lView12.setText(list_compare.get(0).getAmount().toString() + "\n" + list_compare.get(0).getPromoprice().toString());

            TextView lView13 = (TextView) v.findViewById(R.id.Promotion2);
            lView13.setText(list_compare.get(1).getAmount().toString() + "\n" + list_compare.get(1).getPromoprice().toString());
        } catch (NullPointerException ex){
            ex.printStackTrace();
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }

        return v;
    }
}
