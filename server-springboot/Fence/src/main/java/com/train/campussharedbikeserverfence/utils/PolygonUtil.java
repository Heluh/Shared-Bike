package com.train.campussharedbikeserverfence.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;

public class PolygonUtil {

    public static boolean isPointInsidePolygon(double pointLng, double pointLat, String polygonPoints) {
        // 解析多边形点坐标
        Coordinate[] coordinates = parsePolygonPoints(polygonPoints);
        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
        Polygon polygon = geometryFactory.createPolygon(linearRing, null);

        // 创建点
        Point point = geometryFactory.createPoint(new Coordinate(pointLng, pointLat));

        // 检测点是否在多边形内
        return polygon.contains(point);
    }

    private static Coordinate[] parsePolygonPoints(String polygonPoints) {
        JSONArray jsonArray = JSONArray.parseArray(polygonPoints);
        Coordinate[] coordinates = new Coordinate[jsonArray.size() + 1]; // 坐标数组长度 +1
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject point = jsonArray.getJSONObject(i);
            double lng = point.getDoubleValue("lng");
            double lat = point.getDoubleValue("lat");
            coordinates[i] = new Coordinate(lng, lat);
        }
        // 添加起始点作为闭合点
        coordinates[jsonArray.size()] = coordinates[0];
        return coordinates;
    }
}
