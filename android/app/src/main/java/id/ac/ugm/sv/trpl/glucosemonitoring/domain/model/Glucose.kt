package id.ac.ugm.sv.trpl.glucosemonitoring.domain.model

data class Glucose(
    val id: Int,
    val level: Float,
    val date: String,
    val time: String,
) {
    
    val dateTime get() = "$date $time"
    
}