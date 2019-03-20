package com.sunekaer.mods.netherlake;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenGlowStone2;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class WorldProviderHellLake extends WorldProviderHell {
    @Override
    public IChunkGenerator createChunkGenerator()
    {
        return new ChunkGeneratorHellLake(world, world.getSeed());
    }


    public static class ChunkGeneratorHellLake extends ChunkGeneratorHell
    {
        private World world;
        private Random random;
        private MapGenNetherBridge genNetherBridge = new MapGenNetherBridge();

        public ChunkGeneratorHellLake(World world, long seed)
        {
            super(world, true, seed);
            this.world = world;
            this.random = new Random(seed);
            this.genNetherBridge = (MapGenNetherBridge)TerrainGen.getModdedMapGen(genNetherBridge, InitMapGenEvent.EventType.NETHER_BRIDGE);
        }

        @Override
        public void populate(int x, int z)
        {
            genNetherBridge.generateStructure(world, random, new ChunkPos(x, z));
        }

        @Override
        public Chunk generateChunk(int x, int z)
        {
            ChunkPrimer primer = new ChunkPrimer();
            IBlockState bedrock = Blocks.BEDROCK.getDefaultState();
            IBlockState lava = Blocks.LAVA.getDefaultState();
            genNetherBridge.generate(world, x, z, primer);
            int x1, y1, z1;
            for (x1 = 0; x1 < 16; x1++) {
                for (z1 = 0; z1 < 16; z1++) {
                    primer.setBlockState(x1, 0, z1, bedrock);
                }
            }
            for (x1 = 0; x1 < 16; x1++) {
                for (y1 = 1; y1 < 45; y1++) {
                    for (z1 = 0; z1 < 16; z1++) {
                        primer.setBlockState(x1, y1, z1, lava);
                    }
                }
            }
            for (x1 = 0; x1 < 16; x1++) {
                for (z1 = 0; z1 < 16; z1++) {
                    primer.setBlockState(x1, 256, z1, bedrock);
                }
            }

            Chunk chunk = new Chunk(this.world, primer, x, z);

            byte[] biomeArray = chunk.getBiomeArray();
            byte id = (byte) Biome.getIdForBiome(Biomes.HELL);
            for (int i = 0; i < biomeArray.length; ++i) {
                biomeArray[i] = id;
            }

            chunk.generateSkylightMap();

            return chunk;
        }
    }
}
