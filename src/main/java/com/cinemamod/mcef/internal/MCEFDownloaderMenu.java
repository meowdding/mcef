/*
 *     MCEF (Minecraft Chromium Embedded Framework)
 *     Copyright (C) 2023 CinemaMod Group
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 */

package com.cinemamod.mcef.internal;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MCEFDownloaderMenu extends Screen {
    private final Screen menu;

    public MCEFDownloaderMenu(Screen menu) {
        super(Component.literal("MCEF is downloading required libraries..."));
        this.menu = menu;
    }

    //? > 1.21.5 {
    @Override
    protected void renderBlurredBackground(GuiGraphics guiGraphics) {
        this.renderMenuBackground(guiGraphics);
    }
    //?} else {
    /*@Override
    protected void renderBlurredBackground() {
        super.renderBlurredBackground();
    }
    *///?}

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        double cx = width / 2d;
        double cy = height / 2d;

        double progressBarHeight = 14;
        double progressBarWidth = width / 3d; // TODO: base off screen with (1/3 of screen)

        var poseStack = graphics.pose();

        /* Draw Progress Bar */
        //? = 1.21.5 {
        /*poseStack.pushPose();
        poseStack.translate(cx, cy, 0);
        poseStack.translate(-progressBarWidth / 2d, -progressBarHeight / 2d, 0);
        graphics.fill( // bar border
                0, 0,
                (int) progressBarWidth,
                (int) progressBarHeight,
                -1
        );
        graphics.fill( // bar padding
                2, 2,
                (int) progressBarWidth - 2,
                (int) progressBarHeight - 2,
                -16777215
        );
        graphics.fill( // bar bar
                4, 4,
                (int) ((progressBarWidth - 4) * MCEFDownloadListener.INSTANCE.getProgress()),
                (int) progressBarHeight - 4,
                -1
        );
        poseStack.popPose();
        *///?} else {
        poseStack.pushMatrix();
        poseStack.translate((float) cx, (float) cy);
        poseStack.translate((float) (-progressBarWidth / 2d), (float) (-progressBarHeight / 2d));
        graphics.fill( // bar border
                0, 0,
                (int) progressBarWidth,
                (int) progressBarHeight,
                -1
        );
        graphics.fill( // bar padding
                2, 2,
                (int) progressBarWidth - 2,
                (int) progressBarHeight - 2,
                -16777215
        );
        graphics.fill( // bar bar
                4, 4,
                (int) ((progressBarWidth - 4) * MCEFDownloadListener.INSTANCE.getProgress()),
                (int) progressBarHeight - 4,
                -1
        );
        poseStack.popMatrix();
        //?}

        // putting this here incase I want to re-add a third line later on
        // allows me to generalize the code to not care about line count
        String[] text = new String[]{
                MCEFDownloadListener.INSTANCE.getTask(),
                Math.round(MCEFDownloadListener.INSTANCE.getProgress() * 100) + "%",
        };

        /* Draw Text */
        // calculate offset for the top line
        int oSet = ((font.lineHeight / 2) + ((font.lineHeight + 2) * (text.length + 2))) + 4;
        //? if > 1.21.5 {
        poseStack.pushMatrix();
        //?} else
        /*poseStack.pushPose();*/
        poseStack.translate(
                (int) (cx),
                (int) (cy - oSet)
                //? if < 1.21.6
                /*,0*/
        );
        // draw menu name
        graphics.drawString(
                font,
                ChatFormatting.GOLD + title.getString(),
                (int) -(font.width(title.getString()) / 2d), 0,
                0xFFFFFFFF
        );
        // draw text
        int index = 0;
        for (String s : text) {
            if (index == 1) {
                //? if > 1.21.5 {
                poseStack.translate(0, font.lineHeight + 2);
                //?} else
                /*poseStack.translate(0, font.lineHeight + 2, 0);*/
            }

            //? if > 1.21.5 {
            poseStack.translate(0, font.lineHeight + 2);
            //?} else
            /*poseStack.translate(0, font.lineHeight + 2, 0);*/
            graphics.drawString(
                    font,
                    s,
                    (int) -(font.width(s) / 2d), 0,
                    0xFFFFFFFF
            );
            index++;
        }
        //? if > 1.21.5 {
        poseStack.popMatrix();
        //?} else
        /*poseStack.popPose();*/

        // TODO: if listener.isFailed(), draw some "Failed to initialize MCEF" text with an "OK" button to proceed
    }

    @Override
    public void tick() {
        if (MCEFDownloadListener.INSTANCE.isDone() || MCEFDownloadListener.INSTANCE.isFailed()) {
            onClose();
            Minecraft.getInstance().setScreen(menu);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
