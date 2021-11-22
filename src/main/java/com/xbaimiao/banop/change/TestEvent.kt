package com.xbaimiao.banop.change

import com.xbaimiao.banop.scan.BukkitEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * @Author xbaimiao
 * @Date 2021/11/22 21:17
 */
@BukkitEvent
object TestEvent : Listener {

    @EventHandler
    fun a(event: PlayerJoinEvent) {
        println("${event.player.name}加入了服务器")
    }

}