package com.epimorphismmc.monomorphism.pattern;

import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.lowdragmc.lowdraglib.utils.BlockInfo;

import java.lang.reflect.Array;

public class LayerShapeInfo extends MultiblockShapeInfo {
    public LayerShapeInfo(BlockInfo[][][] blocks) {
        super(blocks);
    }

    public static LayerInfoBuilder builder() {
        return new LayerInfoBuilder();
    }

    public static class LayerInfoBuilder extends ShapeInfoBuilder {
        public LayerInfoBuilder() {/**/}

        @Override
        public BlockInfo[][][] bakeArray(Class<BlockInfo> clazz, BlockInfo defaultValue) {
            BlockInfo[][][] Ts = (BlockInfo[][][]) Array.newInstance(clazz, shape.get(0)[0].length(), shape.size(), shape.get(0).length);
            for (int y = 0; y < shape.size(); y++) { //y
                String[] aisleEntry = shape.get(y);
                for (int z = 0; z < shape.get(0).length; z++) {
                    String columnEntry = aisleEntry[z];
                    for (int x = 0; x < columnEntry.length(); x++) {
                        BlockInfo info = symbolMap.getOrDefault(columnEntry.charAt(x), defaultValue);
                        Ts[x][y][z] = info;
                    }
                }
            }
            return Ts;
        }
    }
}
