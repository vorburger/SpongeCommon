package org.spongepowered.common.service.user;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.entity.player.User;
import org.spongepowered.api.service.user.UserStorage;
import org.spongepowered.common.entity.player.SpongeUser;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class SpongeUserStorage implements UserStorage {

    private static final Map<UUID, User> userCache = Maps.newHashMap();

    @Override
    public Optional<User> get(UUID uniqueId) {
        return Optional.fromNullable(UserDiscoverer.findByUuid(checkNotNull(uniqueId, "uniqueId")));
    }

    @Override
    public Optional<User> get(String lastKnownName) {
        checkNotNull(lastKnownName, "lastKnownName");
        checkArgument(lastKnownName.length() >= 3 && lastKnownName.length() <= 16, "Invalid username %s", lastKnownName);
        return Optional.fromNullable(UserDiscoverer.findByUsername(lastKnownName));
    }

    @Override
    public Optional<User> get(GameProfile profile) {
        return Optional.fromNullable(UserDiscoverer.findByUuid(checkNotNull(profile, "profile").getUniqueId()));
    }

    @Override
    public User getOrCreate(GameProfile profile) {
        Optional<User> user = get(profile);
        if (user.isPresent()) {
            return user.get();
        }
        return create((com.mojang.authlib.GameProfile) profile);
    }

    @Override
    public Collection<GameProfile> getAll() {
        return Collections.emptyList(); // TODO
    }

    @Override
    public boolean delete(GameProfile profile) {
        return false; // TODO
    }

    @Override
    public boolean delete(User user) {
        return false; // TODO
    }

    @Override
    public Collection<GameProfile> match(String lastKnownName) {
        return Collections.emptyList(); // TODO
    }

    /**
     * Create a user, or gets it from the user cache.
     *
     * @param profile User's profile
     * @return The user
     */
    public static User create(com.mojang.authlib.GameProfile profile) {
        User user = userCache.get(profile.getId());
        if (user == null) {
            user = (User) new SpongeUser(profile);
            userCache.put(profile.getId(), user);
        }
        return user;
    }
}
