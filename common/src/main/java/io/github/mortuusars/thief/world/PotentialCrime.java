package io.github.mortuusars.thief.world;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.IntFunction;

public enum PotentialCrime implements StringRepresentable {
    NONE("none", Optional.empty()),
    LIGHT("light", Optional.of(Crime.LIGHT)),
    MEDIUM("medium", Optional.of(Crime.MEDIUM)),
    HEAVY("heavy", Optional.of(Crime.HEAVY));

    public static final Codec<PotentialCrime> CODEC = StringRepresentable.fromEnum(PotentialCrime::values);
    public static final IntFunction<PotentialCrime> BY_ID = ByIdMap.continuous(PotentialCrime::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, PotentialCrime> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, PotentialCrime::ordinal);

    private final String name;
    private final Optional<Crime> offence;

    PotentialCrime(String name, Optional<Crime> offence) {
        this.name = name;
        this.offence = offence;
    }

    public Optional<Crime> getCrime() {
        return offence;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
