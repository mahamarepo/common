package com.mahama.common.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Builder<T> {
    private final Supplier<T> instantiate;
    private final List<Consumer<T>> modifiers = new ArrayList<>();

    public Builder(Supplier<T> _instantiate) {
        this.instantiate = _instantiate;
    }

    public static <T> Builder<T> of(Supplier<T> _instantiate) {
        return new Builder<>(_instantiate);
    }

    public <P1> Builder<T> with(Consumer1<T, P1> consumer, P1 p1) {
        Consumer<T> c = instance -> consumer.accept(instance, p1);
        modifiers.add(c);
        return this;
    }

    public T build() {
        T value = instantiate.get();
        modifiers.forEach(modifier -> modifier.accept(value));
        modifiers.clear();
        return value;
    }

    @FunctionalInterface
    public interface Consumer1<T, P1> {
        void accept(T t, P1 p1);
    }
}