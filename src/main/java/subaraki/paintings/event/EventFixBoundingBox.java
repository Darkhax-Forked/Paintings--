package subaraki.paintings.event;

import net.minecraft.world.entity.decoration.Painting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import subaraki.paintings.mod.Paintings;

public class EventFixBoundingBox {

    
    public EventFixBoundingBox() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    //quick hook to fix paintings not having the correct boundingbox when reloading a world, and thus overlapping with other newly placed paintings
    @SubscribeEvent
    public void spawnEvent(EntityJoinWorldEvent event) {
        
        if(event.getEntity() instanceof Painting)
        {
            Painting painting = (Painting) event.getEntity();
            Paintings.UTILITY.updatePaintingBoundingBox(painting);
        }
    }
}
