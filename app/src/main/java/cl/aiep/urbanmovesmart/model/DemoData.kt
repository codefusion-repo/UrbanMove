package cl.aiep.urbanmovesmart.model

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

enum class VehicleType(val title: String) {
    BIKE("Bicicleta"), SCOOTER("Scooter"), EBIKE("Bici eléctrica")
}

data class Vehicle(
    val id: String, val name: String, val type: VehicleType,
    val battery: Int, val rangeKm: Int, val walkMinutes: Int,
    val address: String, val unlockFee: Int, val minuteFee: Int
)

object DemoData {
    val vehicles = listOf(
        Vehicle("UM-204", "Scooter Wave", VehicleType.SCOOTER, 86, 24, 3, "Plaza de Armas · sector A", 700, 210),
        Vehicle("UM-118", "Bicicleta City", VehicleType.BIKE, 100, 0, 5, "Paseo Ahumada · sector B", 500, 130),
        Vehicle("UM-352", "Bici Volt", VehicleType.EBIKE, 64, 31, 7, "Parque Forestal · sector C", 900, 250)
    )
    const val SIMULATED_MINUTES = 12
    fun estimatedFare(vehicle: Vehicle, minutes: Int = SIMULATED_MINUTES): Int =
        vehicle.unlockFee + vehicle.minuteFee * minutes
    fun pesos(value: Int): String {
        val sym = DecimalFormatSymbols(Locale.forLanguageTag("es-CL")).apply {
            groupingSeparator = '.'; decimalSeparator = ','
        }
        return "$" + DecimalFormat("#,##0", sym).format(value)
    }
}
