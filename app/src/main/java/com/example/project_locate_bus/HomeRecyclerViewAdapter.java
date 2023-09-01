package com.example.project_locate_bus;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {

    // Array of images
    // Adding images from drawable folder
    private final Context ctx;
    List<Address> addresses;
    String lG;
    String lT;
    String cityName;

    // Constructor of our ViewPager2Adapter class
    public HomeRecyclerViewAdapter(Context ctx, List<Address> addressesList) {
        this.ctx = ctx;
        this.addresses = addressesList;
    }

    public HomeRecyclerViewAdapter(Context ctx, List<Address> addresses, String lG, String lT, String cityName) {
        this.ctx = ctx;
        this.addresses = addresses;
        this.lG = lG;
        this.lT = lT;
        this.cityName = cityName;
    }

    public HomeRecyclerViewAdapter(Context ctx, String lG, String lT, String cityName) {
        this.ctx = ctx;
        this.lG = lG;
        this.lT = lT;
        this.cityName = cityName;
    }

    // This method returns our layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.home_student_list_item, parent, false);
        return new ViewHolder(view);
    }

    // This method binds the screen with the view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // This will set the images in imageview
        try {
            holder.images.setText("" + addresses.get(position).getAddressLine(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This Method returns the size of the Array
    @Override
    public int getItemCount() {
        return addresses.size();
    }

    // The ViewHolder class holds the view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView images;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.tvDetails);
        }
    }
}
