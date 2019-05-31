package ExcelTest2.standard.entity;

import java.util.List;

/**
 * auth:lht
 * time: 2018年11月23日17:44:14
 */
public class TypeInfo {
    private String type;        //保存类型信息 本程序只确定三种值 整形 浮点型 其他
    private List<String> types; //保存具体的类型
    private double min;         //保存当前最小值        （整形 浮点型）
    private double max;         //保存当前最大值        （整形 浮点型）
    private String over;
    private double Dvalue;

    public TypeInfo(String type, List<String> types) {
        this.type = type;
        this.types = types;
    }

    public TypeInfo() {
    }

    @Override
    public String toString() {
        return "TypeInfo{" +
                "type='" + type + '\'' +
                ", types=" + types +
                ", min=" + min +
                ", max=" + max +
                ", over='" + over + '\'' +
                ", Dvalue=" + Dvalue +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public double getDvalue() {
        return Dvalue;
    }

    public void setDvalue(double dvalue) {
        Dvalue = dvalue;
    }
}
