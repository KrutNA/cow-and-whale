package com.krutna.animalguessing;

import java.util.Objects;

/**
 * Represents class which holds specified parameter. <br/>
 * Traits could be direct or opposite.
 * @param <T> type of parameter
 */
public class Trait<T> {
    private final T value;
    private final boolean isDirect;
    private final Trait<T> opposite;

    /**
     * Create direct Trait
     * @param value main trait parameter
     */
    public Trait(T value) {
        this.value = value;
        this.isDirect = true;
        opposite = new Trait<>(value, this);
    }

    private Trait(T value, Trait<T> opposite) {
        this.value = value;
        this.isDirect = false;
        this.opposite = opposite;
    }

    public T getValue() { return value; }
    public boolean isDirect() { return isDirect; }
    public Trait<T> getOpposite() { return opposite; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trait<?> trait = (Trait<?>) o;
        return isDirect == trait.isDirect && Objects.equals(value, trait.value);
    }

    @Override
    public int hashCode() { return Objects.hash(value, isDirect); }

    @Override
    public String toString() { return (isDirect ? "" : "!") + value.toString(); }
}
