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
package org.spongepowered.common.data.manipulator.mutable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.spongepowered.api.data.DataQuery.of;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.manipulator.MobSpawnerData;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.util.weighted.WeightedCollection;
import org.spongepowered.api.util.weighted.WeightedEntity;
import org.spongepowered.common.data.manipulator.AbstractMutableData;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

public class SpongeMobSpawnerData extends AbstractMutableData<MobSpawnerData> implements MobSpawnerData {

    public static final DataQuery ENTITY_TYPE = of("EntityType");
    public static final DataQuery WEIGHT = of("Weight");
    public static final DataQuery DATA = of("Data");
    public static final DataQuery DELAY = of("Delay");
    public static final DataQuery MIN_DELAY = of("MinDelay");
    public static final DataQuery MAX_DELAY = of("MaxDelay");
    public static final DataQuery COUNT = of("Count");
    public static final DataQuery MAX_ENTITIES = of("MaxEntities");
    public static final DataQuery PLAYER_RANGE = of("PlayerRange");
    public static final DataQuery SPAWN_RANGE = of("SpawnRange");
    public static final DataQuery ENTITIES = of("Entities");
    private short delay;
    private short minDelay;
    private short maxDelay;
    private short count;
    private short maxEntities;
    private short range;
    private short spawnRange;
    private WeightedEntity nextSpawn;
    private WeightedCollection<WeightedEntity> entities = new WeightedCollection<WeightedEntity>();


    public SpongeMobSpawnerData() {
        super(MobSpawnerData.class);
    }

    @Override
    public short getRemainingDelay() {
        return this.delay;
    }

    @Override
    public MobSpawnerData setRemainingDelay(short delay) {
        checkArgument(delay >= 0);
        if (delay < this.minDelay) {
            this.delay = this.minDelay;
        } else if (delay > this.maxDelay) {
            this.delay = this.maxDelay;
        } else {
            this.delay = delay;
        }
        return this;
    }

    @Override
    public short getMinimumSpawnDelay() {
        return this.minDelay;
    }

    @Override
    public MobSpawnerData setMinimumSpawnDelay(short delay) {
        checkArgument(delay >= 0);
        if (delay > this.maxDelay) {
            this.minDelay = this.maxDelay;
        } else {
            this.minDelay = delay;
        }
        return this;
    }

    @Override
    public short getMaximumSpawnDelay() {
        return this.maxDelay;
    }

    @Override
    public MobSpawnerData setMaximumSpawnDelay(short delay) {
        checkArgument(delay >= 0);
        if (delay < this.minDelay) {
            this.maxDelay = this.minDelay;
        } else {
            this.maxDelay = delay;
        }
        return this;
    }

    @Override
    public short getSpawnCount() {
        return this.count;
    }

    @Override
    public MobSpawnerData setSpawnCount(short count) {
        checkArgument(count >= 0);
        this.count = count;
        return this;
    }

    @Override
    public short getMaximumNearbyEntities() {
        return this.maxEntities;
    }

    @Override
    public MobSpawnerData setMaximumNearbyEntities(short count) {
        checkArgument(count > 0);
        this.maxEntities = count;
        return this;
    }

    @Override
    public short getRequiredPlayerRange() {
        return this.range;
    }

    @Override
    public MobSpawnerData setRequiredPlayerRange(short range) {
        checkArgument(range > 0); // We can't have the range such that the player has to be in the mob spawner block
        this.range = range;
        return this;
    }

    @Override
    public short getSpawnRange() {
        return this.spawnRange;
    }

    @Override
    public MobSpawnerData setSpawnRange(short range) {
        checkArgument(range >= 1);
        this.spawnRange = range;
        return this;
    }

    @Override
    public MobSpawnerData setNextEntityToSpawn(EntityType type, @Nullable Collection<DataManipulator<?>> additionalProperties) {
        if (additionalProperties == null) {
            this.nextSpawn = new WeightedEntity(checkNotNull(type), 10);
        } else {
            this.nextSpawn = new WeightedEntity(checkNotNull(type), 10, additionalProperties);
        }
        return this;
    }

    @Override
    public MobSpawnerData setNextEntityToSpawn(WeightedEntity entity) {
        this.nextSpawn = checkNotNull(entity);
        return this;
    }

    @Override
    public WeightedCollection<WeightedEntity> getPossibleEntitiesToSpawn() {
        return this.entities;
    }

    @Override
    public MobSpawnerData copy() {
        final WeightedCollection<WeightedEntity> newEntities = new WeightedCollection<WeightedEntity>();
        for (WeightedEntity entity : this.entities) {
            newEntities.add(new WeightedEntity(entity.get(), entity.getWeight(), entity.getAdditionalProperties()));
        }
        final MobSpawnerData data = new SpongeMobSpawnerData()
                .setMinimumSpawnDelay(this.getMinimumSpawnDelay())
                .setMaximumSpawnDelay(this.getMaximumSpawnDelay())
                .setRemainingDelay(this.getRemainingDelay())
                .setSpawnCount(this.getSpawnCount())
                .setMaximumNearbyEntities(this.getMaximumNearbyEntities())
                .setRequiredPlayerRange(this.getRequiredPlayerRange())
                .setSpawnRange(this.getSpawnRange())
                .setNextEntityToSpawn(this.nextSpawn);
        data.getPossibleEntitiesToSpawn().addAll(newEntities);
        return data;
    }

    @Override
    public int compareTo(MobSpawnerData o) {
        return ComparisonChain.start().compare(o.getRemainingDelay(), this.delay)
                .compare(o.getMinimumSpawnDelay(), this.minDelay)
                .compare(o.getMaximumSpawnDelay(), this.maxDelay)
                .compare(o.getSpawnCount(), this.count)
                .compare(o.getRequiredPlayerRange(), this.range)
                .compare(o.getSpawnRange(), this.spawnRange)
                .compare(o.getMaximumNearbyEntities(), this.maxEntities)
                .result(); // todo compare entities listing
    }

    @Override
    public DataContainer toContainer() {
        List<DataContainer> entities = Lists.newArrayList();
        for (WeightedEntity entity : this.entities) {
            final DataContainer container = new MemoryDataContainer();
            container.set(ENTITY_TYPE, entity.get().getId())
                    .set(WEIGHT, entity.getWeight())
                    .set(DATA, entity.getAdditionalProperties());
            entities.add(container);
        }
        return new MemoryDataContainer()
                .set(DELAY, this.delay)
                .set(MIN_DELAY, this.minDelay)
                .set(MAX_DELAY, this.maxDelay)
                .set(COUNT, this.count)
                .set(MAX_ENTITIES, this.maxEntities)
                .set(PLAYER_RANGE, this.range)
                .set(SPAWN_RANGE, this.spawnRange)
                .set(ENTITIES, entities);
    }
}
