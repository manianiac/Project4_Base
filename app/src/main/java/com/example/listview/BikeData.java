package com.example.listview;

import java.util.Comparator;

public class BikeData {

    String Company, Model, Location, Date, Descripton, Picture, Link;
    double Price;
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */

    public BikeData(String c, String m, String l, String da, String de, String pic, String li, double p) {
        String Company = c;
        String Model = m;
        String Location = l;
        String Date = da;
        String Descripton = de;
        String Picture = pic;
        String Link = li;
        double Price = p;
    }

    @Override
    public String toString() {
        String temp = "";
        temp += "Company: " + Company + "\n";
        temp += "Model: " + Model + "\n";
        temp += "Location: " + Location + "\n";
        temp += "Date: " + Date + "\n";
        temp += "Description: " + Descripton + "\n";
        temp += "Link: " + Link + "\n";
        temp += "Price: " + Price;
        return temp;
    }

    //TODO create your comparators
//TODO Done?
    class ComparatorModel implements Comparator<BikeData> {
        public int compare(BikeData myData1, BikeData myData2) {
// if both equal then 0
            return (myData1.Model.compareTo(myData2.Model));
        }
    }

    class ComparatorPrice implements Comparator<BikeData> {
        public int compare(BikeData myData1, BikeData myData2) {
// if both equal then 0
            if (myData1.Price > myData2.Price)
                return 1;
            else if (myData1.Price < myData2.Price)
                return -1;
            return 0;
        }
    }
}