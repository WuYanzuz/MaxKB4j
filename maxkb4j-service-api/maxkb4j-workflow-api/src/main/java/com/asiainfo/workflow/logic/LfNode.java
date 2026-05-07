package com.asiainfo.workflow.logic;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class LfNode {
    private String id;
    private String type;
    private Integer x;
    private Integer y;
    private JSONObject properties;
}
