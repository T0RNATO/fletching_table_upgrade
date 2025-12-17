package tornato.fletching.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import tornato.fletching.ExpandingTooltip;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public record ArrowComponent(RegistryEntry<Item> fletching, RegistryEntry<Item> shaft, RegistryEntry<Item> point, Optional<RegistryEntry<Potion>> effect, Optional<RegistryEntry<Item>> effect_item) implements TooltipAppender {
    public static final Codec<ArrowComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ItemStack.ITEM_CODEC.fieldOf("fletching")          .forGetter(ArrowComponent::fletching),
            ItemStack.ITEM_CODEC.fieldOf("shaft")              .forGetter(ArrowComponent::shaft),
            ItemStack.ITEM_CODEC.fieldOf("point")              .forGetter(ArrowComponent::point),
            Potion.CODEC.optionalFieldOf("effect")             .forGetter(ArrowComponent::effect),
            ItemStack.ITEM_CODEC.optionalFieldOf("effect_item").forGetter(ArrowComponent::effect_item)
    ).apply(builder, ArrowComponent::new));

    private static final PacketCodec<RegistryByteBuf, RegistryEntry<Item>> ITEM_PACKET_CODEC = PacketCodecs.registryEntry(RegistryKeys.ITEM);

    public static final PacketCodec<RegistryByteBuf, ArrowComponent> PACKET_CODEC = PacketCodec.tuple(
            ITEM_PACKET_CODEC,                                   ArrowComponent::fletching,
            ITEM_PACKET_CODEC,                                   ArrowComponent::shaft,
            ITEM_PACKET_CODEC,                                   ArrowComponent::point,
            Potion.PACKET_CODEC.collect(PacketCodecs::optional), ArrowComponent::effect,
            ITEM_PACKET_CODEC.collect(PacketCodecs::optional),   ArrowComponent::effect_item,
            ArrowComponent::new
    );

    public static final Map<Item, Integer> EFFECT_ITEM_TINT = Map.of(
            Items.GUNPOWDER,      0xff727272,
            Items.GLOWSTONE_DUST, 0xffffbc5e,
            Items.SLIME_BALL,     0xff8cd782
    );

    public static final ArrowComponent DEFAULT = new ArrowComponent(Items.FEATHER.getRegistryEntry(), Items.STICK.getRegistryEntry(), Items.FLINT.getRegistryEntry(), Optional.empty(), Optional.empty());

    private static void line(Consumer<Text> tooltip, String label, MutableText text) {
        tooltip.accept(Text.translatable(label).append(text.withColor(Colors.LIGHT_GRAY)));
    }
    private static void line(Consumer<Text> tooltip, String label, String translationKey) { line(tooltip, label, Text.translatable(translationKey)); }
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
            var effects = potion.value().getEffects();
            if (effects.size() == 1) {
                var effectInstance = effects.getFirst();
                var effectTranslation = Text.translatable(
                        effects.size() == 1 ?
                                effectInstance.getTranslationKey():
                                Potion.finishTranslationKey(effect, "item.minecraft.potion.effect.")
                );
                line(tooltip, "fletching.tooltip.effect",
                        Text.translatable("potion.withDuration",
                            effectInstance.getAmplifier() > 0 ?
                                    Text.translatable("potion.withAmplifier",
                                            effectTranslation,
                                            Text.translatable("potion.potency." + effectInstance.getAmplifier())
                                    ):
                                    effectTranslation,
                            StatusEffectUtil.getDurationText(effectInstance, 0.125F, context.getUpdateTickRate())
                        )
                );
            }
            if (ExpandingTooltip.INSTANCE.isHoldingShift()) {
                tooltip.accept(Text.translatable("fletching.tooltip.effect.expanded").withColor(Colors.YELLOW));
            }
        });

        if (!ExpandingTooltip.INSTANCE.isHoldingShift()) {
            tooltip.accept(Text.literal("[Hold Shift to expand]").withColor(Colors.LIGHT_GRAY));
        }
    }

    public float velocityMultiplier() {
        return this.fletching.value().equals(Items.PHANTOM_MEMBRANE) ? 1.75f : 1;
    }
    public float damageMultiplier() {
        return (this.point.value().equals(Items.DIAMOND) ? 1.75f : 1) / this.velocityMultiplier();
    }

    public String fletchingPath() { return "fletching/" + this.fletching().getKey().get().getValue().getPath(); }
    public String shaftPath() { return "shaft/" + this.shaft().getKey().get().getValue().getPath(); }
    public String pointPath() { return "point/" + this.point().getKey().get().getValue().getPath(); }

    public boolean noGrav() { return this.shaft.value().equals(Items.BREEZE_ROD); }
    public boolean onFire() { return this.shaft.value().equals(Items.BLAZE_ROD); }

    public boolean noWaterPenalty()  { return this.point.value().equals(Items.PRISMARINE_SHARD); }
    public boolean appliesDarkness() { return this.point.value().equals(Items.ECHO_SHARD); }
    public boolean shattersOnHit()   { return this.point.value().equals(Items.AMETHYST_SHARD); }

    public boolean explodesOnHit()   { return this.effect_item.isPresent() && this.effect_item.get().value().equals(Items.GUNPOWDER); }
    public boolean spectral()        { return this.effect_item.isPresent() && this.effect_item.get().value().equals(Items.GLOWSTONE_DUST); }
    public boolean bouncy()          { return this.effect_item.isPresent() && this.effect_item.get().value().equals(Items.SLIME_BALL); }
    public boolean hasEffect()       { return this.effect_item.isPresent() || this.effect.isPresent(); }

    public int getTint() {
        if (this.effect.isPresent()) {
            return PotionContentsComponent.getColor(this.effect.get());
        } else if (this.effect_item.isPresent()) {
            return EFFECT_ITEM_TINT.get(this.effect_item.get().value());
        }
        return -1;
    }
}
