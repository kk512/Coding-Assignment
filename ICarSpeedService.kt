internal interface ICarSpeedService {
    fun setSpeedLimit(customerId: String?, speedLimit: Int)
    fun updateCurrentSpeed(customerId: String?, speed: Int)
}