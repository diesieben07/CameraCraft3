package dev.weiland.mods.cameracraft.client

import net.minecraftforge.client.model.data.IModelData
import net.minecraftforge.client.model.data.ModelProperty

internal interface DummyModelData : IModelData {

    override fun hasProperty(prop: ModelProperty<*>?): Boolean = false

    override fun <T : Any?> getData(prop: ModelProperty<T>?): T? = null

    override fun <T : Any?> setData(prop: ModelProperty<T>?, data: T): T? {
        throw UnsupportedOperationException()
    }
}