package tornato.fletching;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.ForgingSlotsManager;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FletchingScreenHandler extends ForgingScreenHandler {
    public FletchingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public static final Set<Item> FLETCHING = Set.of(
            Items.FEATHER, Items.PHANTOM_MEMBRANE
    );
    public static final Set<Item> SHAFTS = Set.of(
            Items.STICK, Items.BLAZE_ROD, Items.BREEZE_ROD
    );
    public static final Set<Item> POINTS = Set.of(
            Items.FLINT, Items.PRISMARINE_SHARD, Items.ECHO_SHARD, Items.AMETHYST_SHARD, Items.DIAMOND
    );
    public static final Set<Item> EFFECTS = Set.of(
            Items.SPLASH_POTION, Items.GUNPOWDER, Items.GLOWSTONE_DUST, Items.SLIME_BALL
    );

    public static final Map<Item, Integer> YIELD = Map.of(
            Items.DIAMOND, 12,
            Items.ECHO_SHARD, 32
    );

    public FletchingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(Fletching.FLETCHING_SCREEN, syncId, playerInventory, context);
//        this.world = playerInventory.player.getWorld();
//        this.recipes = this.world.getRecipeManager().listAllOfType(RecipeType.SMITHING);
    }

    @Override
    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return !this.output.isEmpty();
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        this.decrementSlot(0);
        this.decrementSlot(1);
        this.decrementSlot(2);
        this.decrementSlot(3);
    }

    private void decrementSlot(int slot) {
        var stack = this.input.getStack(slot);
        stack.decrement(1);
        this.input.setStack(slot, stack);
    }

    @Override
    protected boolean canUse(BlockState state) {
        return state.isOf(Blocks.FLETCHING_TABLE);
    }

    @Override
    public void updateResult() {
        var fletching = this.input.getStack(0);
        var shaft = this.input.getStack(1);
        var point = this.input.getStack(2);
        var effect_slot = this.input.getStack(3);

        if (fletching.isEmpty() || shaft.isEmpty() || point.isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
            return;
        }

        Optional<RegistryEntry<Potion>> effect = effect_slot.isOf(Items.SPLASH_POTION) ?
                effect_slot.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion():
                Optional.empty();

        Optional<RegistryEntry<Item>> effect_item = (!effect_slot.isEmpty() && !effect_slot.isOf(Items.SPLASH_POTION)) ?
                Optional.of(effect_slot.getItem().getRegistryEntry()):
                Optional.empty();

        var stack = new ItemStack(Fletching.ARROW_ITEM, YIELD.getOrDefault(point.getItem(), 6));
        stack.set(Fletching.ARROW_COMPONENT, new ArrowComponent(fletching.getRegistryEntry(), shaft.getRegistryEntry(), point.getRegistryEntry(), effect, effect_item));
        this.output.setStack(0, stack);
    }

    @Override
    protected ForgingSlotsManager getForgingSlotsManager() {
        return ForgingSlotsManager.create()
                .input(0, 35, 48, stack -> FLETCHING.contains(stack.getItem()))
                .input(1, 53, 48, stack -> SHAFTS   .contains(stack.getItem()))
                .input(2, 71, 48, stack -> POINTS   .contains(stack.getItem()))
                .input(3, 125, 7, stack -> EFFECTS  .contains(stack.getItem()))
                .output(4, 125, 48)
                .build();
    }
}
