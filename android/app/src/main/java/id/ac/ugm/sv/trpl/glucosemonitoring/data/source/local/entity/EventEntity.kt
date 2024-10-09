package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: Int,
    val type: String,
    val desc: String,
    val date: String,
    val time: String,
)