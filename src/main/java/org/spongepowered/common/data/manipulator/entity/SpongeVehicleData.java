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
package org.spongepowered.common.data.manipulator.entity;

import static org.spongepowered.api.data.DataQuery.of;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.manipulator.entity.VehicleData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.common.data.manipulator.AbstractSingleValueData;

public class SpongeVehicleData extends AbstractSingleValueData<Entity, VehicleData> implements VehicleData {

    public static final DataQuery LEASH_HOLDER = of("LeashHolder");

    public SpongeVehicleData(Entity entity) {
        super(VehicleData.class, entity);
    }

    @Override
    public Entity getPassenger() {
        return this.getValue();
    }

    @Override
    public boolean setPassenger(Entity entity) {
        if (!entity.isLoaded()) {
            return false;
        } else {
            setValue(entity);
            return true;
        }
    }

    @Override
    public VehicleData copy() {
        return new SpongeVehicleData(this.getValue());
    }

    @Override
    public int compareTo(VehicleData o) {
        return o.getValue().getUniqueId().compareTo(this.getValue().getUniqueId());
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer().set(LEASH_HOLDER, this.getValue().getUniqueId());
    }
}
