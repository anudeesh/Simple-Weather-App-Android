package com.example.anudeesh.hw6;

/**
 * Created by Anudeesh on 10/18/2016.
 */
public class Cities {
    String city,country,temperature,favorite;

    public Cities(String city, String country, String temperature, String favorite) {
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.favorite = favorite;
    }

    public Cities() {

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Cities{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", temperature='" + temperature + '\'' +
                ", favorite='" + favorite + '\'' +
                '}';
    }
}
