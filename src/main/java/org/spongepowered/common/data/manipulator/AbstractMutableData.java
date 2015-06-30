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
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.BaseValue;

import java.util.Collection;

import javax.annotation.Nullable;

@SuppressWarnings("unchecked")
public abstract class AbstractMutableData<M extends DataManipulator<M, I>, I extends ImmutableDataManipulator<I, M>> implements DataManipulator<M, I> {

    private final Class<M> manipulatorClass;

    protected AbstractMutableData(Class<M> manipulatorClass) {
        this.manipulatorClass = checkNotNull(manipulatorClass);
    }

    @Override
    public Optional<M> fill(DataHolder dataHolder) {
        return Optional.of((M) this); // todo
    }

    @Override
    public Optional<M> fill(DataHolder dataHolder, Function<I, M> overlap) {
        return Optional.of((M) this); // todo
    }

    @Override
    public Optional<M> from(DataContainer container) {
        return Optional.absent(); // todo
    }

    @Override
    public <E> M set(Key<? extends BaseValue<E>> key, E value) {
        return (M) this; // todo
    }

    @Override
    public M set(BaseValue<?> value) {
        return (M) this; // todo
    }

    @Override
    public M set(BaseValue<?>... values) {
        return (M) this; // todo
    }

    @Override
    public M set(Collection<? extends BaseValue<?>> values) {
        return (M) this; // todo
    }

    @Override
    public <E> Optional<E> get(Key<? extends BaseValue<E>> key) {
        return Optional.absent(); // todo
    }

    @Nullable
    @Override
    public <E> E getOrNull(Key<? extends BaseValue<E>> key) {
        return null; // todo
    }

    @Override
    public <E> E getOrElse(Key<? extends BaseValue<E>> key, E defaultValue) {
        return null; // todo
    }

    @Override
    public <E, V extends BaseValue<E>> Optional<V> getValue(Key<V> key) {
        return Optional.absent(); // todo
    }

    @Override
    public boolean supports(Key<?> key) {
        return false; // todo
    }

    @Override
    public boolean supports(BaseValue<?> baseValue) {
        return false; // todo
    }

    @Override
    public M copy() {
        return (M) this; // todo
    }

    @Override
    public ImmutableSet<Key<?>> getKeys() {
        return ImmutableSet.of(); // todo
    }

    @Override
    public ImmutableSet<BaseValue<?>> getValues() {
        return ImmutableSet.of(); // todo
    }

    @Override
    public I asImmutable() {
        return null; // todo
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.manipulatorClass);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AbstractMutableData other = (AbstractMutableData) obj;
        return Objects.equal(this.manipulatorClass, other.manipulatorClass);
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer();
    }
}
