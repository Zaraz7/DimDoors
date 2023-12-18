// Date: 2/4/2013 6:55:29 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX


package org.dimdev.dimdoors.client;


import org.dimdev.dimdoors.ticking.MobMonolith;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class ModelMobObelisk extends ModelBase {
    //fields
    ModelRenderer wholemonolith;
    Random rand = new Random();


    public ModelMobObelisk() {
        textureWidth = 256;
        textureHeight = 256;


        wholemonolith = new ModelRenderer(this, 0, 0);
        wholemonolith.addBox(-48 / 2F, -108F / 1.3F, -12 / 2F, 48, 108, 12);


    }

    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        this.setRotationAngles(0, 0, 0, 0, 0, 0, par1Entity);


        GL11.glScalef(((MobMonolith) par1Entity).getRenderSizeModifier(), ((MobMonolith) par1Entity).getRenderSizeModifier(), ((MobMonolith) par1Entity).getRenderSizeModifier());
        wholemonolith.render(par7);
    }
}
