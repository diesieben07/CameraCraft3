package dev.weiland.mods.cameracraft.storage

import com.google.common.collect.BoundType
import com.google.common.collect.Range
import com.google.common.collect.RangeSet
import com.google.common.collect.TreeRangeSet
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import java.io.Closeable
import java.nio.file.Path

@Suppress("UnstableApiUsage")
class PhotoDatabaseImpl(private val path: Path) : Closeable {

    private var nextId: Long = 0
    private val ids: RangeSet<Long> = TreeRangeSet.create()

    fun nextId(): Long {
        return nextId++
    }

    fun isIdTaken(id: Long): Boolean {
        return ids.contains(id)
    }

    fun writeToNbt(): CompoundNBT {
        val nbt = CompoundNBT()
        nbt.putLong("nextId", nextId)

        val taken = ListNBT()
        for (range in ids.asRanges()) {
            val rangeNbt = CompoundNBT()
            rangeNbt.putLong("lo", range.lowerEndpoint())
            rangeNbt.putString("lt", range.lowerBoundType().id())
            rangeNbt.putLong("up", range.upperEndpoint())
            rangeNbt.putString("ut", range.upperBoundType().id())
            taken.add(rangeNbt)
        }

        nbt.put("taken", taken)

        return nbt
    }

    fun readNbt(nbt: CompoundNBT) {
        nextId = nbt.getLong("nextId")
    }

    private fun BoundType.id(): String {
        return when (this) {
            BoundType.OPEN -> "open"
            BoundType.CLOSED -> "closed"
        }
    }

    private fun String.readBoundType(): BoundType {
        return when (this) {
            "open" -> BoundType.OPEN
            else -> BoundType.CLOSED
        }
    }

    override fun close() {
        TODO("Not yet implemented")
    }

}