package com.example.nearbyfinder.Model.PlaceModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressModel {
    @SerializedName("municipalitySubdivision")
    @Expose
    private String municipalitySubdivision;


    @SerializedName("municipality")
    @Expose
    private String municipality;


    @SerializedName("countrySecondarySubdivision")
    @Expose
    private String countrySecondarySubdivision;


    @SerializedName("countrySubdivision")
    @Expose
    private String countrySubdivision;


    @SerializedName("postalCode")
    @Expose
    private String postalCode;


    @SerializedName("countryCode")
    @Expose
    private String countryCode;


    @SerializedName("country")
    @Expose
    private String country;


    @SerializedName("countryCodeISO3")
    @Expose
    private String countryCodeISO3;


    @SerializedName("freeformAddress")
    @Expose
    private String freeformAddress;


    @SerializedName("localName")
    @Expose
    private String localName;



    public String getMunicipalitySubdivision() {
        return municipalitySubdivision;
    }

    public void setMunicipalitySubdivision(String municipalitySubdivision) {
        this.municipalitySubdivision = municipalitySubdivision;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getCountrySecondarySubdivision() {
        return countrySecondarySubdivision;
    }

    public void setCountrySecondarySubdivision(String countrySecondarySubdivision) {
        this.countrySecondarySubdivision = countrySecondarySubdivision;
    }

    public String getCountrySubdivision() {
        return countrySubdivision;
    }

    public void setCountrySubdivision(String countrySubdivision) {
        this.countrySubdivision = countrySubdivision;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCodeISO3() {
        return countryCodeISO3;
    }

    public void setCountryCodeISO3(String countryCodeISO3) {
        this.countryCodeISO3 = countryCodeISO3;
    }

    public String getFreeformAddress() {
        return freeformAddress;
    }

    public void setFreeformAddress(String freeformAddress) {
        this.freeformAddress = freeformAddress;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

}

