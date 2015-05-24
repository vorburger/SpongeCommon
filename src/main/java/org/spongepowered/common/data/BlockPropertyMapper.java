package org.spongepowered.common.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.properties.IProperty;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.manipulator.block.PortionData;
import org.spongepowered.api.data.type.PortionType;
import org.spongepowered.api.data.type.PortionTypes;
import org.spongepowered.common.data.manipulator.block.SpongePortionData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BlockPropertyMapper {

    private static final Map<IProperty, DMConverter> builderMap = new HashMap<IProperty, DMConverter>();

    static {
        builderMap.put(BlockDoublePlant.HALF, new DMConverter<BlockDoublePlant.EnumBlockHalf, PortionData>() {
            @Override
            public PortionData convertFrom(BlockDoublePlant.EnumBlockHalf value) {
                PortionType type = (value == BlockDoublePlant.EnumBlockHalf.LOWER) ? PortionTypes.BOTTOM : PortionTypes.TOP;
                return new SpongePortionData().setValue(type);
            }

            @Override
            public BlockDoublePlant.EnumBlockHalf convertTo(PortionData value) {
                return value.getValue().equals(PortionTypes.BOTTOM) ? BlockDoublePlant.EnumBlockHalf.LOWER : BlockDoublePlant.EnumBlockHalf.UPPER;
            }
        });
    }

    public static ImmutableList<DataManipulator<?>> fromPropertyMap(Map map) {
        ImmutableList.Builder<DataManipulator<?>> builder = ImmutableList.builder();
        for (Object entry: map.entrySet()) {
            Entry ent = (Entry) entry;
            Object key = ent.getKey();
            if (builderMap.containsKey(key)) {
                builder.add(builderMap.get(key).convertFrom(ent.getValue()));
            }
        }
        return builder.build();
    }

    public static abstract class DMConverter<O, M extends DataManipulator<M>> {

        public abstract M convertFrom(O value);

        public abstract O convertTo(M value);

    }

}
