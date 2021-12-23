/*
 * Copyright 2020 OPPO ESA Stack Project
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

import esa.commons.spi.factory.Inject;

public class ConstructorInjectBean1 implements ConstructorInjectBean {

    private String name;
    private int age;
    private Integer salary;
    private ConstructorInjectBean2 bean2;

    public ConstructorInjectBean1() {
    }

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
