package io.github.thewebcode.yplugin.world;

import io.github.thewebcode.yplugin.yml.Path;
import io.github.thewebcode.yplugin.yml.YamlConfig;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.io.File;

public class ChunkData extends YamlConfig {

    @Path("world")
    private String worldName;

    @Path("x")
    private int x;

    @Path("y")
    private int z;

    private int[] cords;

    public ChunkData(Chunk chunk) {
        x = chunk.getX();
        z = chunk.getZ();
        cords = new int[]{x, z};
    }

    public ChunkData(File file) {
        super(file);
    }

    public boolean similarTo(Object o) {
        if (o instanceof ChunkData) {
            ChunkData chunkData = (ChunkData) o;
            return (equals(o)) || (x == chunkData.x && z == chunkData.z);
        } else if (o instanceof Chunk) {
            Chunk chunk = (Chunk) o;
            return x == chunk.getX() && z == chunk.getZ();
        } else {
            return false;
        }
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int[] getCords() {
        return cords;
    }

    public Chunk getChunk() {
        return Chunks.getChunkAt(Worlds.getWorld(worldName), x, z);
    }


    public boolean isIn(Block block) {
        return block.getX() >> 4 == getX() && block.getZ() >> 4 == getZ();
    }

}
