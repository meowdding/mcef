package com.cinemamod.mcef.utils;

import net.minecraft.client.Minecraft;

public final class MultiVersionHelper {

    private MultiVersionHelper() {}
    public static long handle() {
        //? if > 1.21.8 {
        return Minecraft.getInstance().getWindow().handle();
        //?} else
        /*return Minecraft.getInstance().getWindow().getWindow();*/
    }

}
