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
package org.spongepowered.common.data.manipulator.mutable.block;

import static org.spongepowered.api.data.DataQuery.of;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableDirectionalData;
import org.spongepowered.api.data.manipulator.mutable.block.DirectionalData;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.Direction;
import org.spongepowered.common.data.manipulator.AbstractMutableData;
import org.spongepowered.common.data.value.mutable.SpongeValue;

public class SpongeDirectionalData extends AbstractMutableData<DirectionalData, ImmutableDirectionalData> implements DirectionalData {

    public static final DataQuery DIRECTION = of("Direction");

    private Direction direction = Direction.NONE;

    public SpongeDirectionalData() {
        super(DirectionalData.class);
    }

    @Override
    public DirectionalData copy() {
        return new SpongeDirectionalData().setDirection(getDirection());
    }

    @Override
    public int compareTo(DirectionalData o) {
        return o.direction().get().compareTo(this.direction);
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer().set(DIRECTION, this.direction.name());
    }

    @Override
    public Value<Direction> direction() {
        return new SpongeValue<Direction>(Keys.DIRECTION, Direction.NONE, this.direction);
    }

    public Direction getDirection() {
        return direction;
    }

    public SpongeDirectionalData setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }
}
