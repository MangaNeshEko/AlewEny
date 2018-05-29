package com.aleweny.prouction.View;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aleweny.prouction.R;

/**
 * Created by littELGOsht on 4/9/2018.
 */

public class ListViewAdapter extends ArrayAdapter {

    private String[] titleToBe;
    private String[] descriptionToBe;
    private int[] imagesToBe;
    private int[] backgroundToBe;

    public static SharedPreferences sharedPreferences;
    boolean backBoolean;



    public ListViewAdapter(Context context, String[] title, String[] description, int[] images ){
        super(context, R.layout.listitem,R.id.title, title);

        this.titleToBe = title;
        this.descriptionToBe = description;
        this.imagesToBe = images;
//        this.backgroundToBe = background;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listitem, parent,false);

        TextView title = row.findViewById(R.id.title);
        TextView description = row.findViewById(R.id.idDesc);
        ImageView image = row.findViewById(R.id.idPic);


        sharedPreferences = getContext().getSharedPreferences("Settings",0);
        backBoolean = sharedPreferences.getBoolean("switchOne",true);

        if(backBoolean){
            title.setTextColor(getContext().getResources().getColor(R.color.whileBack));
            description.setTextColor(getContext().getResources().getColor(R.color.whileBack));

            //By Iterating using the value of position lets make a layout
            title.setText(titleToBe[position]);
            description.setText(descriptionToBe[position]);
            image.setImageResource(imagesToBe[position]);
//        image.setBackgroundResource(backgroundToBe[position]);
        }else{
            title.setText(titleToBe[position]);
            description.setText(descriptionToBe[position]);
            image.setImageResource(imagesToBe[position]);
        }

//        //By Iterating using the value of position lets make a layout
//        title.setText(titleToBe[position]);
//        description.setText(descriptionToBe[position]);
//        image.setImageResource(imagesToBe[position]);
//        image.setBackgroundResource(backgroundToBe[position]);


        return row;
    }
    public void colorChage(View view){


        TextView textV1 =  view.findViewById(R.id.title);
        TextView textV2 = view.findViewById(R.id.idDesc);
        ImageView imag1 = view.findViewById(R.id.idPic);
        textV1.setTextColor(view.getResources().getColor(R.color.whileBack));
        textV2.setTextColor(view.getResources().getColor(R.color.whileBack));
    }
}
