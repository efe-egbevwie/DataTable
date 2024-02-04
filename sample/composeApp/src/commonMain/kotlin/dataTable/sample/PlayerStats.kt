package com.efe.dataTable.sample

data class PlayerStats(
    val playerId: Long,
    val playerName: String,
    val minutesPlayed: Int,
    val points: Int,
    val rebounds: Int,
    val assists: Int
)

