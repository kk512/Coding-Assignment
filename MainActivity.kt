import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var carSpeedService: ICarSpeedService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            carSpeedService = ICarSpeedService.Stub.asInterface(service)
            Log.d("MainActivity", "Service connected")

            carSpeedService?.apply {
                setSpeedLimit("customer1", 90)
                updateCurrentSpeed("customer1", 100) // Should trigger alert
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            carSpeedService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindService(Intent(this, CarSpeedService::class.java), connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}