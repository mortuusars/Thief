package io.github.mortuusars.thief.compat.lithostitched;

import dev.worldgen.lithostitched.worldgen.structure.DelegatingStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.Iterator;

public class LithostitchedCompat {
    public static StructureStart getStructureWithPieceAt(ServerLevel level, BlockPos pos, TagKey<Structure> structureTag) {
        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        Iterator<StructureStart> structures = level.structureManager().startsForStructure(new ChunkPos(pos),
                structure -> {
                    if (structure instanceof DelegatingStructure delegatingStructure) {
                        structure = delegatingStructure.delegate();
                    }
                    return registry.getHolder(registry.getId(structure))
                            .map(reference -> reference.is(structureTag))
                            .orElse(false);
                }).iterator();

        StructureStart structureStart;
        do {
            if (!structures.hasNext()) {
                return StructureStart.INVALID_START;
            }
            structureStart = structures.next();
        } while (!level.structureManager().structureHasPieceAt(pos, structureStart));
        return structureStart;
    }
}
