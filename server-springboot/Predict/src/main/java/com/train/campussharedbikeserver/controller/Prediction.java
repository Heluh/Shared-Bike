package com.train.campussharedbikeserver.controller;

import com.train.campussharedbikeserver.pojo.PreData;
import com.train.campussharedbikeserver.service.PredictionBikeLevels;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Controller
@RefreshScope
public class Prediction {
    @RequestMapping("/test")
    @ResponseBody
    public void test(){
        System.out.println("网关调用成功");
    }


    @Autowired
    private PredictionBikeLevels preBikeLevels;
    @RequestMapping(value = "/pre",method= RequestMethod.POST)
    @ResponseBody
    public Object preWater1(@RequestBody Map<String,String> map) throws IOException, ParseException, InterruptedException {
        System.out.println("预测网关调用成功");
        //原始数据
        File file = new File("D:\\shixun\\final\\server-springboot\\Predict\\src\\main\\resources\\csv\\fallraw_7041JA26clea.csv");
        String csvPath = file.getAbsolutePath();

        CsvReader csvReader = new CsvReader();
        csvReader.setContainsHeader(true);

        List<PreData> list = new ArrayList<>();
        CsvContainer csv = csvReader.read(file, StandardCharsets.UTF_8);
        String date = map.get("mydate");
        System.out.println(date);
//        预测的数据
        if("2023-07-01".equals(date))
        {
            for (int i = csv.getRowCount() - 2299; i < csv.getRowCount()-2275; i++) {
                PreData preData = new PreData();
                preData.setData(csv.getRows().get(i).getField(12));
                preData.setTime(csv.getRows().get(i).getField(0));
                list.add(preData);
            }
        }
        else if("2023-07-02".equals(date)){
            for (int i = csv.getRowCount() - 2275; i < csv.getRowCount()-2251; i++) {
                PreData preData = new PreData();
                preData.setData(csv.getRows().get(i).getField(12));
                preData.setTime(csv.getRows().get(i).getField(0));
                list.add(preData);
            }
        }
        else if("2023-07-03".equals(date)){
            for (int i = csv.getRowCount() - 2251; i < csv.getRowCount()-2227; i++) {
                PreData preData = new PreData();
                preData.setData(csv.getRows().get(i).getField(12));
                preData.setTime(csv.getRows().get(i).getField(0));
                list.add(preData);
            }
        }
        else if("2023-07-04".equals(date)){
            for (int i = csv.getRowCount() - 2227; i < csv.getRowCount()-2203; i++) {
                PreData preData = new PreData();
                preData.setData(csv.getRows().get(i).getField(12));
                preData.setTime(csv.getRows().get(i).getField(0));
                list.add(preData);
            }
        }
        else if("2023-07-05".equals(date)){
            for (int i = csv.getRowCount() - 2203; i < csv.getRowCount()-2179; i++) {
                PreData preData = new PreData();
                preData.setData(csv.getRows().get(i).getField(12));
                preData.setTime(csv.getRows().get(i).getField(0));
                list.add(preData);
            }
        }
        else if("2023-07-06".equals(date)){
            for (int i = csv.getRowCount() - 2179; i < csv.getRowCount()-2155; i++) {
                PreData preData = new PreData();
                preData.setData(csv.getRows().get(i).getField(12));
                preData.setTime(csv.getRows().get(i).getField(0));
                list.add(preData);
            }
        }
        else{
            for (int i = csv.getRowCount() - 2155; i < csv.getRowCount()-2131; i++) {
                PreData preData = new PreData();
                preData.setData(csv.getRows().get(i).getField(12));
                preData.setTime(csv.getRows().get(i).getField(0));
                list.add(preData);
            }
        }
        String mx = map.get("mx");
        if ("".equals(mx)){
            for (int i = csv.getRowCount() - 20; i < csv.getRowCount(); i++) {
                PreData preData = new PreData();
                preData.setData(csv.getRows().get(i).getField(12));
                preData.setTime(csv.getRows().get(i).getField(0));
                list.add(preData);
            }
            return list;
        }
        //预测数据
        List<Double> preList = preBikeLevels.preBikeLevels(mx, csvPath);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();

        for (int i = 0; i < preList.size(); i++) {
            PreData d = new PreData();
            d.setData(preList.get(i).toString());
            c.add(Calendar.HOUR_OF_DAY, 1);
            d.setTime(df.format(c.getTime()));
            list.add(d);
        }
        return list;
    }
}
