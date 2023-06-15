package io.github.thewebcode.yplugin.block;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Collection;

public class BlockData {

    private byte blockByte;
    private Material material;
    private MaterialData materialData = null;
    private Location location;
    private byte lightLevel;
    private byte lightFromSky;
    private byte lightFromBlocks;
    private boolean liquid = false;

    public BlockData(Block block) {
        location = block.getLocation();
        material = block.getType();
        materialData = new MaterialData(material);
        blockByte = block.getData();
        lightLevel = block.getLightLevel();
        lightFromSky = block.getLightFromSky();
        lightFromBlocks = block.getLightFromBlocks();
        liquid = block.isLiquid();
    }

    public Block getBlock() {
        return location.getBlock();
    }

    private void updateMaterialData() {
        materialData = new MaterialData(material);
    }

    public MaterialData getMaterialData() {
        return materialData;
    }

    public Material getType() {
        return materialData.getItemType();
    }

    public byte getLightLevel() {
        return lightLevel;
    }

    public byte getLightFromSky() {
        return lightFromSky;
    }

    public World getWorld() {
        return location.getWorld();
    }

    public int getX() {
        return location.getBlockX();
    }

    public int getY() {
        return location.getBlockY();
    }

    public int getZ() {
        return location.getBlockZ();
    }

    public byte getData() {
        return blockByte;
    }

    public Location getLocation() {
        return location;
    }

    public Chunk getChunk() {
        return location.getChunk();
    }

    public void setData(byte b) {
        this.blockByte = b;
    }

    public void setType(Material material) {
        this.material = material;
        updateMaterialData();
    }

    public BlockFace getFace(Block block) {
        return getBlock().getFace(block);
    }

    public BlockState getState() {
        return getBlock().getState();
    }

    public Biome getBiome() {
        return getBlock().getBiome();
    }

    public int getBlockPower() {
        return getBlock().getBlockPower();
    }

    public boolean isLiquid() {
        return liquid;
    }

    public boolean breakNaturally() {
        return getBlock().breakNaturally();
    }

    public boolean breakNaturally(ItemStack itemStack) {
        return getBlock().breakNaturally(itemStack);
    }

    public Collection<ItemStack> getDrops() {
        return getBlock().getDrops();
    }

    public Collection<ItemStack> getDrops(ItemStack itemStack) {
        return getBlock().getDrops(itemStack);
    }

    public byte getLightFromBlocks() {
        return lightFromBlocks;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Block) {
            Block block = (Block) o;
            return getWorld().equals(block.getWorld()) && getX() == block.getX() && getY() == block.getY() && getZ() == block.getZ() && getType() == block.getType();
        }

        if (o instanceof BlockData) {
            BlockData blockData = (BlockData) o;
            return blockData.getWorld().equals(getWorld()) && blockData.getType() == getType() && blockData.getX() == getX() && blockData.getZ() == getZ() && blockData.getY() == getY();
        }
        return false;
    }
}
