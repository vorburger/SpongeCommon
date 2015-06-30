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
package org.spongepowered.common.data.processor.item;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.spongepowered.api.data.DataTransactionBuilder.fail;
import static org.spongepowered.api.data.DataTransactionBuilder.successNoData;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataPriority;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.item.LoreData;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.common.data.DataProcessor;
import org.spongepowered.common.data.manipulator.mutable.item.SpongeLoreData;
import org.spongepowered.common.interfaces.text.IMixinText;

import java.util.List;
import java.util.Locale;

public class LoreProcessor implements DataProcessor<LoreData> {

    @SuppressWarnings("deprecation")
    @Override
    public Optional<LoreData> fillData(DataHolder dataHolder, LoreData manipulator, DataPriority priority) {
        checkNotNull(dataHolder);
        checkNotNull(manipulator);
        if (dataHolder instanceof ItemStack) {
            if (!((ItemStack) dataHolder).hasTagCompound()) {
                return Optional.of(manipulator);
            }
            switch (checkNotNull(priority)) {
                case DATA_HOLDER: {
                    final NBTTagCompound subCompound = ((ItemStack) dataHolder).getSubCompound("display", false);
                    if (subCompound == null) {
                        return Optional.of(manipulator);
                    }
                    if (!subCompound.hasKey("Lore", 9)) {
                        return Optional.of(manipulator);
                    }
                    final List<Text> lore = Lists.newArrayList();
                    final NBTTagList list = subCompound.getTagList("Lore", 8);
                    for (int i = 0; i < list.tagCount(); i++) {
                        lore.add(Texts.legacy().fromUnchecked(list.getStringTagAt(i)));
                    }
                    return Optional.of(manipulator.set(lore));
                }
                case POST_MERGE: {
                    final NBTTagCompound subCompound = ((ItemStack) dataHolder).getSubCompound("display", false);
                    if (subCompound == null) {
                        return Optional.of(manipulator);
                    }
                    if (!subCompound.hasKey("Lore", 9)) {
                        return Optional.of(manipulator);
                    }
                    final NBTTagList list = subCompound.getTagList("Lore", 8);
                    for (int i = 0; i < list.tagCount(); i++) {
                        manipulator.add(Texts.legacy().fromUnchecked(list.getStringTagAt(i)));
                    }
                    return Optional.of(manipulator);
                }
                case PRE_MERGE: {
                    final NBTTagCompound subCompound = ((ItemStack) dataHolder).getSubCompound("display", false);
                    if (subCompound == null) {
                        return Optional.of(manipulator);
                    }
                    if (!subCompound.hasKey("Lore", 9)) {
                        return Optional.of(manipulator);
                    }
                    final List<Text> lore = Lists.newArrayList();
                    final NBTTagList list = subCompound.getTagList("Lore", 8);
                    for (int i = 0; i < list.tagCount(); i++) {
                        lore.add(Texts.legacy().fromUnchecked(list.getStringTagAt(i)));
                    }
                    lore.addAll(manipulator.getAll());
                    return Optional.of(manipulator.set(lore));
                }
                default:
                    return Optional.of(manipulator);

            }

        }
        return Optional.absent();
    }

    @Override
    public DataTransactionResult setData(DataHolder dataHolder, LoreData manipulator, DataPriority priority) {
        if (dataHolder instanceof ItemStack) {
            final NBTTagList loreList = new NBTTagList();
            for (Text text : manipulator.getAll()) {
                loreList.appendTag(new NBTTagString(((IMixinText) text).toLegacy('\247', Locale.ENGLISH)));
            }
            ((ItemStack) dataHolder).getSubCompound("display", true).setTag("Lore", loreList);
            return successNoData();
        }
        return fail(manipulator);
    }

    @Override
    public boolean remove(DataHolder dataHolder) {
        return false;
    }

    @Override
    public Optional<LoreData> build(DataView container) throws InvalidDataException {
        return Optional.absent();
    }

    @Override
    public LoreData create() {
        return new SpongeLoreData();
    }

    @Override
    public Optional<LoreData> createFrom(DataHolder dataHolder) {
        return Optional.absent();
    }

    @Override
    public Optional<LoreData> getFrom(DataHolder dataHolder) {
        return Optional.absent();
    }
}
