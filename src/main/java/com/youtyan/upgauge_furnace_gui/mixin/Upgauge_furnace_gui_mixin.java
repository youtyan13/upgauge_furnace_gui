package com.youtyan.upgauge_furnace_gui.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceScreen.class)
public abstract class Upgauge_furnace_gui_mixin extends AbstractContainerScreen<AbstractFurnaceMenu> {

    @Unique
    private static final ResourceLocation NEW_TEXTURE = new ResourceLocation("upgauge_furnace_gui", "textures/gui/container/upgauge_furnace.png");

    public Upgauge_furnace_gui_mixin(AbstractFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "renderBg", at = @At("HEAD"), cancellable = true)
    public void onRenderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        if (!((Object)this instanceof FurnaceScreen)) {
            return;
        }

        ci.cancel();

        int x = this.leftPos;
        int y = this.topPos;

        // 背景だよ
        guiGraphics.blit(NEW_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // ゲージの処理だよ
        int gaugeX = x + 112 - 5;
        int gaugeY = y + 31 - 5;
        int gaugeWidth = 34;
        int gaugeHeight = 34;

        int emptyU = 107, emptyV = 26;
        int fullU = 176, fullV = 14;

        guiGraphics.blit(NEW_TEXTURE, gaugeX, gaugeY, emptyU, emptyV, gaugeWidth, gaugeHeight);

        int progress = this.menu.getBurnProgress();
        float percent = (progress > 0) ? ((float) progress / 24.0f) : 0.0f;

        int fillHeight = (int) (gaugeHeight * percent);

        if (fillHeight > gaugeHeight) fillHeight = gaugeHeight;

        if (fillHeight > 0) {
            guiGraphics.blit(NEW_TEXTURE,
                    gaugeX, gaugeY + (gaugeHeight - fillHeight),
                    fullU, fullV + (gaugeHeight - fillHeight),
                    gaugeWidth, fillHeight
            );
        }
        if (this.menu.isLit()) {
            int burnProgress = this.menu.getLitProgress();
            int fireU = 176;
            int fireV = 0;
            int fireHeight = 13;

            guiGraphics.blit(NEW_TEXTURE,
                    x + 56, y + 36 + 12 - burnProgress,
                    fireU, fireV + 12 - burnProgress,
                    14, burnProgress + 1
            );
        }
    }
}