package com.example.projetamio;

import java.sql.Timestamp;

public class Data {
    private Long timestamp;
    private String label;
    private Double value;
    private String mote;

    public Data(){
        this.timestamp = null;
        this.label = null;
        this.value = null;
        this.mote = null;
    }

    public Data(Long timestamp, String label, Double value, String mote){
        this.timestamp = timestamp;
        this.label = label;
        this.value = value;
        this.mote = mote;
    }

    public Long getTimestamp(){
        return this.timestamp;
    }

    public String getLabel(){
        return this.label;
    }

    public Double getValue(){
        return this.value;
    }

    public String getMote(){
        return this.mote;
    }

    public void setTimestamp(Long timestamp){
        this.timestamp = timestamp;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public void setValue(Double value){
        this.value = value;
    }

    public void setMote(String mote){
        this.mote = mote;
    }


}
