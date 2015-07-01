package org.spongepowered.common.service.user;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.BanEntry;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.UserListBans;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.server.management.UserListWhitelist;
import net.minecraft.server.management.UserListWhitelistEntry;
import net.minecraft.world.storage.SaveHandler;
import org.spongepowered.api.entity.player.User;
import org.spongepowered.common.entity.player.SpongeUser;
import org.spongepowered.common.util.SpongeHooks;
import org.spongepowered.common.world.DimensionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.UUID;

/*
 * Notes:
 * User data can be stored in various places
 * The ban list (banned-players.json)
 * The whitelist (whitelist.json)
 * world player data (world/playerdata/{uuid}.dat)
 */
class UserDiscoverer {

    public static User findByUsername(String username) {
        PlayerProfileCache cache = MinecraftServer.getServer().getPlayerProfileCache();
        HashSet<String> names = Sets.newHashSet(cache.getUsernames());
        if (names.contains(username.toLowerCase(Locale.ROOT))) {
            GameProfile profile = cache.getGameProfileForUsername(username);
            if (profile != null) {
                return findByUuid(profile.getId());
            }
        }
        return null;
    }

    public static User findByUuid(UUID uniqueId) {
        User user = getOnlinePlayer(uniqueId);
        if (user != null) {
            return user;
        }
        user = getStoredPlayerData(uniqueId);
        if (user != null) {
            return user;
        }
        user = getWhitelistEntry(uniqueId);
        if (user != null) {
            return user;
        }
        user = getBanlistEntry(uniqueId);
        return user;
    }

    private static User getOnlinePlayer(UUID uniqueId) {
        return (User) MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(uniqueId);
    }

    private static User getStoredPlayerData(UUID uniqueId) {
        SaveHandler saveHandler = (SaveHandler) DimensionManager.getWorldFromDimId(0).getSaveHandler();
        String[] uuids = saveHandler.getAvailablePlayerDat();
        for (String playerUuid : uuids) {
            if (uniqueId.toString().equals(playerUuid)) {
                GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getProfileByUUID(uniqueId);
                if (profile != null) {
                    return loadFromNbtFile(profile, new File(saveHandler.playersDirectory, playerUuid + ".dat"));
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private static User getWhitelistEntry(UUID uniqueId) {
        UserListWhitelist whiteList = MinecraftServer.getServer().getConfigurationManager().getWhitelistedPlayers();
        UserListWhitelistEntry whiteListData = (UserListWhitelistEntry) whiteList.getEntry(new GameProfile(uniqueId, ""));
        if (whiteListData != null) {
            return SpongeUserStorage.create((GameProfile) whiteListData.value);
        }
        return null;
    }

    private static User getBanlistEntry(UUID uniqueId) {
        UserListBans banList = MinecraftServer.getServer().getConfigurationManager().getBannedPlayers();
        BanEntry banData = (BanEntry) banList.getEntry(new GameProfile(uniqueId, ""));
        if (banData instanceof UserListBansEntry) {
            return SpongeUserStorage.create((GameProfile) banData.value);
        }
        return null;
    }

    private static User loadFromNbtFile(GameProfile profile, File nbtFile) {
        User user = SpongeUserStorage.create(profile);
        try {
            ((SpongeUser) user).readFromNbt(CompressedStreamTools.readCompressed(new FileInputStream(nbtFile)));
        } catch (IOException e) {
            SpongeHooks.logWarning("Corrupt user file {}. {}", nbtFile, e);
        }
        return user;
    }

}
