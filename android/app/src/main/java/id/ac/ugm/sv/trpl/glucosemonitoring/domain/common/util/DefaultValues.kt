package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.util

sealed interface DefaultValues {
    
    val id: Int?
    val string: String?
    val float: Float?
    
}

object Default : DefaultValues {
    
    override val id = Int.INVALID
    override val string = String.EMPTY
    override val float = Float.ZERO
    
}