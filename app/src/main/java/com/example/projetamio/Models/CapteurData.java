package com.example.projetamio.Models;

import java.sql.Timestamp;

public class CapteurData {
    public Long timestamp ;
    public String label;
    public Double value;
    public String mote;

    public  CapteurData(Long timestamp, String label, Double value , String mote){
        this.label = label;
        this.timestamp = timestamp;
        this.value = value;
        this.mote = mote;
    }

}
