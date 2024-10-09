package id.ac.ugm.sv.trpl.glucosemonitoring.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "glucoses")
data class GlucoseEntity(
    @PrimaryKey val id: Int,
    val date: String,
    val time: String,
    val level: Float,
)