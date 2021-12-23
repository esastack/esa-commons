package esa.commons.spi.extensionloader.inject;

import esa.commons.spi.factory.Inject;

public class ConstructorInjectCycleBean2 implements ConstructorInjectCycleBean {

    private ConstructorInjectCycleBean1 bean1;

    @Inject(name = "bean1")
    public ConstructorInjectCycleBean2(ConstructorInjectCycleBean1 bean1) {
        this.bean1 = bean1;
    }

    @Override
    public Object getInject() {
        return bean1;
    }
}
