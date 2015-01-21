/*
 * WorldGuard, a suite of tools for Minecraft
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldGuard team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldguard.session.handler;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.session.Session;
import org.spongepowered.api.data.manipulator.mutable.entity.HealthData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;

public class HealFlag extends Handler {

    private long lastHeal = 0;

    public HealFlag(Session session) {
        super(session);
    }

    @Override
    public void tick(Player player, ApplicableRegionSet set) {
        LocalPlayer localPlayer = getPlugin().wrapPlayer(player);

        if (!getSession().isInvincible(player) && player.getGameModeData().type().get().equals(GameModes.SURVIVAL)) {
            if (player.getHealthData().health().get() <= 0) {
                return;
            }

            long now = System.currentTimeMillis();

            Integer healAmount = set.queryValue(localPlayer, DefaultFlag.HEAL_AMOUNT);
            Integer healDelay = set.queryValue(localPlayer, DefaultFlag.HEAL_DELAY);
            Double minHealth = set.queryValue(localPlayer, DefaultFlag.MIN_HEAL);
            Double maxHealth = set.queryValue(localPlayer, DefaultFlag.MAX_HEAL);

            if (healAmount == null || healDelay == null || healAmount == 0 || healDelay < 0) {
                return;
            }

            HealthData data = player.getHealthData();
            if (minHealth == null) {
                minHealth = data.maxHealth().getMinValue();
            }

            if (maxHealth == null) {
                maxHealth = data.maxHealth().getMaxValue();
            }

            if (data.health().get() >= maxHealth && healAmount > 0) {
                return;
            }

            if (healDelay <= 0) {
                player.offer(data.health().set(healAmount > 0 ? maxHealth : minHealth)); // this will insta-kill if the flag is unset
                lastHeal = now;
            } else if (now - lastHeal > healDelay * 1000) {
                // clamp health between minimum and maximum
                player.offer(data.health().set(Math.min(maxHealth, Math.max(minHealth, data.health().get() + healAmount))));
                lastHeal = now;
            }
        }
    }

}
