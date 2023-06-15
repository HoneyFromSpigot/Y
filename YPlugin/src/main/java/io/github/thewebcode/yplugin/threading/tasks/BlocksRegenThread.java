package io.github.thewebcode.yplugin.threading.tasks;

import com.google.common.collect.Lists;
import io.github.thewebcode.yplugin.block.BlockData;
import io.github.thewebcode.yplugin.block.Blocks;
import io.github.thewebcode.yplugin.effect.Effects;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlocksRegenThread implements Runnable {
    private List<List<BlockData>> blockCollections;
    private boolean playEffect = true;

    public BlocksRegenThread(List<BlockData> blockCollection) {
        this.blockCollections = Lists.partition(blockCollection, 5);
    }

    public BlocksRegenThread(List<Block> blocks, boolean playEffect) {
        List<BlockData> blockDataList = new ArrayList<>();
        blocks.forEach(b -> blockDataList.add(new BlockData(b)));
        this.playEffect = playEffect;
        this.blockCollections = Lists.partition(blockDataList, 5);
    }

    @Override
    public void run() {
        for (List<BlockData> dataList : blockCollections) {
            if (playEffect) {
                for (BlockData data : dataList) {
                    Blocks.setBlock(data.getBlock(), data.getType());
                    Effects.playBlockBreakEffect(data.getLocation(), Effects.BLOCK_EFFECT_RADIUS, data.getType());
                }
            } else {
                dataList.forEach(b -> Blocks.setBlock(b.getBlock(), b.getType()));
            }
        }

    }
}
