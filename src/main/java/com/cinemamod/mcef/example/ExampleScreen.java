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

package com.cinemamod.mcef.example;

import com.cinemamod.mcef.MCEF;
import com.cinemamod.mcef.MCEFBrowser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
//? > 1.21.8 {
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
//?}
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.awt.*;
import java.util.UUID;

public class ExampleScreen extends Screen {
    private static final int BROWSER_DRAW_OFFSET = 20;

    //? > 1.21.5 {
    protected Identifier exampleLocation;
    protected ExampleTexture exampleTexture;
    //?}
    private MCEFBrowser browser;

    public ExampleScreen(Component component) {
        super(component);
    }

    @Override
    protected void init() {
        super.init();

        //? > 1.21.5 {
        exampleLocation = Identifier.fromNamespaceAndPath(
                "example",
                "frame_" + UUID.randomUUID().toString().replace("-", ""));

        exampleTexture = new ExampleTexture(-1, this.exampleLocation.toString());
        //?}
        if (browser == null) {
            String url = "https://www.google.com";
            boolean transparent = true;
            browser = MCEF.createBrowser(url, transparent);
            resizeBrowser();
            //? > 1.21.5
            Minecraft.getInstance().getTextureManager().register(this.exampleLocation, this.exampleTexture);
        }
    }

    private int mouseX(double x) {
        return (int) ((x - BROWSER_DRAW_OFFSET) * minecraft.getWindow().getGuiScale());
    }

    private int mouseY(double y) {
        return (int) ((y - BROWSER_DRAW_OFFSET) * minecraft.getWindow().getGuiScale());
    }

    private int scaleX(double x) {
        return (int) ((x - BROWSER_DRAW_OFFSET * 2) * minecraft.getWindow().getGuiScale());
    }

    private int scaleY(double y) {
        return (int) ((y - BROWSER_DRAW_OFFSET * 2) * minecraft.getWindow().getGuiScale());
    }

    private void resizeBrowser() {
        if (width > 100 && height > 100) {
            browser.resize(scaleX(width), scaleY(height));
        }
    }

    //? > 1.21.10 {
    @Override
    public void resize(int i, int j) {
        super.resize(i, j);
        resizeBrowser();
    }
    //?} else {
    /*public void resize(Minecraft minecraft, int i, int j) {
        super.resize(minecraft, i, j);
        resizeBrowser();
    }
    *///?}

    @Override
    public void onClose() {
        browser.close();
        //? > 1.21.5
        Minecraft.getInstance().getTextureManager().release(exampleLocation);
        super.onClose();
    }

    //? > 1.21.5 {
    private void updateFrame() {
        this.exampleTexture.setId(this.browser.getRenderer().getTextureID());
        this.exampleTexture.setWidth(this.width);
        this.exampleTexture.setHeight(this.height);
    }
    //?}

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        //? if > 1.21.5 {
        this.updateFrame();
        guiGraphics.blit(
                //? > 1.21.5
                RenderPipelines.GUI_TEXTURED,
                //? = 1.21.5
                /*RenderType::guiTextured,*/
                this.exampleLocation,
                BROWSER_DRAW_OFFSET, BROWSER_DRAW_OFFSET,
                0, 0,
                width - BROWSER_DRAW_OFFSET * 2, height - BROWSER_DRAW_OFFSET * 2,
                width - BROWSER_DRAW_OFFSET * 2, height - BROWSER_DRAW_OFFSET * 2,
                Color.white.getRGB()
        );
        //?} else {
        /*// Check if the browser texture is ready for rendering
        if (browser != null && browser.isTextureReady()) {
            Identifier textureLocation = browser.getTextureLocation();
            int frameRenderWidth = width - BROWSER_DRAW_OFFSET * 2;
            int frameRenderHeight = height - BROWSER_DRAW_OFFSET * 2;
            guiGraphics.blit(RenderType::guiTextured, textureLocation, BROWSER_DRAW_OFFSET, BROWSER_DRAW_OFFSET, 0.0F, 0.0F, frameRenderWidth, frameRenderHeight, frameRenderWidth, frameRenderHeight);
        }
        *///?}
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        browser.sendMouseMove(mouseX(mouseX), mouseY(mouseY));
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        browser.sendMouseWheel(mouseX(mouseX), mouseY(mouseY), scrollY, 0);
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }


    //? if > 1.21.8 {
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        browser.sendMousePress(mouseX(event.x()), mouseY(event.y()), event.button());
        browser.setFocus(true);
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        browser.sendMouseRelease(mouseX(event.x()), mouseY(event.y()), event.button());
        browser.setFocus(true);
        return super.mouseReleased(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        browser.sendKeyPress(event.key(), event.scancode(), event.modifiers());
        browser.setFocus(true);
        return super.keyPressed(event);
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        browser.sendKeyRelease(event.key(), event.scancode(), event.modifiers());
        browser.setFocus(true);
        return super.keyReleased(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (event.codepoint() == (char) 0) {
            return false;
        }
        browser.sendKeyTyped(event.codepointAsString().charAt(0), event.modifiers());
        browser.setFocus(true);
        return super.charTyped(event);
    }
    //?} else {
    /*@Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        browser.sendMousePress(mouseX(mouseX), mouseY(mouseY), button);
        browser.setFocus(true);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        browser.sendMouseRelease(mouseX(mouseX), mouseY(mouseY), button);
        browser.setFocus(true);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        browser.sendKeyPress(keyCode, scanCode, modifiers);
        browser.setFocus(true);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        browser.sendKeyRelease(keyCode, scanCode, modifiers);
        browser.setFocus(true);
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (codePoint == (char) 0) {
            return false;
        }
        browser.sendKeyTyped(codePoint, modifiers);
        browser.setFocus(true);
        return super.charTyped(codePoint, modifiers);
    }
    *///?}
}
