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

package com.sk89q.worldguard.sponge.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.DyeableData;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.potion.PotionEffect;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.potion.PotionEffectTypes;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Material utility class.
 */
public final class Materials {

    private static final int MODIFIED_ON_RIGHT = 1;
    private static final int MODIFIED_ON_LEFT = 2;
    private static final int MODIFIES_BLOCKS = 4;

    private static final BiMap<EntityType, ItemType> ENTITY_ITEMS = HashBiMap.create();
    private static final Map<BlockType, Integer> BLOCK_MATERIAL_FLAGS = new HashMap<BlockType, Integer>();
    private static final Map<ItemType, Integer> ITEM_MATERIAL_FLAGS = new HashMap<ItemType, Integer>();
    private static final Set<PotionEffectType> DAMAGE_EFFECTS = new HashSet<PotionEffectType>();

    static {
        ENTITY_ITEMS.put(EntityTypes.PAINTING, ItemTypes.PAINTING);
        ENTITY_ITEMS.put(EntityTypes.ARROW, ItemTypes.ARROW);
        ENTITY_ITEMS.put(EntityTypes.SNOWBALL, ItemTypes.SNOWBALL);
        ENTITY_ITEMS.put(EntityTypes.FIREBALL, ItemTypes.FIRE_CHARGE);
        ENTITY_ITEMS.put(EntityTypes.SMALL_FIREBALL, ItemTypes.FIREWORK_CHARGE);
        ENTITY_ITEMS.put(EntityTypes.ENDER_PEARL, ItemTypes.ENDER_PEARL);
        ENTITY_ITEMS.put(EntityTypes.THROWN_EXP_BOTTLE, ItemTypes.EXPERIENCE_BOTTLE);
        ENTITY_ITEMS.put(EntityTypes.ITEM_FRAME, ItemTypes.ITEM_FRAME);
        ENTITY_ITEMS.put(EntityTypes.PRIMED_TNT, ItemTypes.TNT);
        ENTITY_ITEMS.put(EntityTypes.FIREWORK, ItemTypes.FIREWORKS);
        ENTITY_ITEMS.put(EntityTypes.COMMANDBLOCK_MINECART, ItemTypes.COMMAND_BLOCK_MINECART);
        ENTITY_ITEMS.put(EntityTypes.BOAT, ItemTypes.BOAT);
        ENTITY_ITEMS.put(EntityTypes.RIDEABLE_MINECART, ItemTypes.MINECART);
        ENTITY_ITEMS.put(EntityTypes.CHESTED_MINECART, ItemTypes.CHEST_MINECART);
        ENTITY_ITEMS.put(EntityTypes.FURNACE_MINECART, ItemTypes.FURNACE_MINECART);
        ENTITY_ITEMS.put(EntityTypes.TNT_MINECART, ItemTypes.TNT_MINECART);
        ENTITY_ITEMS.put(EntityTypes.HOPPER_MINECART, ItemTypes.HOPPER_MINECART);
        ENTITY_ITEMS.put(EntityTypes.SPLASH_POTION, ItemTypes.POTION);
        ENTITY_ITEMS.put(EntityTypes.EGG, ItemTypes.EGG);

        BLOCK_MATERIAL_FLAGS.put(BlockTypes.AIR, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STONE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.GRASS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DIRT, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.COBBLESTONE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.PLANKS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.SAPLING, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.BEDROCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.FLOWING_WATER, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.WATER, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.FLOWING_LAVA, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LAVA, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.SAND, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.GRAVEL, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.GOLD_ORE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.IRON_ORE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.COAL_ORE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LOG, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LEAVES, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.SPONGE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.GLASS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LAPIS_ORE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LAPIS_BLOCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DISPENSER, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.SANDSTONE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.NOTEBLOCK, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.BED, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.GOLDEN_RAIL, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DETECTOR_RAIL, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STICKY_PISTON, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.WEB, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.TALLGRASS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DEADBUSH, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.PISTON, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.PISTON_EXTENSION, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.WOOL, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.PISTON_HEAD, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.YELLOW_FLOWER, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.RED_FLOWER, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.BROWN_MUSHROOM, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.RED_MUSHROOM, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.GOLD_BLOCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.IRON_BLOCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DOUBLE_STONE_SLAB, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STONE_SLAB, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.BRICK_BLOCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.TNT, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.BOOKSHELF, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.MOSSY_COBBLESTONE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.OBSIDIAN, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.TORCH, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.FIRE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.MOB_SPAWNER, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.OAK_STAIRS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.CHEST, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.REDSTONE_WIRE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DIAMOND_ORE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DIAMOND_BLOCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.CRAFTING_TABLE, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.WHEAT, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.FARMLAND, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.FURNACE, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LIT_FURNACE, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STANDING_SIGN, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.WOODEN_DOOR, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LADDER, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.RAIL, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STONE_STAIRS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.WALL_SIGN, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LEVER, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STONE_PRESSURE_PLATE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.IRON_DOOR, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.WOODEN_PRESSURE_PLATE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.REDSTONE_ORE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LIT_REDSTONE_ORE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.UNLIT_REDSTONE_TORCH, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.REDSTONE_TORCH, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STONE_BUTTON, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.SNOW, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.ICE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.SNOW, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.CACTUS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.CLAY, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.REEDS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.JUKEBOX, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.FENCE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.PUMPKIN, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.NETHERRACK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.SOUL_SAND, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.GLOWSTONE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.PORTAL, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LIT_PUMPKIN, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.CAKE, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.UNPOWERED_REPEATER, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.POWERED_REPEATER, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STAINED_GLASS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.TRAPDOOR, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.MONSTER_EGG, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STONEBRICK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.BROWN_MUSHROOM_BLOCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.RED_MUSHROOM_BLOCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.IRON_BARS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.GLASS_PANE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.MELON_BLOCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.PUMPKIN_STEM, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.MELON_STEM, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.VINE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.FENCE_GATE, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.BRICK_STAIRS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STONE_BRICK_STAIRS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.MYCELIUM, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.WATERLILY, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.NETHER_BRICK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.NETHER_BRICK_FENCE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.NETHER_BRICK_STAIRS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.NETHER_WART, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.ENCHANTING_TABLE, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.BREWING_STAND, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.CAULDRON, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.END_PORTAL, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.END_PORTAL_FRAME, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.END_STONE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DRAGON_EGG, MODIFIED_ON_LEFT | MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.REDSTONE_LAMP, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LIT_REDSTONE_LAMP, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DOUBLE_WOODEN_SLAB, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.WOODEN_SLAB, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.COCOA, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.SANDSTONE_STAIRS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.EMERALD_ORE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.ENDER_CHEST, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.TRIPWIRE_HOOK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.TRIPWIRE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.EMERALD_BLOCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.SPRUCE_STAIRS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.BIRCH_STAIRS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.JUNGLE_STAIRS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.COMMAND_BLOCK, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.BEACON, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.COBBLESTONE_WALL, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.FLOWER_POT, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.CARROTS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.POTATOES, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.WOODEN_BUTTON, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.SKULL, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.ANVIL, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.TRAPPED_CHEST, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LIGHT_WEIGHTED_PRESSURE_PLATE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.HEAVY_WEIGHTED_PRESSURE_PLATE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.UNPOWERED_COMPARATOR, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.POWERED_COMPARATOR, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DAYLIGHT_DETECTOR, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.REDSTONE_BLOCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.QUARTZ_ORE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.HOPPER, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.QUARTZ_BLOCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.QUARTZ_STAIRS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.ACTIVATOR_RAIL, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DROPPER, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STAINED_HARDENED_CLAY, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STAINED_GLASS_PANE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LEAVES2, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.LOG2, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.ACACIA_STAIRS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DARK_OAK_STAIRS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.HAY_BLOCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.CARPET, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.HARDENED_CLAY, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.COAL_BLOCK, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.PACKED_ICE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DOUBLE_PLANT, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STANDING_BANNER, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.WALL_BANNER, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DAYLIGHT_DETECTOR_INVERTED, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.RED_SANDSTONE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.RED_SANDSTONE_STAIRS, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DOUBLE_STONE_SLAB2, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.STONE_SLAB2, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.SPRUCE_FENCE_GATE, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.BIRCH_FENCE_GATE, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.JUNGLE_FENCE_GATE, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DARK_OAK_FENCE_GATE, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.ACACIA_FENCE_GATE, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.SPRUCE_FENCE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.BIRCH_FENCE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.JUNGLE_FENCE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DARK_OAK_FENCE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.ACACIA_FENCE, 0);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.SPRUCE_DOOR, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.BIRCH_DOOR, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.JUNGLE_DOOR, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.ACACIA_DOOR, MODIFIED_ON_RIGHT);
        BLOCK_MATERIAL_FLAGS.put(BlockTypes.DARK_OAK_DOOR, MODIFIED_ON_RIGHT);

