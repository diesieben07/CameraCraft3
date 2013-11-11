package de.take_weiland.mods.cameracraft.item;

public class CCItemMisc extends CCItemMultitype<MiscItemType> {

	public CCItemMisc(int defaultId) {
		super("misc", defaultId);
	}

	@Override
	public MiscItemType[] getTypes() {
		return MiscItemType.values();
	}

}
