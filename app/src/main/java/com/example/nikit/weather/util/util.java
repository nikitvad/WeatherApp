package com.example.nikit.weather.util;

/**
 * Created by nikit on 20.02.2017.
 */

public class util {
    public static int tempConvert(int temp, String measure){

        if(measure.equals("˚C")){
            temp-=273;
        }else if(measure.equals("˚F")){
            temp-=273;
            temp*=1.8;
            temp+=32;
        }
        return temp;
    }
}
