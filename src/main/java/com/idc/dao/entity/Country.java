package com.idc.dao.entity;

import lombok.Data;

import java.util.List;

@Data
public class Country {
    private String cy;

    private List<String> aliases;

    private String type;

    private String label;

    private String cx;

}