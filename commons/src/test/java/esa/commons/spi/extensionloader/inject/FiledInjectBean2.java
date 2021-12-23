package esa.commons.spi.extensionloader.inject;

import esa.commons.spi.factory.Inject;

/**
 * FiledInjectBean2
 *
 * @author guconglin
 * @date 2021/12/23 16:09
 */
public class FiledInjectBean2 implements FiledInjectBean {
    @Inject(name = "bean1")
    private FiledInjectBean1 filedInjectBean1;

    @Override
    public Object getInject() {
        return filedInjectBean1;
    }
}
