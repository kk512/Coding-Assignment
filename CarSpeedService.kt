import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import java.util.concurrent.ConcurrentHashMap

class CarSpeedService : Service() {

    private val speedLimits = ConcurrentHashMap<String, Int>()

    private val binder = object : ICarSpeedService.Stub() {

        override fun setSpeedLimit(customerId: String?, speedLimit: Int) {
            customerId?.let {
                speedLimits[it] = speedLimit
                Log.d("CarSpeedService", "Set limit for $customerId: $speedLimit km/h")
            }
        }

        override fun updateCurrentSpeed(customerId: String?, speed: Int) {
            customerId?.let {
                val limit = speedLimits[it] ?: 100
                if (speed > limit) {
                    sendFirebaseNotification(it, speed)
                    Log.w("CarSpeedService", "Speed exceeded: $it at $speed km/h")
                } else {
                    Log.i("CarSpeedService", "$it is within the limit: $speed km/h")
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder = binder

    private fun sendFirebaseNotification(customerId: String, speed: Int) {
        val topic = "fleet_notifications"

        val message = RemoteMessage.Builder("$topic@fcm.googleapis.com")
            .setMessageId(System.currentTimeMillis().toString())
            .addData("title", "Speed Alert")
            .addData("body", "$customerId exceeded speed limit at $speed km/h")
            .build()

        // For demo: real FCM sending requires server-side (use Admin SDK or HTTP call)
        Log.d("FCM-Send", "Simulating: $message")
        // Note: Sending Firebase message should be done from backend using server key
    }
}