package ch.joelhaeberli.testobjectfactory.dummies;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DummyClassA {

    private int dummyInt;
    public int dummyInt2;
    protected int dummyInt3;

    private BigDecimal dummyBigDecimal;
    public BigDecimal dummyBigDecimal2;
    protected BigDecimal dummyBigDecimal3;

    private List<DummyClassB> dummyList;
    private Map<String, DummyClassB> dummyMap;
    private DummyClassB dummyClassB;

    public DummyClassA() {
    }

    public int getDummyInt() {
        return dummyInt;
    }

    public int getDummyInt2() {
        return dummyInt2;
    }

    public int getDummyInt3() {
        return dummyInt3;
    }

    public BigDecimal getDummyBigDecimal() {
        return dummyBigDecimal;
    }

    public BigDecimal getDummyBigDecimal2() {
        return dummyBigDecimal2;
    }

    public BigDecimal getDummyBigDecimal3() {
        return dummyBigDecimal3;
    }

    public List<DummyClassB> getDummyList() {
        return dummyList;
    }

    public Map<String, DummyClassB> getDummyMap() {
        return dummyMap;
    }

    public DummyClassB getDummyClassB() {
        return dummyClassB;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("dummyInt", dummyInt)
                .append("dummyInt2", dummyInt2)
                .append("dummyInt3", dummyInt3)
                .append("dummyBigDecimal", dummyBigDecimal)
                .append("dummyBigDecimal2", dummyBigDecimal2)
                .append("dummyBigDecimal3", dummyBigDecimal3)
                .append("dummyList", dummyList)
                .append("dummyMap", dummyMap)
                .append("dummyClassB", dummyClassB)
                .toString();
    }
}
