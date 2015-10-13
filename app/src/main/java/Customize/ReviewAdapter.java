package Customize;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dong_gyo.project.R;

import java.util.ArrayList;

import Items.Review;

/**
 * Created by Dong_Gyo on 2015. 10. 2..
 */
public class ReviewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    int layout;
    ArrayList <Review> reviews;

    public ReviewAdapter (Context context, int layout, ArrayList <Review> reviews) {
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.reviews = reviews;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);

            TextView userid = (TextView)convertView.findViewById(R.id.reviewid);
            TextView reviewtext = (TextView)convertView.findViewById(R.id.reviewcontent);
            ImageView imgview = (ImageView)convertView.findViewById(R.id.reviewimg);

            userid.setText(reviews.get(position).getName());
            reviewtext.setText(reviews.get(position).getContent());

            byte[] decodedBytes = Base64.decode(reviews.get(position).getImage(), Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            int height = bmp.getHeight();
            int width = bmp.getWidth();

            Bitmap resized = null;
            resized = Bitmap.createScaledBitmap(bmp, width/2, height/2, true);


            imgview.setImageBitmap(resized);

        }
        return convertView;
    }
}
