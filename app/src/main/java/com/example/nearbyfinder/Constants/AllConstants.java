package com.example.nearbyfinder.Constants;

import com.example.nearbyfinder.Model.PoiGroupSet;
import com.example.nearbyfinder.R;

import java.util.ArrayList;
import java.util.Arrays;

public interface AllConstants {



    ArrayList<PoiGroupSet> poiGroups=new ArrayList<>(
            Arrays.asList(
                    new PoiGroupSet(1, R.drawable.ic_restaurant, "Restaurant", "7315"),
                    new PoiGroupSet(2, R.drawable.ic_atm, "ATM", "7397"),
                    new PoiGroupSet(3, R.drawable.ic_gas_station, "Gas", "7311"),
                    new PoiGroupSet(4, R.drawable.ic_shopping_cart, "Groceries", "9361023"),
                    new PoiGroupSet(5, R.drawable.ic_hotel, "Hotels", "7314"),
                    new PoiGroupSet(6, R.drawable.ic_pharmacy, "Pharmacies", "7326"),
                    new PoiGroupSet(7, R.drawable.ic_hospital, "Hospitals & Clinics", "7321002"),
                    new PoiGroupSet(8, R.drawable.ic_car_wash, "Car Wash", "9155"),
                    new PoiGroupSet(9, R.drawable.ic_saloon, "Beauty Saloon", "9361067")
            )
    );

}
