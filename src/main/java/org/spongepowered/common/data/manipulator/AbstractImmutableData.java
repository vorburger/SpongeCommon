/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.data.manipulator;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.BaseValue;

import javax.annotation.Nullable;

public abstract class AbstractImmutableData<I extends ImmutableDataManipulator<I, M>, M extends DataManipulator<M, I>> implements ImmutableDataManipulator<I, M> {

    private final Class<I> immutableClass;

    protected AbstractImmutableData(Class<I> immutableClass) {
        this.immutableClass = checkNotNull(immutableClass);
    }

    @Override
    public Optional<I> fill(DataHolder dataHolder) {
        return null;
    }

    @Override
    public Optional<I> fill(DataHolder dataHolder, Function<I, M> overlap) {
        return null;
    }

    @Override
    public Optional<I> from(DataContainer container) {
        return null;
    }

    @Override
    public <E> Optional<I> with(Key<? extends BaseValue<E>> key, E value) {
        return null;
    }

    @Override
    public Optional<I> with(BaseValue<?> value) {
        return null;
    }

    @Override
    public <E> Optional<E> get(Key<? extends BaseValue<E>> key) {
        return null;
    }

    @Nullable
    @Override
    public <E> E getOrNull(Key<? extends BaseValue<E>> key) {
        return null;
    }

    @Override
    public <E> E getOrElse(Key<? extends BaseValue<E>> key, E defaultValue) {
        return null;
    }

    @Override
    public <E, V extends BaseValue<E>> Optional<V> getValue(Key<V> key) {
        return null;
    }

    @Override
    public boolean supports(Key<?> key) {
        return false;
    }

    @Override
    public boolean supports(BaseValue<?> baseValue) {
        return false;
    }

    @Override
    public I copy() {
        return null;
    }

    @Override
    public ImmutableSet<Key<?>> getKeys() {
        return null;
    }

    @Override
    public ImmutableSet<BaseValue<?>> getValues() {
        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.immutableClass);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AbstractImmutableData other = (AbstractImmutableData) obj;
        return Objects.equal(this.immutableClass, other.immutableClass);
    }

    @Override
    public int compareTo(I o) {
        return 0;
    }
}
