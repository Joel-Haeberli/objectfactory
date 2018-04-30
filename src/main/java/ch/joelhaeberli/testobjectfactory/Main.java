package ch.joelhaeberli.testobjectfactory;

import ch.joelhaeberli.testobjectfactory.factory.TestObjectFactory;

public class Main {

    public static void main(String[] args) {

        HouseTestClass houseTestClass = TestObjectFactory.getInstance().getTestObject(HouseTestClass.class);

        System.out.println(houseTestClass.toString());
    }
}
