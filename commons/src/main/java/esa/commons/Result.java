package esa.commons;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A type that represents either success ({@code [Ok]}) or failure ({@code [Err]}).
 *
 * @param <T> ok
 * @param <E> error
 */
public interface Result<T, E> {

    /**
     * success with {@code null} value
     */
    static <T, E> Result<T, E> ok() {
        return ResultImpl.ok();
    }

    /**
     * err with {@code null} value
     */
    static <T, E> Result<T, E> err() {
        return ResultImpl.err();
    }

    /**
     * success value
     */
    static <T, E> Result<T, E> ok(T ok) {
        return ok == null ? ResultImpl.ok() : ResultImpl.ok(ok);
    }

    /**
     * error value
     */
    static <T, E> Result<T, E> err(E err) {
        return err == null ? ResultImpl.err() : ResultImpl.err(err);
    }

    /**
     * If success ({@code [Ok]}), invoke the specified consumer with the ok value, otherwise do nothing.
     *
     * @param op block to be executed
     */
    void ifOk(Consumer<? super T> op);

    /**
     * If failure ({@code [Err]}), invoke the specified consumer with the err value, otherwise do nothing.
     *
     * @param op block to be executed
     */
    void ifError(Consumer<? super E> op);

    /**
     * Returns the {@code [Ok]} value.
     *
     * @return {@code [Ok]} value
     * @throws java.util.NoSuchElementException if the value is an {@code [Err]}, with a message provided by the
     *                                          {@code [Err]}'s value.
     */
    T get();

    /**
     * Returns the {@code [Ok]} value.
     *
     * @return {@code [Ok]} value
     * @throws java.util.NoSuchElementException if the value is an {@code [Err]}, with a message provided by {@code
     *                                          msg}.
     */
    T get(String msg);

    /**
     * Returns the {@code [Ok]} value or another value provided by {@code another}.
     *
     * @return {@code [Ok]} value or provided value.
     */
    T getOrElse(T another);

    /**
     * Returns the {@code [Err]} value.
     *
     * @return {@code [Err]} value
     * @throws java.util.NoSuchElementException if the value is an {@code [Ok]}, with a message provided by the
     *                                          {@code [Ok]}'s value.
     */
    E getErr();

    /**
     * Returns the {@code [Err]} value.
     *
     * @return {@code [Err]} value
     * @throws java.util.NoSuchElementException if the value is an {@code [Ok]}, with a message provided by {@code msg}.
     */
    E getErr(String msg);

    /**
     * Returns the {@code [Err]} value or another value provided by {@code another}.
     *
     * @return {@code [Err]} value or provided value.
     */
    E getErrOrElse(E another);

    /**
     * If a value is {@code [Ok]}, apply the provided mapping function to it.
     *
     * @param fn function to apply
     * @return result
     */
    Result<T, E> map(Function<? super T, ? extends T> fn);

    /**
     * If a value is {@code [Err]}, apply the provided mapping function to it.
     *
     * @param fn function to apply
     * @return result
     */
    Result<T, E> mapErr(Function<? super E, ? extends E> fn);

    /**
     * Returns {@code true} if the result is {@code [Ok]}.
     */
    boolean isOk();

    /**
     * Returns {@code true} if the result is {@code [Err]}.
     */
    boolean isErr();

    /**
     * Returns {@code true} if the result is an {@code [Ok]} value containing the given value.
     */
    boolean contains(T v);

    /**
     * Returns {@code true} if the result is an {@code [Err]} value containing the given value.
     */
    boolean containsErr(E e);
}
