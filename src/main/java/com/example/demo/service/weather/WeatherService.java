package com.example.demo.service.weather;

import com.example.demo.dto.weather.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    private String weatherAuthKey;

    public WeatherService(@Value("${authKey.weatherInfo}") String key) {
        weatherAuthKey = key;
    }

    @Transactional
    public List<WeatherResultDto> getWeatherInfo() throws IOException {

        LocalDateTime now = LocalDateTime.now();

        String date = now.getDayOfMonth() >= 10? "" + now.getDayOfMonth() : "0" + now.getDayOfMonth();
        String dateValue = now.getYear() + String.valueOf(now.getMonthValue()) + date;
        String timeValue = (now.getHour() - 1) < 10? "0" + (now.getHour() - 1) + "30" : (now.getHour() - 1) + "30";

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + weatherAuthKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(dateValue, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(timeValue, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("64", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("119", "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        String line = rd.readLine();

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject jsonObject = new JSONObject(line);
        JSONObject rmResponse = new JSONObject(jsonObject.get("response").toString());
        JSONObject rmBody = new JSONObject(rmResponse.get("body").toString());
        JSONObject rmItems = new JSONObject(rmBody.get("items").toString());

        WeatherListDto result = objectMapper.readValue(rmItems.toString(), WeatherListDto.class);

        List<WeatherResultDto> lst = joinWeatherData(result.getItem());

        rd.close();
        conn.disconnect();
        return lst;
    }

    public List<WeatherResultDto> joinWeatherData(List<WeatherInfoDto> lst) {

        List<WeatherResultDto> result = new ArrayList<>(6);

        for(int i = 0; i < 6; i++) {
            result.add(new WeatherResultDto());
        }

        int i = 0;

        for(WeatherInfoDto s : lst) {
            WeatherResultDto value = result.get(i);
            String category = s.getCategory();

            switch(category) {
                case "T1H":
                    value.setTemperature(s.getFcstValue());
                    break;

                case "RN1":
                    value.setRainFall(s.getFcstValue());
                    break;

                case "SKY":
                    String skyValue = s.getFcstValue();

                    if(skyValue.equals("1")) {
                        value.setSkyCondition("맑음");
                    }
                    else if(skyValue.equals("3")) {
                        value.setSkyCondition("구름 많음");
                    }
                    else {
                        value.setSkyCondition("흐림");
                    }

                    break;

                case "REH":
                    value.setHumidity(s.getFcstValue() + "%");
                    break;

                case "PTY":

                    String rainFallValue = s.getFcstValue();

                    if(rainFallValue.equals("0")) {
                        value.setRainType("");
                    }
                    else if(rainFallValue.equals("1")) {
                        value.setRainType("비");
                    }
                    else if(rainFallValue.equals("2")) {
                        value.setRainType("비/눈");
                    }
                    else if(rainFallValue.equals("3")) {
                        value.setRainType("눈");
                    }
                    else if(rainFallValue.equals("5")) {
                        value.setRainType("빗방울");
                    }
                    else if(rainFallValue.equals("6")) {
                        value.setRainType("빗방울/눈날림");
                    }
                    else if(rainFallValue.equals("7")) {
                        value.setRainType("눈날림");
                    }

                    break;

                default:
                    break;
            }

            if(i < 6) {
                value.setPreDate(s.getFcstDate());
                value.setPreTime(s.getFcstTime());
            }

            i = (i+1) % 6;
        }

        return result;
    }
}
