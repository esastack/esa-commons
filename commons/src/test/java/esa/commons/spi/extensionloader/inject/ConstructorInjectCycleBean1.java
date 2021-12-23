package esa.commons.spi.extensionloader.inject;


import esa.commons.spi.factory.Inject;

public class ConstructorInjectCycleBean1 implements ConstructorInjectCycleBean {

    private ConstructorInjectCycleBean2 bean2;

    @Inject(name = "bean2")
    public ConstructorInjectCycleBean1(ConstructorInjectCycleBean2 bean2) {
        this.bean2 = bean2;
    }

    @Override
    public Object getInject() {
        return bean2;
    }
}
