package me.jj97181818.dangongcha.api;

import java.io.Serializable;
import java.util.List;

public class Stops implements Serializable {
    public List<Route> routes;
    public class Route {
        public String routeUID;     //路線辨識碼
        public String routeName;    //路線名稱
        public String city;         //城市英文名稱
        public List<SubRoute> subRoutes;
        public class SubRoute {
            public String subRouteUID;  //子路線辨識碼
            public String subRouteName; //子路線名稱
            public List<Stop> stops;
            public class Stop {
                public String stopUID;      //站牌辨識碼
                public String stopName;     //站牌名稱
            }
        }
    }
}
