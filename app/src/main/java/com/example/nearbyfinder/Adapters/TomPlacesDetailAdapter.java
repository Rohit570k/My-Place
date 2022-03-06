package com.example.nearbyfinder.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbyfinder.Model.TomNearbyPlaceModel;
import com.example.nearbyfinder.R;
import com.example.nearbyfinder.databinding.PlaceItemLayoutBinding;

import org.w3c.dom.Text;

import java.util.List;

public class TomPlacesDetailAdapter extends RecyclerView.Adapter<TomPlacesDetailAdapter.ViewHolder> {
  List<TomNearbyPlaceModel> tomNearbyPlaceModels;

    public TomPlacesDetailAdapter(List<TomNearbyPlaceModel> tomNearbyPlaceModels) {
        this.tomNearbyPlaceModels = tomNearbyPlaceModels;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_layout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    if(tomNearbyPlaceModels!=null){
        TomNearbyPlaceModel nearbyPlaceModel =tomNearbyPlaceModels.get(position);
        holder.placeName.setText(nearbyPlaceModel.getPoi().getName());
        holder.catergories1.setText(nearbyPlaceModel.getPoi().getCategories().get(0));
        if(nearbyPlaceModel.getPoi().getCategories().size()>1) {
            holder.categories2.setText(nearbyPlaceModel.getPoi().getCategories().get(1));
        }
        holder.distance.setText((int) (nearbyPlaceModel.getDist()*1)+" meter");
        holder.phonenumber.setText(nearbyPlaceModel.getPoi().getPhone());
        holder.url.setText(nearbyPlaceModel.getPoi().getUrl());
        holder.addressDetails.setText(nearbyPlaceModel.getAddress().getFreeformAddress());
    }
    }
    @Override
    public int getItemCount() {
       if(tomNearbyPlaceModels!=null)
           return  tomNearbyPlaceModels.size();
       else
           return 0;
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder {
        TextView  placeName;
        TextView catergories1;
        TextView categories2;
        TextView distance;
        TextView phonenumber;
        TextView url;
        TextView addressDetails;
        private PlaceItemLayoutBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName =itemView.findViewById(R.id.placename);
            catergories1=itemView.findViewById(R.id.categories1);
            categories2=itemView.findViewById(R.id.categories2);
            distance=itemView.findViewById(R.id.distance);
            phonenumber=itemView.findViewById(R.id.phonenumber);
            url=itemView.findViewById(R.id.url);
            addressDetails=itemView.findViewById(R.id.addressdetails);
        }
    }
}
