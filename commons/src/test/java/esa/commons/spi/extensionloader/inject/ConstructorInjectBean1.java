package esa.commons.spi.extensionloader.inject;

import esa.commons.spi.factory.Inject;

public class ConstructorInjectBean1 implements ConstructorInjectBean {

    private String name;
    private int age;
    private Integer salary;
    private ConstructorInjectBean2 bean2;

    public ConstructorInjectBean1() {}

    @Inject(name = "bean2")
    public ConstructorInjectBean1(int age, String name, Integer salary, ConstructorInjectBean2 bean2) {
        this.age = age;
        this.name = name;
        this.salary = salary;
        this.bean2 = bean2;
    }

    @Override
    public Object getInject() {
        return bean2;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Integer getSalary() {
        return salary;
    }
}
