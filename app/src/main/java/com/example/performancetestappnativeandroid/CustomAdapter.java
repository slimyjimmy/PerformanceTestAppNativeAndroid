package com.example.performancetestappnativeandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
//    String[] data;
    Bitmap[] pictures;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context context, Bitmap[] pictures) {
        this.context = context;
        this.pictures = pictures;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pictures.length;
    }

    @Override
    public Object getItem(int position) {
        return pictures[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);
//        TextView text = (TextView) vi.findViewById(R.id.text);
//        text.setText(data[position]);
        ImageView imageView = vi.findViewById(R.id.row_image);
        imageView.setImageBitmap(pictures[position]);
//        byte[] decodedString = Base64.decode(person_object.getPhoto(),Base64.NO_WRAP);
//        InputStream inputStream  = new ByteArrayInputStream(decodedString);
//        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
//        user_image.setImageBitmap(bitmap);
        return vi;
    }
}
