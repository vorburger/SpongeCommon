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
package org.spongepowered.common.data.manipulator.mutable.tileentity;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.manipulator.tileentity.BeaconData;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.common.data.manipulator.AbstractMutableData;

import javax.annotation.Nullable;

public class SpongeBeaconData extends AbstractMutableData<BeaconData> implements BeaconData {

    @Nullable
    private PotionEffectType primary;

    @Nullable
    private PotionEffectType secondary;

    public SpongeBeaconData() {
        super(BeaconData.class);
    }

    @Override
    public Optional<PotionEffectType> getPrimaryEffect() {
        return Optional.fromNullable(this.primary);
    }

    @Override
    public BeaconData setPrimaryEffect(PotionEffectType effect) {
        this.primary = checkNotNull(effect);
        return this;
    }

    @Override
    public Optional<PotionEffectType> getSecondaryEffect() {
        return Optional.fromNullable(this.secondary);
    }

    @Override
    public BeaconData setSecondaryEffect(PotionEffectType effect) {
        this.secondary = checkNotNull(effect);
        return this;
    }

    @Override
    public BeaconData clearEffects() {
        this.primary = null;
        this.secondary = null;
        return this;
    }

    @Override
    public BeaconData copy() {
        return new SpongeBeaconData().setPrimaryEffect(this.primary).setSecondaryEffect(this.secondary);
    }

    @Override
    public int compareTo(BeaconData o) {
        return o.getPrimaryEffect().isPresent() ? this.getPrimaryEffect().isPresent()
                ? o.getPrimaryEffect().get().getId().compareTo(this.getPrimaryEffect().get().getId())
                : -1 : this.getPrimaryEffect().isPresent() ? 1
                : o.getSecondaryEffect().isPresent() ? this.getSecondaryEffect().isPresent()
                ? o.getSecondaryEffect().get().getId().compareTo(this.getSecondaryEffect().get().getId())
                : -1 : this.getSecondaryEffect().isPresent() ? 1 : 0;
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer();
    }
}
