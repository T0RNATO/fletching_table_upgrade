package tornato.fletching;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.Optional;
import java.util.function.Consumer;

public record ArrowComponent(RegistryEntry<Item> fletching, RegistryEntry<Item> shaft, RegistryEntry<Item> point, Optional<RegistryEntry<Potion>> effect, Optional<RegistryEntry<Item>> effect_item) implements TooltipAppender {
    public static final Codec<ArrowComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ItemStack.ITEM_CODEC.fieldOf("fletching").forGetter(ArrowComponent::fletching),
            ItemStack.ITEM_CODEC.fieldOf("shaft").forGetter(ArrowComponent::shaft),
            ItemStack.ITEM_CODEC.fieldOf("point").forGetter(ArrowComponent::point),
            Potion.CODEC.optionalFieldOf("effect").forGetter(ArrowComponent::effect),
            ItemStack.ITEM_CODEC.optionalFieldOf("effect_item").forGetter(ArrowComponent::effect_item)
    ).apply(builder, ArrowComponent::new));

    private static Text line(String label, String translationKey) {
        return Text.literal(label).append(Text.translatable(translationKey).withColor(Colors.LIGHT_GRAY));
    }
    private static Text item(String label, RegistryEntry<Item> entry) {
        return line(label, entry.value().getTranslationKey());
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        tooltip.accept(item(" Fletching: ", fletching));
        tooltip.accept(item(" Shaft: ", shaft));
        tooltip.accept(item(" Point: ", point));
        effect.ifPresent(potion -> tooltip.accept(line(" Effect: ", Potion.finishTranslationKey(effect, "item.minecraft.potion.effect."))));
        effect_item.ifPresent(effect -> tooltip.accept(item(" Effect: ", effect)));
    }
}
