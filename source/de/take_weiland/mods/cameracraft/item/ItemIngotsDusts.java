package de.take_weiland.mods.cameracraft.item;

public class ItemIngotsDusts extends CCItemMultitype<IngotDustType> {

	public ItemIngotsDusts(int defaultId) {
		super("ingotsDusts", defaultId);
	}

	@Override
	public IngotDustType[] getTypes() {
		return IngotDustType.values();
	}

}
