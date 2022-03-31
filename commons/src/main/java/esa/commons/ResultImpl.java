/*
 * Copyright 2022 OPPO ESA Stack Project
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
package esa.commons;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

final class ResultImpl {

    private static final NullOk<?, ?> NULL_OK = new NullOk<>();
    private static final NullErr<?, ?> NULL_ERR = new NullErr<>();

    static <T, E> Result<T, E> ok(T ok) {
        return new Ok<>(ok);
    }

    static <T, E> Result<T, E> err(E err) {
        return new Err<>(err);
    }

    @SuppressWarnings("unchecked")
    static <T, E> Result<T, E> ok() {
        return (Result<T, E>) NULL_OK;
    }

    @SuppressWarnings("unchecked")
    static <T, E> Result<T, E> err() {
        return (Result<T, E>) NULL_ERR;
    }

    private static final class Ok<T, E> implements Result<T, E> {

        private final T value;

        Ok(T value) {
            this.value = value;
        }

        @Override
        public void ifOk(Consumer<? super T> op) {
            op.accept(value);
        }

        @Override
        public void ifError(Consumer<? super E> op) {
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public T get(String msg) {
            return value;
        }

        @Override
        public T getOrElse(T another) {
            return value;
        }

        @Override
        public E getErr() {
            throw new NoSuchElementException("called `Result.getErr()` on an `Ok` value: " + value);
        }

        @Override
        public E getErr(String msg) {
            throw new NoSuchElementException(msg);
        }

        @Override
        public E getErrOrElse(E another) {
            return another;
        }

        @Override
        public Result<T, E> map(Function<? super T, ? extends T> fn) {
            final T t = fn.apply(value);
            return t == null ? ok() : ok(t);
        }

        @Override
        public Result<T, E> mapErr(Function<? super E, ? extends E> fn) {
            return this;
        }

        @Override
        public boolean isOk() {
            return true;
        }

        @Override
        public boolean isErr() {
            return false;
        }

        @Override
        public boolean contains(T v) {
            return ObjectUtils.safeEquals(value, v);
        }

        @Override
        public boolean containsErr(E e) {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Ok<?, ?> ok = (Ok<?, ?>) o;
            return Objects.equals(value, ok.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return "Result::Ok[" + value + "]";
        }
    }

    private static class NullOk<T, E> implements Result<T, E> {

        @Override
        public void ifOk(Consumer<? super T> op) {
            op.accept(null);
        }

        @Override
        public void ifError(Consumer<? super E> op) {
        }

        @Override
        public T get() {
            return null;
        }

        @Override
        public T get(String msg) {
            return null;
        }

        @Override
        public T getOrElse(T another) {
            return null;
        }

        @Override
        public E getErr() {
            throw new NoSuchElementException("called `Result.getErr()` on an `Ok` value: null");
        }

        @Override
        public E getErr(String msg) {
            throw new NoSuchElementException(msg);
        }

        @Override
        public E getErrOrElse(E another) {
            return another;
        }

        @Override
        public Result<T, E> map(Function<? super T, ? extends T> fn) {
            final T t = fn.apply(null);
            return t == null ? ok() : ok(t);
        }

        @Override
        public Result<T, E> mapErr(Function<? super E, ? extends E> fn) {
            return this;
        }

        @Override
        public boolean isOk() {
            return true;
        }

        @Override
        public boolean isErr() {
            return false;
        }

        @Override
        public boolean contains(T v) {
            return v == null;
        }

        @Override
        public boolean containsErr(E e) {
            return false;
        }

        @Override
        public String toString() {
            return "Result::Ok[null]";
        }
    }

    private static final class Err<T, E> implements Result<T, E> {

        private final E err;

        Err(E err) {
            this.err = err;
        }

        @Override
        public void ifOk(Consumer<? super T> action) {
        }

        @Override
        public void ifError(Consumer<? super E> action) {
            action.accept(err);
        }

        @Override
        public T get() {
            return get("called `Result.get()` on an `Err` value: " + err);
        }

        @Override
        public T get(String msg) {
            throw new NoSuchElementException(msg);
        }

        @Override
        public T getOrElse(T another) {
            return another;
        }

        @Override
        public E getErr() {
            return err;
        }

        @Override
        public E getErr(String msg) {
            return err;
        }

        @Override
        public E getErrOrElse(E another) {
            return err;
        }

        @Override
        public Result<T, E> map(Function<? super T, ? extends T> fn) {
            return this;
        }

        @Override
        public Result<T, E> mapErr(Function<? super E, ? extends E> fn) {
            final E e = fn.apply(err);
            return e == null ? err() : err(e);
        }

        @Override
        public boolean isOk() {
            return false;
        }

        @Override
        public boolean isErr() {
            return true;
        }

        @Override
        public boolean contains(T v) {
            return false;
        }

        @Override
        public boolean containsErr(E e) {
            return ObjectUtils.safeEquals(err, e);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Err<?, ?> err1 = (Err<?, ?>) o;
            return Objects.equals(err, err1.err);
        }

        @Override
        public int hashCode() {
            return Objects.hash(err);
        }

        @Override
        public String toString() {
            return "Result::Err[" + err + "]";
        }
    }

    private static class NullErr<T, E> implements Result<T, E> {

        @Override
        public void ifOk(Consumer<? super T> action) {
        }

        @Override
        public void ifError(Consumer<? super E> action) {
            action.accept(null);
        }

        @Override
        public T get() {
            return get("called `Result.get()` on an `Err` value: null");
        }

        @Override
        public T get(String msg) {
            throw new NoSuchElementException(msg);
        }

        @Override
        public T getOrElse(T another) {
            return another;
        }

        @Override
        public E getErr() {
            return null;
        }

        @Override
        public E getErr(String msg) {
            return null;
        }

        @Override
        public E getErrOrElse(E another) {
            return null;
        }

        @Override
        public Result<T, E> map(Function<? super T, ? extends T> fn) {
            return this;
        }

        @Override
        public Result<T, E> mapErr(Function<? super E, ? extends E> fn) {
            final E e = fn.apply(null);
            return e == null ? err() : err(e);
        }

        @Override
        public boolean isOk() {
            return false;
        }

        @Override
        public boolean isErr() {
            return true;
        }

        @Override
        public boolean contains(T v) {
            return false;
        }

        @Override
        public boolean containsErr(E e) {
            return e == null;
        }

        @Override
        public String toString() {
            return "Result::Err[null]";
        }
    }

    private ResultImpl() {
    }

}
