package esa.commons.spi.extensionloader.inject;

import esa.commons.spi.SPI;

@SPI
public interface FiledInjectBean {
    Object getInject();
}
