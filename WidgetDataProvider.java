package com.example.spoonpoint.spoonpoint.widget;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.spoonpoint.spoonpoint.R;
import com.example.spoonpoint.spoonpoint.Utils.RestaurantUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * WidgetDataProvider acts as the adapter for the collection view widget,
 * providing RemoteViews to the widget in the getViewAt method.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "WidgetDataProvider";

    private static List<RestaurantUtils> mCollection = new ArrayList<>();
    private List<RestaurantUtils> mWidgetCollect = new ArrayList<>();
    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {

        if (mWidgetCollect.size() > 0) {
            return mWidgetCollect.size();
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.list_remote_view);
        Bitmap featured_poster;

/*Check for empty featured image url (the server returns an empty string for restaurants without featured image)
 * substitute empty featured image by a local placeholder image.
*/

        try {
            if (!((mWidgetCollect.get(position).featured_image).isEmpty())) {
                featured_poster = Picasso.with(mContext).load(mWidgetCollect.get(position).featured_image).resize(50, 50).get();
            } else {
				/* keep in mind not to load vector images to the remote view of the collection widget (picasso does not support loading vector images on to the widget - might result in a crash) */
                featured_poster = Picasso.with(mContext).load(R.drawable.restaurant).resize(50, 50).get();
            }


            view.setImageViewBitmap(R.id.widget_image, featured_poster);

        } catch (IOException j) {
            j.printStackTrace();
        }

        view.setTextViewText(R.id.widget_text, mWidgetCollect.get(position).name);

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public static void recommendationData(ArrayList<RestaurantUtils> data) {
        mCollection = data;
    }

    private void initData() {
        mWidgetCollect = mCollection;
    }


}