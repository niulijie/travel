package com.niulijie.easyexcel.enums;

/**
 * 自定义属性基本类型校验规则定义
 * @author Sunyi
 */
public enum Rule {

    INPUT(1,"^.{1,20}$"),
    TEXTAREA(2,"^.{1,200}$"),
    TELEPHONE(3,"0?(13|14|15|17|18|19)[0-9]{9}"),
    EMAIL(4,"^\\w+@[a-zA-Z0-9]{2,10}(?:\\.[a-z]{2,4}){1,3}$"),
    HREF(5,"^((https|http|ftp|rtsp|mms)?:\\\\/\\\\/)[^\\\\s]+"),
    NUMBER(6,"-?[0-9]\\\\d*"),
    DATE(7,"^[1-9]\\\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$"),
    TIME(8," /^(20|21|22|23|[0-1]\\\\d):[0-5]\\\\d:[0-5]\\\\d$/"),
    SELECT(9,null),
    RADIO(13,null),
    CHECKBOX(10,null),
    SUPER_SELECT(12,null),
    SWITCH(11,""),
    ;

    private Integer attrId;

    private String attrRule;

    Rule(Integer attrId, String attrRule) {
        this.attrId = attrId;
        this.attrRule = attrRule;
    }

    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public String getAttrRule() {
        return attrRule;
    }

    public void setAttrRule(String attrRule) {
        this.attrRule = attrRule;
    }

    public static Rule getbyid(String index) {
        for(Rule rule : values()) {
            if(rule.getAttrId().equals(index)) {
                return rule ;
            }
        }
        return null;
    }
}
