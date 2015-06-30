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
package org.spongepowered.common.data.processor.entity;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.spongepowered.api.data.DataTransactionBuilder.fail;
import static org.spongepowered.api.data.DataTransactionBuilder.successNoData;

import com.google.common.base.Optional;
import net.minecraft.entity.passive.EntityAnimal;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataPriority;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.entity.BreedableData;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.common.data.DataProcessor;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongeBreedableData;
import org.spongepowered.common.data.util.DataUtil;

public class BreedableDataProcessor implements DataProcessor<BreedableData> {

    @Override
    public Optional<BreedableData> getFrom(DataHolder dataHolder) {
        if (!(dataHolder instanceof EntityAnimal)) {
            return Optional.absent();
        }
        return ((EntityAnimal) dataHolder).isInLove() ? Optional.of(create()) : Optional.<BreedableData>absent();
    }

    @Override
    public Optional<BreedableData> fillData(DataHolder dataHolder, BreedableData manipulator, DataPriority priority) {
        if (!(dataHolder instanceof EntityAnimal)) {
            return Optional.absent();
        }
        return Optional.of(checkNotNull(manipulator));
    }

    @Override
    public DataTransactionResult setData(DataHolder dataHolder, BreedableData manipulator, DataPriority priority) {
        if (!(dataHolder instanceof EntityAnimal)) {
            return fail(manipulator);
        }
        switch (checkNotNull(priority)) {
            case DATA_MANIPULATOR:
            case POST_MERGE:
                final boolean inlove = ((EntityAnimal) dataHolder).isInLove();
                ((EntityAnimal) dataHolder).setInLove(null);
                return successNoData();
            default:
                return successNoData();
        }
    }

    @Override
    public boolean remove(DataHolder dataHolder) {
        if (!(dataHolder instanceof EntityAnimal)) {
            return false;
        }
        ((EntityAnimal) dataHolder).resetInLove();
        return true;
    }

    @Override
    public Optional<BreedableData> build(DataView container) throws InvalidDataException {
        final boolean breeding = DataUtil.getData(container, SpongeBreedableData.BREEDABLE, Boolean.TYPE);
        return breeding ? Optional.of(create()) : Optional.<BreedableData>absent();
    }

    @Override
    public BreedableData create() {
        return new SpongeBreedableData();
    }

    @Override
    public Optional<BreedableData> createFrom(DataHolder dataHolder) {
        if (!(dataHolder instanceof EntityAnimal)) {
            return Optional.absent();
        }
        return ((EntityAnimal) dataHolder).isInLove() ? Optional.of(create()) : Optional.<BreedableData>absent();
    }
}
