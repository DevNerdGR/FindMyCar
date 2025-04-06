package gr.findmycar.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import java.time.LocalDateTime
import java.time.ZoneOffset

class ParkingLotViewModel : ViewModel() {
    private val database = Firebase.database("https://findmycar-f6eb9-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val parkingLot = mutableStateOf<ParkingLot?>(null)

    init {
        getParkingLotRealTime()
    }

    fun getParkingLot() {
        database.getReference("parkingLot")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.getValue<ParkingLot>()?.let {
                        parkingLot.value = it
                    }
                }
            }
    }

    fun getParkingLotRealTime() {
        database.getReference("parkingLot")
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.getValue<ParkingLot>()?.let {
                            parkingLot.value = it
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("ParkingLotViewModel", error.message)
                    }
                }
            )
    }

    fun updateParkingLot(lot : ParkingLot) {
        database.getReference("parkingLot")
            .updateChildren(lot.toJson())
    }
}


data class ParkingLot(
    val lotNumber: Int = 0,
    val location: String = "Space",
    val epochTimeStamp: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
    val user : String = ""
) {
    fun toJson() : Map<String, Any?>{
        return mapOf(
            "epochTimeStamp" to epochTimeStamp,
            "location" to location,
            "lotNumber" to lotNumber,
            "user" to user
        )
    }
}