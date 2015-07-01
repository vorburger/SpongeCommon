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
package org.spongepowered.common.data.processor;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.spongepowered.api.data.DataTransactionBuilder.fail;
import static org.spongepowered.api.data.DataTransactionBuilder.successNoData;
import static org.spongepowered.common.item.ItemsHelper.getTagCompound;

import com.google.common.base.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IWorldNameable;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataPriority;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.mutable.DisplayNameData;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.text.Texts;
import org.spongepowered.common.data.DataProcessor;
import org.spongepowered.common.data.manipulator.mutable.SpongeDisplayNameData;
import org.spongepowered.common.text.SpongeTexts;

public class DisplayNameDataProcessor implements DataProcessor<DisplayNameData> {

    @Override
    public Optional<DisplayNameData> build(DataView container) throws InvalidDataException {
        return null;
    }

    @Override
    public DisplayNameData create() {
        return new SpongeDisplayNameData();
    }

    @SuppressWarnings("deprecation")
    @Override
    public Optional<DisplayNameData> createFrom(DataHolder dataHolder) {
        if (dataHolder instanceof Entity) {
            if (((Entity) dataHolder).hasCustomName()) {
                final SpongeDisplayNameData data = (SpongeDisplayNameData) create();
                data.setDisplayName(SpongeTexts.toText(((Entity) dataHolder).getDisplayName()));
                data.setCustomNameVisible(((Entity) dataHolder).getAlwaysRenderNameTag());
                return Optional.<DisplayNameData>of(data);
            } else {
                return Optional.of(create());
            }
        } else if (dataHolder instanceof ItemStack) {
            if (((ItemStack) dataHolder).getItem() == Items.written_book) {
                final NBTTagCompound mainCompound = ((ItemStack) dataHolder).getTagCompound();
                final String titleString = mainCompound.getString("title");
                final DisplayNameData data = create();
                data.setDisplayName(Texts.legacy().fromUnchecked(titleString));
                data.setCustomNameVisible(true);
                return Optional.of(data);
            }
            final NBTTagCompound mainCompound = ((ItemStack) dataHolder).getSubCompound("display", false);
            if (mainCompound != null && mainCompound.hasKey("Name", 8)) {
                final String displayString = mainCompound.getString("Name");
                final DisplayNameData data = new SpongeDisplayNameData();
                System.err.println("The retrieved displayname from an item stack was: " + displayString);
                data.setDisplayName(Texts.legacy().fromUnchecked(displayString));
                data.setCustomNameVisible(true);
                return Optional.of(data);
            } else {
                return Optional.of(create());
            }
        } else if (dataHolder instanceof IWorldNameable) {
            if (((IWorldNameable) dataHolder).hasCustomName()) {
                final DisplayNameData data = new SpongeDisplayNameData();
                final String customName = ((IWorldNameable) dataHolder).getCommandSenderName();
                data.setDisplayName(Texts.legacy().fromUnchecked(customName));
                data.setCustomNameVisible(true);
                return Optional.of(data);
            }
        }
        return Optional.absent();
    }

    @Override
    public boolean remove(DataHolder dataHolder) {
        if (dataHolder instanceof ItemStack) {
            if (((ItemStack) dataHolder).getItem() == Items.written_book) {
                // todo
            }
            ((ItemStack) dataHolder).clearCustomName();
            return true;
        } // todo
        return false;
    }

    @Override
    public Optional<DisplayNameData> fillData(DataHolder dataHolder, DisplayNameData manipulator) {
        return Optional.absent(); // todo
    }

    @SuppressWarnings("deprecation")
    @Override
    public DataTransactionResult setData(DataHolder dataHolder, DisplayNameData manipulator) {
        if (dataHolder instanceof ItemStack) {
            switch (checkNotNull(priority)) {
                case DATA_MANIPULATOR:
                    if (((ItemStack) dataHolder).getItem() == Items.written_book) {
                        getTagCompound((ItemStack) dataHolder).setString("title", Texts.legacy().to(manipulator.getDisplayName()));
                    } else {
                        ((ItemStack) dataHolder).setStackDisplayName(Texts.legacy().to(manipulator.getDisplayName()));
                    }
                    return successNoData();
                case DATA_HOLDER:
                case POST_MERGE:
                    // todo
                default:
                    return fail(manipulator);
            }

        } else if (dataHolder instanceof Entity) {
            ((Entity) dataHolder).setCustomNameTag(Texts.legacy().to(manipulator.getDisplayName()));
            ((Entity) dataHolder).setAlwaysRenderNameTag(manipulator.isCustomNameVisible());
        }
        return fail(manipulator);
    }

    @Override
    public Optional<DisplayNameData> getFrom(DataHolder dataHolder) {
        return Optional.absent();
    }
}