        ITEM_MATERIAL_FLAGS.put(ItemTypes.IRON_SHOVEL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.IRON_PICKAXE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.IRON_AXE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.FLINT_AND_STEEL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.APPLE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BOW, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.ARROW, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.COAL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.DIAMOND, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.IRON_INGOT, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLD_INGOT, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.IRON_SWORD, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.WOODEN_SWORD, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.WOODEN_SHOVEL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.WOODEN_PICKAXE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.WOODEN_AXE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.STONE_SWORD, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.STONE_SHOVEL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.STONE_PICKAXE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.STONE_AXE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.DIAMOND_SWORD, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.DIAMOND_SHOVEL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.DIAMOND_PICKAXE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.DIAMOND_AXE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.STICK, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BOWL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.MUSHROOM_STEW, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLDEN_SWORD, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLDEN_SHOVEL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLDEN_PICKAXE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLDEN_AXE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.STRING, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.FEATHER, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GUNPOWDER, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.WOODEN_HOE, MODIFIES_BLOCKS);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.STONE_HOE, MODIFIES_BLOCKS);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.IRON_HOE, MODIFIES_BLOCKS);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.DIAMOND_HOE, MODIFIES_BLOCKS);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLDEN_HOE, MODIFIES_BLOCKS);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.WHEAT_SEEDS, MODIFIES_BLOCKS);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.WHEAT, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BREAD, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.LEATHER_HELMET, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.LEATHER_CHESTPLATE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.LEATHER_LEGGINGS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.LEATHER_BOOTS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.CHAINMAIL_HELMET, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.CHAINMAIL_CHESTPLATE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.CHAINMAIL_LEGGINGS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.CHAINMAIL_BOOTS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.IRON_HELMET, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.IRON_CHESTPLATE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.IRON_LEGGINGS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.IRON_BOOTS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.DIAMOND_HELMET, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.DIAMOND_CHESTPLATE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.DIAMOND_LEGGINGS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.DIAMOND_BOOTS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLDEN_HELMET, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLDEN_CHESTPLATE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLDEN_LEGGINGS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLDEN_BOOTS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.FLINT, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.PORKCHOP, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.COOKED_PORKCHOP, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.PAINTING, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLDEN_APPLE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.SIGN, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.WOODEN_DOOR, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BUCKET, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.WATER_BUCKET, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.LAVA_BUCKET, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.MINECART, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.SADDLE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.IRON_DOOR, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.REDSTONE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.SNOWBALL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BOAT, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.LEATHER, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.MILK_BUCKET, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BRICK, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.CLAY_BALL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.REEDS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.PAPER, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BOOK, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.SLIME_BALL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.CHEST_MINECART, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.FURNACE_MINECART, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.EGG, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.COMPASS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.FISHING_ROD, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.CLOCK, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GLOWSTONE_DUST, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.FISH, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.COOKED_FISH, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.DYE, MODIFIES_BLOCKS);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BONE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.SUGAR, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.CAKE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BED, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.REPEATER, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.COOKIE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.MAP, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.SHEARS, MODIFIES_BLOCKS);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.MELON, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.PUMPKIN_SEEDS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.MELON_SEEDS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BEEF, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.COOKED_BEEF, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.CHICKEN, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.COOKED_CHICKEN, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.ROTTEN_FLESH, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.ENDER_PEARL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BLAZE_ROD, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GHAST_TEAR, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLD_NUGGET, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.NETHER_WART, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.POTION, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GLASS_BOTTLE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.SPIDER_EYE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.FERMENTED_SPIDER_EYE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BLAZE_POWDER, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.MAGMA_CREAM, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BREWING_STAND, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.CAULDRON, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.ENDER_EYE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.SPECKLED_MELON, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.MONSTER_EGG, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.EXPERIENCE_BOTTLE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.FIRE_CHARGE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.WRITABLE_BOOK, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.WRITTEN_BOOK, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.EMERALD, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.ITEM_FRAME, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.FLOWER_POT, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.CARROT, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.POTATO, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.BAKED_POTATO, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.POISONOUS_POTATO, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.MAP, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLDEN_CARROT, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.SKULL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.CARROT_ON_A_STICK, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.NETHER_STAR, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.PUMPKIN_PIE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.FIREWORKS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.FIREWORK_CHARGE, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.ENCHANTED_BOOK, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.COMPARATOR, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.NETHER_BRICK, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.QUARTZ, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.TNT_MINECART, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.HOPPER_MINECART, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.IRON_HORSE_ARMOR, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.GOLDEN_HORSE_ARMOR, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.DIAMOND_HORSE_ARMOR, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.LEAD, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.NAME_TAG, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.COMMAND_BLOCK_MINECART, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.RECORD_13, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.RECORD_CAT, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.RECORD_BLOCKS, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.RECORD_CHIRP, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.RECORD_FAR, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.RECORD_MALL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.RECORD_MELLOHI, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.RECORD_STAL, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.RECORD_STRAD, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.RECORD_WARD, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.RECORD_11, 0);
        ITEM_MATERIAL_FLAGS.put(ItemTypes.RECORD_WAIT, 0);

        //DAMAGE_EFFECTS.add(PotionEffectType.ABSORPTION);
        DAMAGE_EFFECTS.add(PotionEffectTypes.BLINDNESS);
        DAMAGE_EFFECTS.add(PotionEffectTypes.NAUSEA);
        //DAMAGE_EFFECTS.add(PotionEffectType.DAMAGE_RESISTANCE);
        //DAMAGE_EFFECTS.add(PotionEffectType.FAST_DIGGING);
        //DAMAGE_EFFECTS.add(PotionEffectType.FIRE_RESISTANCE);
        DAMAGE_EFFECTS.add(PotionEffectTypes.INSTANT_DAMAGE);
        //DAMAGE_EFFECTS.add(PotionEffectType.HEAL);
        //DAMAGE_EFFECTS.add(PotionEffectType.HEALTH_BOOST);
        DAMAGE_EFFECTS.add(PotionEffectTypes.HUNGER);
        //DAMAGE_EFFECTS.add(PotionEffectType.INCREASE_DAMAGE);
        //DAMAGE_EFFECTS.add(PotionEffectType.INVISIBILITY);
        //DAMAGE_EFFECTS.add(PotionEffectType.JUMP);
        //DAMAGE_EFFECTS.add(PotionEffectType.NIGHT_VISION);
        DAMAGE_EFFECTS.add(PotionEffectTypes.POISON);
        //DAMAGE_EFFECTS.add(PotionEffectType.REGENERATION);
        //DAMAGE_EFFECTS.add(PotionEffectType.SATURATION);
        DAMAGE_EFFECTS.add(PotionEffectTypes.SLOWNESS);
        DAMAGE_EFFECTS.add(PotionEffectTypes.MINING_FATIGUE);
        //DAMAGE_EFFECTS.add(PotionEffectType.SPEED);
        //DAMAGE_EFFECTS.add(PotionEffectType.WATER_BREATHING);
        DAMAGE_EFFECTS.add(PotionEffectTypes.WEAKNESS);
        DAMAGE_EFFECTS.add(PotionEffectTypes.WITHER);
    }

    private Materials() {
    }

    /**
     * Get the related material for an entity type.
     *
     * @param type the entity type
     * @return the related material or {@code null} if one is not known or exists
     */
    @Nullable
    public static ItemType getRelatedMaterial(EntityType type) {
        return ENTITY_ITEMS.get(type);
    }

    /**
     * Get the material of the block placed by the given bucket, defaulting
     * to water if the bucket type is not known.
     *
     * <p>If a non-bucket material is given, it will be assumed to be
     * an unknown bucket type. If the given bucket doesn't have a block form
     * (it can't be placed), then water will be returned (i.e. for milk).
     * Be aware that either the stationary or non-stationary material may be
     * returned.</p>
     *
     * @param type the bucket material
     * @return the block material
     */
    public static BlockType getBucketBlockMaterial(ItemType type) {
        if (type.equals(ItemTypes.LAVA_BUCKET)) {
            return BlockTypes.FLOWING_LAVA;
        } else if (type.equals(ItemTypes.MILK_BUCKET)) {
            return BlockTypes.WATER;
        } else {
           return BlockTypes.WATER;
        }
    }

    /**
     * Test whether the given material is a mushroom.
     *
     * @param material the material
     * @return true if a mushroom block
     */
    public static boolean isMushroom(BlockType material) {
        return material.equals(BlockTypes.RED_MUSHROOM) || material.equals(BlockTypes.BROWN_MUSHROOM);
    }

    /**
     * Test whether the given material is a leaf block.
     *
     * @param material the material
     * @return true if a leaf block
     */
    public static boolean isLeaf(BlockType material) {
        return material.equals(BlockTypes.LEAVES) || material.equals(BlockTypes.LEAVES2);
    }

    /**
     * Test whether the given material is a liquid block.
     *
     * @param material the material
     * @return true if a liquid block
     */
    public static boolean isLiquid(BlockType material) {
        return isWater(material) || isLava(material);
    }

    /**
     * Test whether the given material is water.
     *
     * @param material the material
     * @return true if a water block
     */
    public static boolean isWater(BlockType material) {
        return material.equals(BlockTypes.FLOWING_WATER) || material.equals(BlockTypes.WATER);
    }

    /**
     * Test whether the given material is lava.
     *
     * @param material the material
     * @return true if a lava block
     */
    public static boolean isLava(BlockType material) {
        return material.equals(BlockTypes.LAVA) || material.equals(BlockTypes.FLOWING_LAVA);
    }

    /**
     * Test whether the given material is a portal material.
     *
     * @param material the material
     * @return true if a portal block
     */
    public static boolean isPortal(BlockType material) {
        return material.equals(BlockTypes.PORTAL) || material.equals(BlockTypes.END_PORTAL);
    }

    /**
     * Test whether the given material data is of the given dye color.
     *
     * <p>Returns false for non-dyed items.</p>
     *
     * @param data the data
     * @return true if it is the provided dye color
     */
    public static boolean isDyeColor(DyeableData data, DyeColor color) {
        return data.get(Keys.DYE_COLOR).get().equals(color);
    }

    /**
     * Test whether the given material is a rail block.
     *
     * @param material the material
     * @return true if a rail block
     */
    public static boolean isRailBlock(BlockType material) {
        return material.equals(BlockTypes.RAIL)
                || material.equals(BlockTypes.ACTIVATOR_RAIL)
                || material.equals(BlockTypes.DETECTOR_RAIL)
                || material.equals(BlockTypes.GOLDEN_RAIL);
    }

    /**
     * Test whether the given material is a piston block, not including
     * the "technical blocks" such as the piston extension block.
     *
     * @param material the material
     * @return true if a piston block
     */
    public static boolean isPistonBlock(BlockType material) {
        return material.equals(BlockTypes.PISTON)
                || material.equals(BlockTypes.STICKY_PISTON);
    }

    /**
     * Test whether the given material is a Minecart.
     *
     * @param material the material
     * @return true if a Minecart item
     */
    public static boolean isMinecart(ItemType material) {
        return material.equals(ItemTypes.MINECART)
                || material.equals(ItemTypes.COMMAND_BLOCK_MINECART)
                || material.equals(ItemTypes.TNT_MINECART)
                || material.equals(ItemTypes.HOPPER_MINECART)
                || material.equals(ItemTypes.FURNACE_MINECART)
                || material.equals(ItemTypes.CHEST_MINECART);
    }

    /**
     * Test whether the given material is an inventory block.
     *
     * @param material the material
     * @return true if an inventory block
     */
    public static boolean isInventoryBlock(BlockType material) {
        return material.equals(BlockTypes.CHEST)
                || material.equals(BlockTypes.JUKEBOX)
                || material.equals(BlockTypes.DISPENSER)
                || material.equals(BlockTypes.FURNACE)
                || material.equals(BlockTypes.LIT_FURNACE)
                || material.equals(BlockTypes.BREWING_STAND)
                || material.equals(BlockTypes.TRAPPED_CHEST)
                || material.equals(BlockTypes.HOPPER)
                || material.equals(BlockTypes.DROPPER);
    }

    /**
     * Test whether the given material is affected by
     * {@link DefaultFlag#USE}.
     *
     * <p>Generally, materials that are considered by this method are those
     * that are not inventories but can be used.</p>
     *
     * @param material the material
     * @return true if covered by the use flag
     */
    public static boolean isUseFlagApplicable(BlockType material) {
        if (material.equals(BlockTypes.LEVER)) return true;
        if (material.equals(BlockTypes.STONE_BUTTON)) return true;
        if (material.equals(BlockTypes.WOODEN_BUTTON)) return true;
        if (material.equals(BlockTypes.WOODEN_DOOR)) return true;
        if (material.equals(BlockTypes.TRAPDOOR)) return true;
        if (material.equals(BlockTypes.FENCE_GATE)) return true;
        if (material.equals(BlockTypes.CRAFTING_TABLE)) return true;
        if (material.equals(BlockTypes.ENCHANTING_TABLE)) return true;
        if (material.equals(BlockTypes.BEACON)) return true;
        if (material.equals(BlockTypes.ANVIL)) return true;
        if (material.equals(BlockTypes.WOODEN_PRESSURE_PLATE)) return true;
        if (material.equals(BlockTypes.STONE_PRESSURE_PLATE)) return true;
        if (material.equals(BlockTypes.LIGHT_WEIGHTED_PRESSURE_PLATE)) return true;
        if (material.equals(BlockTypes.HEAVY_WEIGHTED_PRESSURE_PLATE)) return true;
        if (material.equals(BlockTypes.SPRUCE_FENCE_GATE)) return true;
        if (material.equals(BlockTypes.BIRCH_FENCE_GATE)) return true;
        if (material.equals(BlockTypes.JUNGLE_FENCE_GATE)) return true;
        if (material.equals(BlockTypes.DARK_OAK_FENCE_GATE)) return true;
        if (material.equals(BlockTypes.ACACIA_FENCE_GATE)) return true;
        if (material.equals(BlockTypes.SPRUCE_DOOR)) return true;
        if (material.equals(BlockTypes.BIRCH_DOOR)) return true;
        if (material.equals(BlockTypes.JUNGLE_DOOR)) return true;
        if (material.equals(BlockTypes.ACACIA_DOOR)) return true;
        if (material.equals(BlockTypes.DARK_OAK_DOOR)) return true;

        return false;
    }

    /**
     * Test whether the given type is Redstone ore.
     *
     * @param type the material
     * @return true if Redstone ore
     */
    public static boolean isRedstoneOre(BlockType type) {
        return type.equals(BlockTypes.LIT_REDSTONE_ORE) || type.equals(BlockTypes.REDSTONE_ORE);
    }

    public static boolean isWoodDoor(BlockType type) {
        return type.equals(BlockTypes.ACACIA_DOOR)
                || type.equals(BlockTypes.BIRCH_DOOR)
                || type.equals(BlockTypes.DARK_OAK_DOOR)
                || type.equals(BlockTypes.JUNGLE_DOOR)
                || type.equals(BlockTypes.SPRUCE_DOOR)
                || type.equals(BlockTypes.WOODEN_DOOR);
    }

    /**
     * Test whether the given material is a block that is modified when it is
     * left or right clicked.
     *
     * <p>This test is conservative, returning true for blocks that it is not
     * aware of.</p>
     *
     * @param material the material
     * @param rightClick whether it is a right click
     * @return true if the block is modified
     */
    public static boolean isBlockModifiedOnClick(BlockType material, boolean rightClick) {
        Integer flags = BLOCK_MATERIAL_FLAGS.get(material);
        return flags == null
                || (rightClick && (flags & MODIFIED_ON_RIGHT) == MODIFIED_ON_RIGHT)
                || (!rightClick && (flags & MODIFIED_ON_LEFT) == MODIFIED_ON_LEFT);
    }

    /**
     * Test whether the given item modifies a given block when right clicked.
     *
     * <p>This test is conservative, returning true for items that it is not
     * aware of or does not have the details for.</p>
     *
     * @param item the item
     * @param block the block
     * @return true if the item is applied to the block
     */
    public static boolean isItemAppliedToBlock(ItemType item, BlockType block) {
        Integer flags = ITEM_MATERIAL_FLAGS.get(item);
        return flags == null || (flags & MODIFIES_BLOCKS) == MODIFIES_BLOCKS;
    }

    /**
     * Test whether the given material should be tested as "building" when
     * it is used.
     *
     * @param type the type
     * @return true to be considered as used
     */
    public static boolean isConsideredBuildingIfUsed(BlockType type) {
        return type.equals(BlockTypes.SAPLING);
    }

    /**
     * Test whether a list of potion effects contains one or more potion
     * effects used for doing damage.
     *
     * @param effects A collection of effects
     * @return True if at least one damage effect exists
     */
    public static boolean hasDamageEffect(Collection<PotionEffect> effects) {
        for (PotionEffect effect : effects) {
            if (DAMAGE_EFFECTS.contains(effect.getType())) {
                return true;
            }
        }

        return false;
    }

}
