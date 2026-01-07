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

package com.cinemamod.mcef;

import com.cinemamod.mcef.example.ExampleScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class FabricMCEFClientMod implements ClientModInitializer {


    public static final KeyMapping KEY_MAPPING = new KeyMapping(
            "Open Browser", InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F10,
            //? > 1.21.8 {
            KeyMapping.Category.CREATIVE
            //?} else
            /*"meow"*/
    );

    public FabricMCEFClientMod() {
        ClientTickEvents.START_CLIENT_TICK.register((client) -> onTick());
    }

    public void onTick() {
        // Check if our key was pressed
        if (KEY_MAPPING.isDown() && !(Minecraft.getInstance().screen instanceof ExampleScreen)) {
            //Display the web browser UI.
            Minecraft.getInstance().setScreen(new ExampleScreen(
                    Component.literal("Example Screen")
            ));
        }
    }

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(KEY_MAPPING);
    }
}
