package com.manya.pdc.base.collection.primitive;

import com.manya.pdc.base.collection.TypeBasedCollectionDataType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.stream.Collector;

public class ShortCollectionDataType<A, Z extends Collection<E>, E> extends TypeBasedCollectionDataType<A, Z, E, Short, byte[]> {
    public ShortCollectionDataType(Collector<E, A, Z> collector, PersistentDataType<Short, E> elementDataType) {
        super(collector, byte[].class, elementDataType);
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull Z complex, @NotNull PersistentDataAdapterContext context) {
        ByteBuffer buffer = ByteBuffer.allocate(complex.size() * Short.BYTES);
        for(E element : complex) {
            buffer.putShort(elementDataType.toPrimitive(element, context));
        }
        return buffer.array();
    }

    @Override
    public @NotNull Z fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        A container = createContainer();
        ByteBuffer buffer = ByteBuffer.wrap(primitive);
        while(buffer.remaining() >= Short.BYTES) {
            accumulate(container, elementDataType.fromPrimitive(buffer.getShort(), context));
        }
        return finish(container);
    }
}
