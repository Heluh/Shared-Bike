package com.train.campussharedbikeserver.service.impl;

import com.train.campussharedbikeserver.service.PredictionBikeLevels;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ink
 * 2022/7/5
 */
@Service
public class PredictionBikeLevelsImpl implements PredictionBikeLevels {

//    private RedissonClient redisson;

    private final String python = "C:\\Users\\l1065\\AppData\\Local\\Programs\\Python\\Python39\\python.exe";
    private final String pyPath = "D:\\shixun\\final\\server-springboot\\Predict\\python\\main.py";

    @Override
    public List<Double> preBikeLevels(String type, String csvPath) throws IOException, InterruptedException {
        //预测数据
        String[] arguments = new String[]{python, pyPath, csvPath, String.valueOf(type)};
        String s = "";

        long l = System.currentTimeMillis();
        Process proc = Runtime.getRuntime().exec(arguments);
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            if (line.contains("==")) continue;
            s += line;
        }
        in.close();
        proc.waitFor();
        /*//打印错误日志
        BufferedReader errorIn = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        while ((line = errorIn.readLine()) != null) {
            System.out.println(line);
        }
        errorIn.close();*/
        // 提取数据
        String regx = "[0-9]\\d*.\\d*|-([1-9]\\d*.\\d*|0\\.\\d*[1-9]\\d*)";
        System.out.println(regx);
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(s);


        List<Double> preList = new ArrayList<>();
        DecimalFormat dateFormat = new DecimalFormat("#.00");
        while (matcher.find()) {
            preList.add(Double.valueOf(dateFormat.format(Double.valueOf(matcher.group()))));
        }
        System.out.println((System.currentTimeMillis() - l) / 1000);

        return preList;
    }


}
