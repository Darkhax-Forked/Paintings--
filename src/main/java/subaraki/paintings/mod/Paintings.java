package subaraki.paintings.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.entity.decoration.Motive;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import subaraki.paintings.event.EventRegistry;
import subaraki.paintings.packet.NetworkHandler;
import subaraki.paintings.util.PaintingUtility;
import subaraki.paintings.util.json.PaintingPackReader;

@Mod(Paintings.MODID)
@EventBusSubscriber(modid = Paintings.MODID, bus = Bus.MOD)
public class Paintings {

    public static final String MODID = "paintings";
    public static final Logger LOG = LogManager.getLogger(MODID);

    /** call init here, to read json files before any event is launched. */
    private static final PaintingPackReader PAINTING_PACK_READER = new PaintingPackReader().init();

    public static final PaintingUtility UTILITY = new PaintingUtility();

    public Paintings() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::modConfig);

        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        modLoadingContext.registerConfig(ModConfig.Type.SERVER, ConfigData.SERVER_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigData.CLIENT_SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

        new EventRegistry();
        new NetworkHandler();
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {

        // init on class load to make sure the list is filled before the registry access
        PAINTING_PACK_READER.registerReloadListener();

    }

    public void modConfig(ModConfigEvent event)
    {

        ModConfig config = event.getConfig();
        if (config.getSpec() == ConfigData.CLIENT_SPEC)
            ConfigData.refreshClient();
        else
            if (config.getSpec() == ConfigData.SERVER_SPEC)
                ConfigData.refreshServer();
    }

    @SubscribeEvent
    public static void registerPaintings(RegistryEvent.Register<Motive> event)
    {

        PaintingPackReader.registerToMinecraft(event);
    }
}
