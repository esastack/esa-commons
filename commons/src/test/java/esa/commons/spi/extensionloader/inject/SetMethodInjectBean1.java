package esa.commons.spi.extensionloader.inject;

import esa.commons.spi.factory.Inject;

public class SetMethodInjectBean1 implements SetMethodInjectBean {

    private int age;
    private SetMethodInjectBean2 bean2;

    @Inject(name = "bean2")
    public void setMethodInjectBean2(SetMethodInjectBean2 bean2) {
        this.bean2 = bean2;
    }

    @Inject
    public void setAge(int age) {
        this.age = age;
    }

    public void nonSetMethod() {}
    public void setBean2() {}

    @Override
    public Object getInject() {
        return bean2;
    }
}
