package de.take_weiland.mods.cameracraft.api.photo;

import net.minecraft.item.ItemStack;

/**
 * <p>An Item that stores photos.</p>
 * <p>For all methods with an ItemStack parameter, the stack must be of this Item, otherwise the results are undefined.</p>
 */
public interface PhotoStorageItem {

    /**
     * <p>Create a {@code PhotoStorage} pointing to the photos stored in the given ItemStack.</p>
     * @param stack the ItemStack
     * @return a PhotoStorage
     */
	PhotoStorage getPhotoStorage(ItemStack stack);

    /**
     * <p>Whether the given ItemStack is sealed. The result is equivalent to {@code getPhotoStorage(stack).isSealed()}.</p>
     * @param stack the ItemStack
     * @return whether the given ItemStack is sealed
     */
	boolean isSealed(ItemStack stack);

    /**
     * <p>Forcibly unseal the given ItemStack. This operation may produce a new ItemStack or modify the existing one.</p>
     * <p>This operation may not be supported by all Items.</p>
     * @param sealed the sealed ItemStack
     * @return a possibly new ItemStack
     */
	ItemStack unseal(ItemStack sealed);
	
	/**
	 * <p>Whether the given ItemStack can be rewound (usually films).</p>
	 * @param stack the ItemStack
	 * @return true iif the given ItemStack can be rewound
	 */
	boolean canRewind(ItemStack stack);
	
	/**
     * <p>Rewind the given ItemStack. This method does nothing if the operation is not supported or the ItemStack is already
     * rewound.</p>
	 * @param stack the ItemStack to rewind
	 * @return a possibly new ItemStack
	 */
	ItemStack rewind(ItemStack stack);
	
	/**
     * <p>Whether the given ItemStack can be processed in a PhotoProcessor.</p>
	 * @param stack the ItemStack
	 * @return whether this PhotoStorage can be processed in a PhotoProcessor
	 */
	boolean canBeProcessed(ItemStack stack);
	
	/**
	 * <p>Process the given ItemStack in a PhotoProcessor.</p>
	 * @param stack the ItemStack
	 * @return a possibly new ItemStack
	 */
	ItemStack process(ItemStack stack);

	/**
     * <p>Whether the given ItemStack can be scanned by a Scanner.</p>
	 * @return whether the ItemStack can be scanned in a Scanner
	 */
	boolean canBeScanned(ItemStack stack);

    /**
     * <p>Whether the given ItemStack's photo storage is random access, meaning it can be read and written freely.</p>
     * @param stack the ItemStack
     * @return whether the ItemStack is random access
     */
	boolean isRandomAccess(ItemStack stack);
	
}
