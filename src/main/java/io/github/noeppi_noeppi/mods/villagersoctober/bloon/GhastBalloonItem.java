package io.github.noeppi_noeppi.mods.villagersoctober.bloon;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.moddingx.libx.base.ItemBase;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.Registerable;
import org.moddingx.libx.registration.RegistrationContext;
import org.moddingx.libx.registration.SetupContext;

import javax.annotation.Nonnull;

public class GhastBalloonItem extends ItemBase implements Registerable {

    public final EntityType<GhastBalloon> entityType;
    
    public GhastBalloonItem(ModX mod, Properties properties) {
        super(mod, properties);
        
        this.entityType = EntityType.Builder.of(GhastBalloon::new, MobCategory.MISC)
                .sized(1.4f, 1.4f).build(mod.resource("ghast_balloon").toString());
    }
    
    @Override
    public void registerAdditional(RegistrationContext ctx, EntryCollector builder) {
        builder.register(Registry.ENTITY_TYPE_REGISTRY, entityType);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(SetupContext ctx) {
        ctx.enqueue(() -> EntityRenderers.register(this.entityType, GhastBalloonRenderer::new));
    }

    @Nonnull
    @Override
    public InteractionResult useOn(@Nonnull UseOnContext ctx) {
        if (ctx.getLevel().getBlockState(ctx.getClickedPos()).is(BlockTags.FENCES)) {
            if (ctx.getLevel().isEmptyBlock(ctx.getClickedPos().above(1)) && ctx.getLevel().isEmptyBlock(ctx.getClickedPos().above(2))) {
                if (!ctx.getLevel().isClientSide) {
                    GhastBalloon balloon = this.entityType.create(ctx.getLevel());
                    if (balloon == null) return InteractionResult.FAIL;
                    balloon.setPos(ctx.getClickedPos().getX() + 0.5, ctx.getClickedPos().getY() + 2.6, ctx.getClickedPos().getZ() + 0.5);
                    balloon.setLookAngleDeg(ctx.getLevel().random.nextFloat() * 360);
                    balloon.setTiePos(ctx.getClickedPos());
                    ctx.getLevel().addFreshEntity(balloon);
                    if (ctx.getPlayer() == null || !ctx.getPlayer().isCreative()) {
                        ctx.getItemInHand().shrink(1);
                    }
                }
                return InteractionResult.sidedSuccess(ctx.getLevel().isClientSide);
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.PASS;
        }
    }
}
