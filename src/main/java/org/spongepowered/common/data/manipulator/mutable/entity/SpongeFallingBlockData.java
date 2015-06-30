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
package org.spongepowered.common.data.manipulator.mutable.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.spongepowered.api.data.DataQuery.of;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.manipulator.entity.FallingBlockData;
import org.spongepowered.common.data.manipulator.AbstractMutableData;

public class SpongeFallingBlockData extends AbstractMutableData<FallingBlockData> implements FallingBlockData {

    public static final DataQuery BLOCK_STATE = of("BlockState");
    public static final DataQuery DAMAGE_PER_BLOCK = of("DamagePerBlock");
    public static final DataQuery MAX_DAMAGE = of("MaxDamage");
    public static final DataQuery CAN_PLACE = of("CanPlace");
    public static final DataQuery CAN_DROP = of("CanDrop");
    private BlockState blockState;
    private double damagePerBlock;
    private double maxDamage;
    private boolean canPlace;
    private boolean canDrop;

    public SpongeFallingBlockData() {
        super(FallingBlockData.class);
        this.blockState = BlockTypes.STONE.getDefaultState();
    }

    @Override
    public double getFallDamagePerBlock() {
        return this.damagePerBlock;
    }

    @Override
    public FallingBlockData setFallDamagePerBlock(double damage) {
        checkArgument(damage >= 0);
        this.damagePerBlock = damage;
        return this;
    }

    @Override
    public double getMaxFallDamage() {
        return this.maxDamage;
    }

    @Override
    public FallingBlockData setMaxFallDamage(double damage) {
        checkArgument(damage >= 0);
        this.maxDamage = damage;
        return this;
    }

    @Override
    public BlockState getBlockState() {
        return this.blockState;
    }

    @Override
    public FallingBlockData setBlockState(BlockState blockState) {
        this.blockState = checkNotNull(blockState);
        return this;
    }

    @Override
    public boolean getCanPlaceAsBlock() {
        return this.canPlace;
    }

    @Override
    public FallingBlockData setCanPlaceAsBlock(boolean placeable) {
        this.canPlace = placeable;
        return this;
    }

    @Override
    public boolean getCanDropAsItem() {
        return this.canDrop;
    }

    @Override
    public FallingBlockData setCanDropAsItem(boolean droppable) {
        this.canDrop = droppable;
        return this;
    }

    @Override
    public FallingBlockData copy() {
        return new SpongeFallingBlockData()
                .setBlockState(this.getBlockState())
                .setCanDropAsItem(this.getCanDropAsItem())
                .setCanPlaceAsBlock(this.getCanPlaceAsBlock())
                .setFallDamagePerBlock(this.getFallDamagePerBlock())
                .setMaxFallDamage(this.getMaxFallDamage());
    }

    @Override
    public int compareTo(FallingBlockData o) {
        return (o.getBlockState().hashCode() - this.getBlockState().hashCode())
                - (int) Math.floor(o.getFallDamagePerBlock() - this.getFallDamagePerBlock())
                - (int) Math.floor(o.getMaxFallDamage() - this.getMaxFallDamage());
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer()
                .set(BLOCK_STATE, this.blockState)
                .set(DAMAGE_PER_BLOCK, this.damagePerBlock)
                .set(MAX_DAMAGE, this.maxDamage)
                .set(CAN_PLACE, this.canPlace)
                .set(CAN_DROP, this.canDrop);
    }
}
