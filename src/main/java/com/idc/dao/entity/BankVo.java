package com.idc.dao.entity;

public class BankVo {
    private String CountryName; // ID
    private String CountryCode; // 父节点ID
    private String SeriesName;  // 节点名称
    private String SeriesCode; // 属性数据
    private String data2016; // ID
    private String data2017; // 父节点ID
    private String data2018;  // 节点名称
    private String data2019; // 属性数据
    private String data2020; // 属性数据

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getSeriesName() {
        return SeriesName;
    }

    public void setSeriesName(String seriesName) {
        SeriesName = seriesName;
    }

    public String getSeriesCode() {
        return SeriesCode;
    }

    public void setSeriesCode(String seriesCode) {
        SeriesCode = seriesCode;
    }

    public String getData2016() {
        return data2016;
    }

    public void setData2016(String data2016) {
        this.data2016 = data2016;
    }

    public String getData2017() {
        return data2017;
    }

    public void setData2017(String data2017) {
        this.data2017 = data2017;
    }

    public String getData2018() {
        return data2018;
    }

    public void setData2018(String data2018) {
        this.data2018 = data2018;
    }

    public String getData2019() {
        return data2019;
    }

    public void setData2019(String data2019) {
        this.data2019 = data2019;
    }

    public String getData2020() {
        return data2020;
    }

    public void setData2020(String data2020) {
        this.data2020 = data2020;
    }
}
