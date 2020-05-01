package pojo;

public class Problem {
    String problemType;//问题分类
    String jx;//机型
    String describe;//问题描述
    String unit;//责任单位

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getJx() {
        return jx;
    }

    public void setJx(String jx) {
        this.jx = jx;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Problem() {
    }

    public Problem(String problemType, String jx, String describe, String unit) {
        this.problemType = problemType;
        this.jx = jx;
        this.describe = describe;
        this.unit = unit;
    }
}
