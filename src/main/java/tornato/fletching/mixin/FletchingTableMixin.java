package tornato.fletching.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tornato.fletching.FletchingScreenHandler;

@Mixin(FletchingTableBlock.class)
public class FletchingTableMixin {
    @Inject(cancellable = true, method = "onUse", at = @At("HEAD"))
    private void awd(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (world.isClient) {
            cir.setReturnValue(ActionResult.SUCCESS);
        } else {
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inventory, _player) -> new FletchingScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), Text.literal("Fletching Table")
            ));
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }
}
