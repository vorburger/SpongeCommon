package org.spongepowered.common.mixin.core.entity.player;

import com.google.common.base.Optional;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.entity.AchievementData;
import org.spongepowered.api.data.manipulator.entity.BanData;
import org.spongepowered.api.data.manipulator.entity.StatisticData;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.player.User;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.entity.player.SpongeUser;
import org.spongepowered.common.interfaces.IMixinSubject;

@Mixin(value = SpongeUser.class, remap = false)
public abstract class MixinSpongeUser implements User, IMixinSubject {

    @Shadow private com.mojang.authlib.GameProfile profile;

    @Override
    public GameProfile getProfile() {
        return (GameProfile) this.profile;
    }

    @Override
    public boolean isOnline() {
        return this.getPlayer().isPresent();
    }

    @Override
    public Optional<Player> getPlayer() {
        return Optional.fromNullable((Player) MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(this.profile.getId()));
    }

    @Override
    public AchievementData getAchievementData() {
        throw new UnsupportedOperationException(); // TODO Achievement Data
    }

    @Override
    public StatisticData getStatisticData() {
        throw new UnsupportedOperationException(); // TODO Statistic Data
    }

    @Override
    public BanData getBanData() {
        throw new UnsupportedOperationException(); // TODO Ban Data
    }

    @Override
    public boolean validateRawData(DataContainer container) {
        throw new UnsupportedOperationException(); // TODO Data API
    }

    @Override
    public void setRawData(DataContainer container) throws InvalidDataException {
        throw new UnsupportedOperationException(); // TODO Data API
    }

    @Override
    public String getSubjectCollectionIdentifier() {
        return PermissionService.SUBJECTS_USER;
    }

    @Override
    public Tristate permDefault(String permission) {
        return Tristate.FALSE;
    }

    @Override
    public String getIdentifier() {
        return this.profile.getId().toString();
    }
}
