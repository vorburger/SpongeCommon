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
package org.spongepowered.common.data.processor.block;

import static org.spongepowered.api.data.DataTransactionBuilder.failResult;

import com.google.common.base.Optional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataTransactionBuilder;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableDirectionalData;
import org.spongepowered.api.data.manipulator.mutable.block.DirectionalData;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.util.Direction;
import org.spongepowered.common.data.BlockDataProcessor;
import org.spongepowered.common.data.DataProcessor;
import org.spongepowered.common.data.manipulator.immutable.block.ImmutableSpongeDirectionalData;
import org.spongepowered.common.data.manipulator.mutable.block.SpongeDirectionalData;
import org.spongepowered.common.interfaces.block.IMixinBlockDirectional;

public class DirectionalProcessor implements DataProcessor<DirectionalData, ImmutableDirectionalData>, BlockDataProcessor<DirectionalData> {

    @Override
    public Optional<DirectionalData> fillData(DataHolder dataHolder, DirectionalData manipulator) {
        return Optional.absent();
    }

    @Override
    public DataTransactionResult setData(DataHolder dataHolder, DirectionalData manipulator) {
        return DataTransactionBuilder.successNoData();
    }

    @Override
    public boolean remove(DataHolder dataHolder) {
        return false;
    }

    @Override
    public Optional<DirectionalData> build(DataView container) throws InvalidDataException {
        return Optional.absent();
    }

    @Override
    public DirectionalData create() {
        return new SpongeDirectionalData();
    }

    @Override
    public ImmutableDirectionalData createImmutable() {
        return new ImmutableSpongeDirectionalData(Direction.NONE);
    }

    @Override
    public Optional<DirectionalData> createFrom(DataHolder dataHolder) {
        return Optional.absent();
    }

    @Override
    public Optional<DirectionalData> fromBlockPos(final World world, final BlockPos blockPos) {
        IBlockState blockState = world.getBlockState(blockPos);
        if (blockState.getBlock() instanceof IMixinBlockDirectional) {
            return Optional.of(((IMixinBlockDirectional) blockState.getBlock()).getDirectionalData(blockState));
        }
        return Optional.absent();
    }

    @Override
    public DataTransactionResult setData(World world, BlockPos blockPos, DirectionalData manipulator) {
        IBlockState blockState = world.getBlockState(blockPos);
        if (blockState.getBlock() instanceof IMixinBlockDirectional) {
            return ((IMixinBlockDirectional) blockState.getBlock()).setDirectionalData(manipulator, world, blockPos);
        }
        return failResult(manipulator.direction().asImmutable());
    }

    @Override
    public Optional<BlockState> withData(IBlockState blockState, DirectionalData manipulator) {
        if (blockState.getBlock() instanceof IMixinBlockDirectional) {
            return Optional.of(((IMixinBlockDirectional) blockState.getBlock()).setDirectionalData(blockState, manipulator));
        }
        return Optional.absent();
    }

    @Override
    public boolean remove(World world, BlockPos blockPos) {
        IBlockState blockState = world.getBlockState(blockPos);
        if (blockState.getBlock() instanceof IMixinBlockDirectional) {
//            ((IMixinBlockDirectional) blockState.getBlock()).resetDirectionData(blockState);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BlockState> removeFrom(IBlockState blockState) {
        return Optional.absent();
    }

    @Override
    public Optional<DirectionalData> createFrom(IBlockState blockState) {
        if (blockState.getBlock() instanceof IMixinBlockDirectional) {
            ((IMixinBlockDirectional) blockState.getBlock()).getDirectionalData(blockState);
        }
        return Optional.absent();
    }

    @Override
    public Optional<DirectionalData> getFrom(DataHolder dataHolder) {
        return Optional.absent();
    }
}
