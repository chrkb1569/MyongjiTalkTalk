package com.example.demo.service.bus;

import com.example.demo.dto.bus.BusArriveResultListDto;
import com.example.demo.dto.bus.BusArriveInfoDto;
import com.example.demo.dto.bus.BusArriveResultDto;
import com.example.demo.dto.bus.BusRouteSearchResultDto;
import com.example.demo.dto.bus.BusRouteListDto;
import com.example.demo.dto.bus.BusStationRequestDto;
import com.example.demo.dto.bus.BusStationResponseDto;
import com.example.demo.dto.bus.BusStationSearchResultDto;
import com.example.demo.exeption.bus.EmptyBusStationException;
import com.example.demo.exeption.bus.StationNotFoundException;
import com.example.demo.exeption.bus.WrongBusInfoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusService {
    private String busStationAuthKey;
    private String busArriveAuthKey;
    private String busRouteAuthKey;

    public BusService(@Value("${authKey.busArriveInfo}") String arriveKey,
                      @Value("${authKey.busStationInfo}") String stationKey,
                      @Value("${authKey.busRouteInfo}") String routeKey) {
        busStationAuthKey = stationKey;
        busArriveAuthKey = arriveKey;
        busRouteAuthKey = routeKey;
    }

    @Transactional(readOnly = true) // 해당 정류장에 도착하는 버스들과 도착 시간들 반환
    public List<BusArriveResultDto> getBusArriveTimeList(String stationId) throws IOException {
        List<BusArriveInfoDto> lst = getArriveTime(stationId);

        List<BusArriveResultDto> result = new LinkedList<>();

        for(BusArriveInfoDto s : lst) {
            BusArriveResultDto component = new BusArriveResultDto().toDto(s);
            component.setBusNumber(getBusNumber(component.getBusNumber()));
            result.add(component);
        }

        return result;
    }

    @Transactional(readOnly = true) // 해당 버스의 노선 반환
    public List<String> getBusRoute(String stationId, String busNumber) throws IOException {
        String routeId = getBusRouteId(busNumber);

        return getBusRoute(routeId).stream().map(s->s.getStationName()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true) // 정류장 검색 로직
    public List<BusStationSearchResultDto> getBusStationList(BusStationRequestDto requestDto) throws IOException{
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/4050000/busstop/getBusstop");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + busStationAuthKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("stop_nm","UTF-8") + "=" + URLEncoder.encode(requestDto.getStop_nm(), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("gu","UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("stty_emd_nm","UTF-8") + "=" + URLEncoder.encode("", "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        int statusCode = conn.getResponseCode();

        BufferedReader rd;
        if(statusCode >= 200 && statusCode <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        String line = rd.readLine();

        ObjectMapper objectMapper = new ObjectMapper();
        BusStationResponseDto responseDto = objectMapper.readValue(line, BusStationResponseDto.class);

        rd.close();
        conn.disconnect();

        if(responseDto.getResultCode() == null || Integer.parseInt(responseDto.getResultCode()) != 0) {
            throw new StationNotFoundException(requestDto.getStop_nm());
        }

        return responseDto.getItems();
    }

    @Transactional(readOnly = true) // 버스의 routeId를 통하여 버스의 번호값 반환
    public String getBusNumber(String routeId) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busrouteservice/getBusRouteInfoItem");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + busRouteAuthKey);
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode(routeId, "UTF-8"));
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

        JSONObject rawData = XML.toJSONObject(line);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        JSONObject rmResponse = new JSONObject(rawData.get("response").toString());
        JSONObject header = new JSONObject(rmResponse.get("msgHeader").toString());
        if(!header.get("resultCode").toString().equals("0")) {
            throw new WrongBusInfoException();
        }
        JSONObject rmMsgBody = new JSONObject(rmResponse.get("msgBody").toString());
        JSONObject rmBusRouteInfoItem = new JSONObject(rmMsgBody.get("busRouteInfoItem").toString());

        rd.close();
        conn.disconnect();

        return rmBusRouteInfoItem.get("routeName").toString();
    }

    @Transactional(readOnly = true) // 버스 번호를 입력할 경우 버스의 routeID를 반환
    public String getBusRouteId(String routeName) throws IOException{
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busrouteservice/getAreaBusRouteList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + busRouteAuthKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("areaId","UTF-8") + "=" + URLEncoder.encode("23", "UTF-8")); /*운행지역 ID (세부내용 별첨 참조)*/
        urlBuilder.append("&" + URLEncoder.encode("keyword","UTF-8") + "=" + URLEncoder.encode(routeName, "UTF-8")); /*노선번호*/
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

        JSONObject rawData = XML.toJSONObject(line);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        JSONObject rmResponse = new JSONObject(rawData.get("response").toString());
        JSONObject header = new JSONObject(rmResponse.get("msgHeader").toString());

        if(!header.get("resultCode").toString().equals("0")) {
            throw new WrongBusInfoException();
        }
        JSONObject rmMsgBody = new JSONObject(rmResponse.get("msgBody").toString());
        JSONObject rmBusRouteList = new JSONObject(rmMsgBody.get("busRouteList").toString());

        rd.close();
        conn.disconnect();

        return rmBusRouteList.get("routeId").toString();
    }

    @Transactional // 해당 버스의 노선 정보 확인
    public List<BusRouteSearchResultDto> getBusRoute(String routeId) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busrouteservice/getBusRouteStationList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + busRouteAuthKey);
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode(routeId, "UTF-8"));
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

        JSONObject rawData = XML.toJSONObject(line);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        JSONObject rmResponse = new JSONObject(rawData.get("response").toString());
        JSONObject header = new JSONObject(rmResponse.get("msgHeader").toString());

        if(!header.get("resultCode").toString().equals("0")) {
            throw new WrongBusInfoException();
        }

        JSONObject rmMsgBody = new JSONObject(rmResponse.get("msgBody").toString());
        BusRouteListDto lst = objectMapper.readValue(rmMsgBody.toString(), BusRouteListDto.class);

        rd.close();
        conn.disconnect();

        return lst.getBusRouteStationList();
    }

    @Transactional(readOnly = true) // 해당 정류장에 도착할 예정인 버스들을 조회,
    public List<BusArriveInfoDto> getArriveTime(String stationId) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + busArriveAuthKey );
        urlBuilder.append("&" + URLEncoder.encode("stationId","UTF-8") + "=" + URLEncoder.encode(stationId, "UTF-8"));
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

        JSONObject rawData = XML.toJSONObject(line);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        JSONObject rmResponse = new JSONObject(rawData.get("response").toString());
        JSONObject header = new JSONObject(rmResponse.get("msgHeader").toString());

        if(!header.get("resultCode").toString().equals("0")) {
            throw new EmptyBusStationException();
        }

        JSONObject rmMsgBody = new JSONObject(rmResponse.get("msgBody").toString());

        BusArriveResultListDto resultDto = objectMapper.readValue(rmMsgBody.toString(), BusArriveResultListDto.class);

        rd.close();
        conn.disconnect();

        return resultDto.getBusArrivalList();
    }
}
