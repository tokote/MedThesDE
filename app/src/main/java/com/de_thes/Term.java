package com.de_thes;

/**
 * Created by tom on 25.09.16.
 */
public class Term {

    private String name;
    private int listViewPosition;
    private int D_number;

    public Term(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


    public int getD_number() {
        return D_number;
    }

    public void setD_number(int d_number) {
        D_number = d_number;
    }


    public int getPosition() {
        return listViewPosition;
    }

    public void setPosition(int position) {
        this.listViewPosition = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
