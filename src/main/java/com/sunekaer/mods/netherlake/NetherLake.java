package com.sunekaer.mods.netherlake;


import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
        modid = NetherLake.MOD_ID,
        name = NetherLake.MOD_NAME,
        version = NetherLake.VERSION
)

public class NetherLake {
    public static final String MOD_ID = "netherlake";
    public static final String MOD_NAME = "NetherLake";
    public static final String VERSION = "0.0.0.netherlake";

    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
        DimensionManager.unregisterDimension(-1);
        DimensionManager.registerDimension(-1, DimensionType.register("Nether", "_nether", -1, WorldProviderHellLake.class, false));
    }
}
