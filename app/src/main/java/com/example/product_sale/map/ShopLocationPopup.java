package com.example.product_sale.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

import com.example.product_sale.models.ShopLocation;

import java.util.List;

public class ShopLocationPopup {
    public interface OnShopLocationSelectedListener {
        void onShopLocationSelected(double latitude, double longitude);
    }

    public static void show(Context context, List<ShopLocation> list, OnShopLocationSelectedListener listener) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        for (ShopLocation location : list) {
            adapter.add(location.getAddress());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Shop Location");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShopLocation selectedLocation = list.get(which);
                listener.onShopLocationSelected(selectedLocation.getLatitude(), selectedLocation.getLongitude());
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
}
