package esa.commons.spi.extensionloader.inject;

import esa.commons.spi.factory.Inject;

public class SetMethodInjectBean2 implements SetMethodInjectBean {
    private SetMethodInjectBean1 bean1;

    @Inject(name = "bean1")
    public void setInjectBean(SetMethodInjectBean1 bean1) {
        this.bean1 = bean1;
    }

    @Override
    public Object getInject() {
        return bean1;
    }
}
