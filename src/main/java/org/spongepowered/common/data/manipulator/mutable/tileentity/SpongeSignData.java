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

import static org.spongepowered.api.data.DataQuery.of;

import com.google.common.collect.Lists;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.manipulator.tileentity.SignData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.common.data.manipulator.AbstractMutableData;

import java.util.List;

@NonnullByDefault
public class SpongeSignData extends AbstractMutableData<SignData> implements SignData {
    public static final DataQuery LINES = new DataQuery("Lines");
    private final List<Text> lines;

    public SpongeSignData() {
        this(Lists.newArrayList(Texts.of(), Texts.of(), Texts.of(), Texts.of()));
    }

    public SpongeSignData(List<Text> lines) {
        super(SignData.class);
        this.lines = lines;
    }

    @Override
    public List<Text> getLines() {
        return this.lines;
    }

    @Override
    public Text getLine(int index) throws IndexOutOfBoundsException {
        return this.lines.get(index);
    }

    @Override
    public SignData setLine(int index, Text text) throws IndexOutOfBoundsException {
        this.lines.set(index, text);
        return this;
    }

    @Override
    public SignData reset() {
        for (int i = 0; i < this.lines.size(); i++) {
            this.lines.set(i, Texts.of());
        }
        return this;
    }

    @Override
    public SignData copy() {
        return new SpongeSignData(Lists.newArrayList(this.lines));
    }

    @Override
    public int compareTo(SignData o) {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        List<String> jsonLines = Lists.newArrayListWithExpectedSize(4);
        for (Text line : this.lines) {
            jsonLines.add(Texts.json().to(line));
        }
        return new MemoryDataContainer().set(LINES, jsonLines);
    }
}
