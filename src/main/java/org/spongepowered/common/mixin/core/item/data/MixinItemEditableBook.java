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
package org.spongepowered.common.mixin.core.item.data;

import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.DisplayNameData;
import org.spongepowered.api.data.manipulator.item.AuthorData;
import org.spongepowered.api.data.manipulator.item.CloneableData;
import org.spongepowered.api.data.manipulator.item.PagedData;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(ItemEditableBook.class)
public class MixinItemEditableBook extends MixinItem {

    @Override
    public List<DataManipulator<?>> getManipulatorsFor(ItemStack itemStack) {
        final List<DataManipulator<?>> manipulators = super.getManipulatorsFor(itemStack);
        manipulators.add(getData(itemStack, AuthorData.class));
        manipulators.add(getData(itemStack, DisplayNameData.class));
        manipulators.add(getData(itemStack, PagedData.class));
        manipulators.add(getData(itemStack, CloneableData.class));
        return manipulators;
    }
}
