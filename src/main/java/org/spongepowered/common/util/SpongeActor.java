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
package org.spongepowered.common.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldServer;
import org.spongepowered.api.world.World;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Based on Forge's FakePlayer. <p>Call {@link #getInstance} to obtain the
 * player in the given world. Must call {@link #reset} after using.</p>
 */
public class SpongeActor extends EntityPlayerMP {

    private static final Map<UUID, SpongeActor> instances = Maps.newHashMap();

    public static SpongeActor getInstance(WorldServer world) {
        SpongeActor instance = instances.get(((World) world).getUniqueId());
        if (instance == null) {
            instance = new SpongeActor(world);
        }
        return instance.in(world);
    }

    private static final GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes("SpongeActor".getBytes()), "SpongeActor");
    private static final NetworkManager fakeNetworkManager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);

    private SpongeActor(WorldServer world) {
        super(MinecraftServer.getServer(), world, profile, new ItemInWorldManager(world));
        this.playerNetServerHandler = new NetHandlerPlayServer(this.mcServer, fakeNetworkManager, this) {

            @Override
            public void update() {
            }

            @Override
            public void kickPlayerFromServer(String reason) {
            }

            @Override
            public void setPlayerLocation(double x, double y, double z, float yaw, float pitch, @SuppressWarnings("rawtypes") Set relativeSet) {
            }

            @Override
            public void onDisconnect(IChatComponent reason) {
            }

            @Override
            public void sendPacket(Packet packetIn) {
            }
        };
        this.reset();
        instances.put(((World) world).getUniqueId(), this);
    }

    @Override
    public boolean canCommandSenderUseCommand(int i, String s) {
        return false;
    }

    @Override
    public void addChatComponentMessage(IChatComponent chatmessagecomponent) {
    }

    @Override
    public void addStat(StatBase par1StatBase, int par2) {
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return true;
    }

    @Override
    public boolean canAttackPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void onDeath(DamageSource source) {
        return;
    }

    @Override
    public void onUpdate() {
        return;
    }

    @Override
    public void travelToDimension(int dim) {
        return;
    }

    @Override
    public void handleClientSettings(C15PacketClientSettings pkt) {
        return;
    }

    /**
     * Reset the actor. MUST call this after finishing all actions to avoid
     * memory leaks.
     */
    public void reset() {
        this.inventory.clear();
        this.onGround = true;
        this.in(null).at(0, 0, 0);
    }

    /**
     * Places this actor in the given world.
     *
     * @param world The world
     * @return This actor
     */
    public SpongeActor in(WorldServer world) {
        this.setWorld(world);
        this.theItemInWorldManager.setWorld(world);
        return this;
    }

    /**
     * Positions this actor at the given location.
     *
     * @param x Position X
     * @param y Position Y
     * @param z Position Z
     * @return This actor
     */
    public SpongeActor at(int x, int y, int z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        return this;
    }

    /**
     * Hold the given item stack in the actor's hand.
     *
     * @param itemStack The item stack
     */
    public void holdStack(ItemStack itemStack) {
        this.inventory.setItemStack(itemStack);
        this.inventory.mainInventory[this.inventory.currentItem] = itemStack;
    }

}
