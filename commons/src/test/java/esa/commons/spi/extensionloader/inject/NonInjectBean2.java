/*
 * Copyright 2021 OPPO ESA Stack Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package esa.commons.spi.extensionloader.inject;

import esa.commons.spi.Feature;
import esa.commons.spi.factory.Inject;

@Feature(name = "bean2")
public class NonInjectBean2 implements NonInjectBean {
    @Inject(name = "bean8")
    private NonInjectBean bean;

    @Inject(name = "bean8")
    public void setBean(NonInjectBean bean) {
        this.bean = bean;
    }
}
