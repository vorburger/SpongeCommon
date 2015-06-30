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
package org.spongepowered.common.data.manipulator.immutable.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableTradeOfferData;
import org.spongepowered.api.data.manipulator.mutable.entity.TradeOfferData;
import org.spongepowered.api.data.value.immutable.ImmutableListValue;
import org.spongepowered.api.item.merchant.TradeOffer;
import org.spongepowered.common.data.manipulator.AbstractImmutableData;

import java.util.List;

public class SpongeImmutableTradeOfferData extends AbstractImmutableData<ImmutableTradeOfferData, TradeOfferData> implements ImmutableTradeOfferData {

    private final ImmutableList<TradeOffer> offers;

    public SpongeImmutableTradeOfferData(List<TradeOffer> offers) {
        super(ImmutableTradeOfferData.class);
        this.offers = ImmutableList.copyOf(checkNotNull(offers));
    }

    @Override
    public ImmutableListValue<TradeOffer> tradeOffers() {
        return null;
    }

    @Override
    public TradeOfferData asMutable() {
        return null;
    }

    @Override
    public DataContainer toContainer() {
        return null;
    }
}
