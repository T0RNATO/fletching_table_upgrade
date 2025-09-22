package tornato.fletching;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
    public static final ArrowComponent DEFAULT = new ArrowComponent(Items.FEATHER.getRegistryEntry(), Items.STICK.getRegistryEntry(), Items.FLINT.getRegistryEntry(), Optional.empty(), Optional.empty());

    private static void line(Consumer<Text> tooltip, String label, String translationKey) {
        tooltip.accept(Text.translatable(label).append(Text.translatable(translationKey).withColor(Colors.LIGHT_GRAY)));
    }
    private static void item(Consumer<Text> tooltip, String labelTranslation, RegistryEntry<Item> entry) {
        var key = entry.value().getTranslationKey();
        line(tooltip, labelTranslation, key);

        if (ExpandingTooltip.INSTANCE.isHoldingShift()) {
            tooltip.accept(Text.translatable("fletching.tooltip." + key).withColor(Colors.YELLOW));
        }
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        item(tooltip, "fletching.tooltip.fletching", fletching);
        item(tooltip, "fletching.tooltip.shaft", shaft);
        item(tooltip, "fletching.tooltip.point", point);

        effect_item.ifPresent(effect -> item(tooltip, "fletching.tooltip.effect", effect));
        effect.ifPresent(potion -> {
            line(tooltip, "fletching.tooltip.effect", Potion.finishTranslationKey(effect, "item.minecraft.potion.effect."));
            if (ExpandingTooltip.INSTANCE.isHoldingShift()) {
                tooltip.accept(Text.translatable("fletching.tooltip.effect.expanded").withColor(Colors.YELLOW));
            }
        });

        if (!ExpandingTooltip.INSTANCE.isHoldingShift()) {
            tooltip.accept(Text.literal("[Hold Shift to expand]").withColor(Colors.LIGHT_GRAY));
        }
    }

    public boolean noGrav() { return this.shaft.value().equals(Items.BREEZE_ROD); }
    public boolean onFire() { return this.shaft.value().equals(Items.BLAZE_ROD); }

    public float velocityMultiplier() { return this.fletching.value().equals(Items.PHANTOM_MEMBRANE) ? 1.75f : 1; }

    public boolean noWaterPenalty() { return this.point.value().equals(Items.PRISMARINE_SHARD); }
    public boolean appliesDarkness() { return this.point.value().equals(Items.ECHO_SHARD); }
    public boolean shattersOnHit() { return this.point.value().equals(Items.AMETHYST_SHARD); }

    public boolean explodesOnHit() { return this.effect_item.isPresent() && this.effect_item.get().equals(Items.GUNPOWDER); }
    public boolean spectral() { return this.effect_item.isPresent() && this.effect_item.get().equals(Items.GLOWSTONE_DUST); }
    public boolean bouncy() { return this.effect_item.isPresent() && this.effect_item.get().equals(Items.SLIME_BALL); }
}
