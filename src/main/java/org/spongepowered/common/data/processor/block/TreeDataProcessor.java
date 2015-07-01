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

import static com.google.common.base.Preconditions.checkNotNull;
import static org.spongepowered.api.data.DataTransactionBuilder.fail;
import static org.spongepowered.common.data.util.DataUtil.getData;

import com.google.common.base.Optional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataPriority;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.block.TreeData;
import org.spongepowered.api.data.type.TreeType;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.common.data.BlockDataProcessor;
import org.spongepowered.common.data.DataProcessor;
import org.spongepowered.common.data.manipulator.mutable.block.SpongeTreeData;
import org.spongepowered.common.interfaces.block.IMixinBlockTree;

public class TreeDataProcessor implements DataProcessor<TreeData>, BlockDataProcessor<TreeData> {

    @Override
    public Optional<TreeData> getFrom(DataHolder dataHolder) {
        return Optional.absent();
    }

    @Override
    public Optional<TreeData> fillData(DataHolder dataHolder, TreeData manipulator) {
        return Optional.absent();
    }

    @Override
    public DataTransactionResult setData(DataHolder dataHolder, TreeData manipulator) {
        return fail(manipulator);
    }

    @Override
    public boolean remove(DataHolder dataHolder) {
        return false;
    }

    @Override
    public Optional<TreeData> build(DataView container) throws InvalidDataException {
        final TreeType treeType = getData(container, SpongeTreeData.TREE_TYPE, TreeType.class);
        return Optional.of(create().setValue(treeType));
    }

    @Override
    public TreeData create() {
        return new SpongeTreeData();
    }

    @Override
    public Optional<TreeData> createFrom(DataHolder dataHolder) {
        return Optional.absent();
    }

    @Override
    public Optional<TreeData> fromBlockPos(World world, BlockPos blockPos) {
        final IBlockState blockState = checkNotNull(world).getBlockState(checkNotNull(blockPos));
        if (blockState.getBlock() instanceof IMixinBlockTree) {
            return Optional.of(((IMixinBlockTree) blockState.getBlock()).getTreeData(blockState));
        }
        return Optional.absent();
    }

    @Override
    public DataTransactionResult setData(World world, BlockPos blockPos, TreeData manipulator) {
        final IBlockState blockState = checkNotNull(world).getBlockState(checkNotNull(blockPos));
        if (blockState.getBlock() instanceof IMixinBlockTree) {
            return ((IMixinBlockTree) blockState.getBlock()).setTreeData(checkNotNull(manipulator), world, blockPos, checkNotNull(priority));
        }
        return fail(manipulator);
    }

    @Override
    public Optional<BlockState> withData(IBlockState blockState, TreeData manipulator) {
        return Optional.absent();
    }

    @Override
    public boolean remove(World world, BlockPos blockPos) {
        final IBlockState blockState = checkNotNull(world).getBlockState(checkNotNull(blockPos));
        if (blockState.getBlock() instanceof IMixinBlockTree) {
            return world.setBlockState(blockPos, (IBlockState) ((IMixinBlockTree) blockState.getBlock()).resetTreeData(((BlockState) blockState)));
        }
        return false;
    }

    @Override
    public Optional<BlockState> removeFrom(IBlockState blockState) {
        return Optional.absent();
    }

    @Override
    public Optional<TreeData> createFrom(IBlockState blockState) {
        if (checkNotNull(blockState).getBlock() instanceof IMixinBlockTree) {
            return Optional.of(((IMixinBlockTree) blockState.getBlock()).getTreeData(blockState));
        }
        return Optional.absent();
    }
}
