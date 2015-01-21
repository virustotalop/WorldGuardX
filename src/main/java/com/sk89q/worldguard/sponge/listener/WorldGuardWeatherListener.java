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

package com.sk89q.worldguard.sponge.listener;

import com.google.common.base.Predicate;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.sponge.WorldConfiguration;
import com.sk89q.worldguard.sponge.WorldGuardPlugin;
import com.sk89q.worldguard.sponge.internal.TargetMatcherSet;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.weather.LightningStrikeEvent;
import org.spongepowered.api.event.weather.WeatherChangeEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.weather.Weathers;

public class WorldGuardWeatherListener extends AbstractListener {

    /**
     * Construct the object;
     *
     * @param plugin The plugin instance
     */
    public WorldGuardWeatherListener(WorldGuardPlugin plugin) {
        super(plugin);
    }

    @Listener
    public void onWeatherChange(WeatherChangeEvent event) {
        WorldConfiguration wcfg = getWorldConfig((World) event.getWeatherUniverse());

        if (event.getResultingWeather().equals(Weathers.RAIN)) {
            if (wcfg.disableWeather) {
                event.setResultingWeather(event.getInitialWeather());
            }
        } else {
            if (!wcfg.disableWeather && wcfg.alwaysRaining) {
                event.setResultingWeather(event.getInitialWeather());
            }
        }
    }

    @Listener
    public void onLightningStrike(LightningStrikeEvent event) {
        WorldConfiguration wcfg = getWorldConfig((World) event.getWeatherUniverse());

        final TargetMatcherSet matcherSet = wcfg.disallowedLightningBlocks;
        event.filterLocations(new Predicate<Location>() {
            @Override
            public boolean apply(Location location) {
                return matcherSet.test(location.getBlockType());
            }
        });

        Location loc = event.getEntity().getLocation();
        if (wcfg.useRegions) {
            ApplicableRegionSet set = getPlugin().getRegionContainer().createQuery().getApplicableRegions(loc);

            if (!set.testState(null, DefaultFlag.LIGHTNING)) {
                //event.setCancelled(true);
            }
        }
    }
}
