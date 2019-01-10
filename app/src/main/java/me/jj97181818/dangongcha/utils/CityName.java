package me.jj97181818.dangongcha.utils;

import java.util.Arrays;

public class CityName {
    //中文縣市名稱
    public static final String[] CHINESE = {
            "基隆市", "新北市", "台北市", "宜蘭縣", "桃園市",
            "新竹市", "新竹縣", "苗栗縣", "台中市", "彰化縣",
            "南投縣", "雲林縣", "嘉義市", "嘉義縣", "台南市",
            "高雄市", "屏東縣", "台東縣", "花蓮縣", "澎湖縣",
            "金門縣", "連江縣", "公路客運"
    };

    //中文縣市名稱
    public static final String[] ENGLISH = {
            "Keelung", "NewTaipei", "Taipei", "YilanCounty", "Taoyuan",
            "Hsinchu", "HsinchuCounty", "MiaoliCounty", "Taichung", "ChanghuaCounty",
            "NantouCounty", "YunlinCounty", "Chiayi", "ChiayiCounty", "Tainan",
            "Kaohsiung", "PingtungCounty", "TaitungCounty", "HualienCounty", "PenghuCounty",
            "KinmenCounty", "LienchiangCounty", "InterCity"
    };

    //縣市名稱英文轉中文
    public static String toChinese(String englishName) {
        int index = Arrays.asList(ENGLISH).indexOf(englishName);
        return CHINESE[index];
    }

    //縣市名稱中文轉英文
    public static String toEnglish(String chineseName) {
        int index = Arrays.asList(CHINESE).indexOf(chineseName);
        return ENGLISH[index];
    }
}
