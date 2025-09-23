package tornato.fletching;

import net.fabricmc.api.ModInitializer;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class Fletching implements ModInitializer {
    public static final ScreenHandlerType<FletchingScreenHandler> FLETCHING_SCREEN = Registry.register(Registries.SCREEN_HANDLER, id("fletching"), new ScreenHandlerType<>(FletchingScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final Item ARROW_ITEM = Items.register(id("arrow"), new FletchedArrowItem(new Item.Settings()));
    public static final ComponentType<ArrowComponent> ARROW_COMPONENT = Registry.register(Registries.DATA_COMPONENT_TYPE, id("arrow"),
            ComponentType.<ArrowComponent>builder().codec(ArrowComponent.CODEC).packetCodec(ArrowComponent.PACKET_CODEC).build());
    public static final EntityType<FletchedArrowEntity> ARROW_ENTITY = Registry.register(Registries.ENTITY_TYPE, id("arrow"), EntityType.Builder.<FletchedArrowEntity>create(FletchedArrowEntity::new, SpawnGroup.MISC).build("fletching:arrow"));

    public static Identifier id(String path) {
        return Identifier.of("fletching", path);
    }

    @Override
    public void onInitialize() {
        ExpandingTooltip.INSTANCE = () -> false;
    }
}
