package com.train.campussharedbikeserver.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface PredictionBikeLevels {
    /**
     * @param type  选择不同的模型
     *                1: TSLM
     *                2:
     * @param csvPath 要预测数据的csv文件
     * @return 预测的数值
     */
    List<Double> preBikeLevels(String type,String csvPath) throws IOException, InterruptedException, ParseException;
}
