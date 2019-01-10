package me.jj97181818.dangongcha.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Infos implements Serializable {
    public List<Route> routes;
    public class Route {
        public String routeUID;    //路線辨識碼
        public String routeName;   //路線名稱
        public String city;    //城市英文名稱
        public String departureStopName;   //起站名稱
        public String destinationStopName; //終站名稱
    }
}

