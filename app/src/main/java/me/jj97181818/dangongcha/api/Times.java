package me.jj97181818.dangongcha.api;

import java.io.Serializable;
import java.util.List;

public class Times implements Serializable {
    public List<Time> stops;
    public class Time {
        public String routeUID;     //路線辨識碼
        public String routeName;    //路線名稱
        public String stopUID;      //站牌辨識碼
        public String stopName;     //站牌名稱
        public String estimateTime; //估計時間
        public String stopStatus;   //站牌狀態
    }
}
