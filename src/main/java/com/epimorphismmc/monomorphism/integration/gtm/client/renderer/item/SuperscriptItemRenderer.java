package com.epimorphismmc.monomorphism.integration.gtm.client.renderer.item;

import com.epimorphismmc.monomorphism.MonoLib;
import com.epimorphismmc.monomorphism.client.utils.MORenderUtils;
import com.epimorphismmc.monomorphism.integration.gtm.item.IMOItemRendererProvider;
import com.epimorphismmc.monomorphism.integration.gtm.item.component.INumberSuperscriptEffect;
import com.epimorphismmc.monomorphism.integration.gtm.item.component.IVoltageSuperscriptEffect;

import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.Locale;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.VN;

public class SuperscriptItemRenderer extends WrappedItemRenderer {
    private final Int2ObjectOpenHashMap<ResourceLocation> voltageTextures =
            new Int2ObjectOpenHashMap<>();
    private final Int2ObjectOpenHashMap<ResourceLocation> numberTextures =
            new Int2ObjectOpenHashMap<>();
    private final Int2ObjectOpenHashMap<ResourceLocation> romaNumberTextures =
            new Int2ObjectOpenHashMap<>();

    public SuperscriptItemRenderer() {
        if (Platform.isClient()) {
            registerEvent();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderItem(
            ItemStack stack,
            ItemDisplayContext transformType,
            boolean leftHand,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int combinedLight,
            int combinedOverlay,
            BakedModel model) {
        model = getVanillaModel(stack, null, null);
        var texture = getTexture(stack);
        if (transformType == ItemDisplayContext.GUI && texture != null) {

            Tesselator tess = Tesselator.getInstance();

            var buffers = MultiBufferSource.immediate(tess.getBuilder());
            vanillaRender(
                    stack,
                    transformType,
                    leftHand,
                    poseStack,
                    buffers,
                    combinedLight,
                    combinedOverlay,
                    model);
            buffers.endBatch();

            BufferBuilder buf = tess.getBuilder();
            buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            poseStack.pushPose();
            poseStack.translate(-0.5F, -0.5F, -0.5F);

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            TextureAtlasSprite sprite = ModelFactory.getBlockSprite(texture);
            MORenderUtils.bindBlockAtlas();

            float minU = sprite.getU0();
            float maxU = sprite.getU1();
            float minV = sprite.getV0();
            float maxV = sprite.getV1();

            Matrix4f pos = poseStack.last().pose();
            buf.vertex(pos, 1, 1, 0).uv(maxU, minV).endVertex();
            buf.vertex(pos, 0, 1, 0).uv(minU, minV).endVertex();
            buf.vertex(pos, 0, 0, 0).uv(minU, maxV).endVertex();
            buf.vertex(pos, 1, 0, 0).uv(maxU, maxV).endVertex();
            tess.end();

            poseStack.popPose();
        } else {
            vanillaRender(
                    stack, transformType, leftHand, poseStack, buffer, combinedLight, combinedOverlay, model);
        }
    }

    @Nullable private ResourceLocation getTexture(ItemStack stack) {
        if (stack.getItem() instanceof IMOItemRendererProvider provider) {
            var info = provider.getRenderInfo(stack);
            if (info instanceof INumberSuperscriptEffect effect) {
                return effect.isRoma()
                        ? romaNumberTextures.get(effect.tier())
                        : numberTextures.get(effect.tier());
            }

            if (info instanceof IVoltageSuperscriptEffect effect) {
                return voltageTextures.get(effect.tier());
            }
        }
        return null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onPrepareTextureAtlas(
            ResourceLocation atlasName, Consumer<ResourceLocation> register) {
        if (atlasName.equals(InventoryMenu.BLOCK_ATLAS)) {
            for (int i = 0; i < 15; i++) {
                var voltage =
                        MonoLib.id("superscript/voltage/%s".formatted(VN[i].toLowerCase(Locale.ROOT)));
                register.accept(voltage);
                voltageTextures.put(i, voltage);

                var number = MonoLib.id("superscript/number/%s".formatted(i + 1));
                register.accept(number);
                numberTextures.put(i + 1, number);

                var romaNumber = MonoLib.id("superscript/number/roman/%s".formatted(i + 1));
                register.accept(romaNumber);
                romaNumberTextures.put(i + 1, romaNumber);
            }
        }
    }
}
