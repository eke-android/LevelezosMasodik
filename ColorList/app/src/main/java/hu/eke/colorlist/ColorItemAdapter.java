package hu.eke.colorlist;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by szugyi on 18/11/16.
 */

public class ColorItemAdapter extends ArrayAdapter<ColorItem> {
    private static final int res = R.layout.list_row;

    public ColorItemAdapter(Context context, List<ColorItem> objects) {
        super(context, res, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("LIST", String.format("convertView: %s", convertView));
        TextView view = (TextView) convertView;
        if (view == null) {
            view = (TextView) View.inflate(getContext(), res, null);
            Log.v("LIST", "List item created");
        }

        ColorItem rowData = getItem(position);
        view.setText(rowData.getColor());
        try {
            view.setTextColor(Color.parseColor(rowData.getValue()));
        } catch (Exception ex) {
            try {
                view.setTextColor(Color.parseColor(rowData.getColor()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }
}
