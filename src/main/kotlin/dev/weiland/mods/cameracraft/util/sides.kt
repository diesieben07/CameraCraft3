package dev.weiland.mods.cameracraft.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun World.isServer(): Boolean {
    contract {
        returns(true) implies (this@isServer is ServerWorld)
    }
    return this is ServerWorld
}

@OptIn(ExperimentalContracts::class)
fun PlayerEntity.isServer(): Boolean {
    contract {
        returns(true) implies (this@isServer is ServerPlayerEntity)
    }
    return this is ServerPlayerEntity
}