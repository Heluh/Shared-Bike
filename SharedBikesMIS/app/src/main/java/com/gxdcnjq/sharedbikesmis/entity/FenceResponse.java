package com.gxdcnjq.sharedbikesmis.entity;

import java.util.List;

public class FenceResponse {
    private String code;
    private String msg;
    private List<FenceData> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<FenceData> getData() {
        return data;
    }

    public void setData(List<FenceData> data) {
        this.data = data;
    }
}
