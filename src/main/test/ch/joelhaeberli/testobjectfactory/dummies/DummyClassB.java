package ch.joelhaeberli.testobjectfactory.dummies;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DummyClassB {

    private int dummyIntOfB;

    public DummyClassB() {
    }

    public int getDummyIntOfB() {
        return dummyIntOfB;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("dummyIntOfB", dummyIntOfB)
                .toString();
    }
}
