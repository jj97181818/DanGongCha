package me.jj97181818.dangongcha.api;

import java.io.Serializable;
import java.util.List;

public class Reals implements Serializable {
    public List<Real> buses;
    public class Real {
        public String routeUID;     //路線辨識碼
        public String routeName;    //路線名稱
        public String stopUID;      //站牌辨識碼
        public String stopName;     //站牌名稱
        public String estimateTime; //估計時間
        public String busNumber;    //車輛號碼
        public String busStatus;    //車輛狀態
        public String arriving;     //進站狀態
    }
}
