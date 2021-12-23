package esa.commons.spi.extensionloader.inject;

import esa.commons.spi.factory.Inject;

/**
 * FiledInjectBean1
 *
 * @author guconglin
 * @date 2021/12/23 16:08
 */
public class FiledInjectBean1 implements FiledInjectBean {
    private String name;

    @Inject
    private int age;

    @Inject
    private FiledInjectBean2 bean2;

    @Override
    public Object getInject() {
        return bean2;
    }
}
