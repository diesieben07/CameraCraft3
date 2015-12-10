package de.take_weiland.mods.cameracraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.take_weiland.mods.commons.client.icon.IconManager;
import de.take_weiland.mods.commons.client.icon.IconManagerBuilder;
import de.take_weiland.mods.commons.client.icon.Icons;
import de.take_weiland.mods.commons.client.icon.RotatedDirection;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import static net.minecraftforge.common.util.ForgeDirection.NORTH;

/**
 * @author diesieben07
 */
public abstract class CCRotatedBlock extends CCBlock {

    public IIcon iconTop;
    protected IconManager icons;

    public CCRotatedBlock(String name, Material material) {
        super(name, material);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, placer, stack);
        world.setBlockMetadataWithNotify(x, y, z, icons.getMeta(placer), 3);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return icons.getIcon(side, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        icons = configureIcons(Icons.newBuilder(reg, this), reg).build();
    }

    public RotatedDirection getFront(int meta) {
        return icons.getFront(meta);
    }

    protected IconManagerBuilder configureIcons(IconManagerBuilder builder, IIconRegister register) {
        iconTop = register.registerIcon("cameracraft:" + textureTop());

        return builder
                .addCardinalDirections()
                .textureTopBottom(iconTop)
                .textureSides(textureSides())
                .texture(textureFront(), NORTH);

    }

    protected String textureFront() {
        return getBaseName() + ".front";
    }

    protected String textureSides() {
        return getBaseName() + ".sides";
    }

    protected String textureTop() {
        return getBaseName() + ".up";
    }

    protected String textureBottom() {
        return getBaseName() + ".bottom";
    }

}
