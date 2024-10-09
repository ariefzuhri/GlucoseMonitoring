package id.ac.ugm.sv.trpl.glucosemonitoring.data.common.wrapper

sealed class ApiResponse<out T> {
    
    data class Success<out T>(val data: T) : ApiResponse<T>()
    
    object Empty : ApiResponse<Nothing>()
    
    data class Failed(val message: String) : ApiResponse<Nothing>()
    
}