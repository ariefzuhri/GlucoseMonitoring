package id.ac.ugm.sv.trpl.glucosemonitoring.domain.common.wrapper

sealed class Result<out T : Any> {
    
    object Standby : Result<Nothing>()
    
    object Loading : Result<Nothing>()
    
    data class Success<out T : Any>(val data: T? = null) : Result<T>()
    
    object Failed : Result<Nothing>()
    
}