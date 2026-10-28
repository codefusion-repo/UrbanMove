package cl.aiep.urbanmovesmart.model

import org.junit.Assert.assertEquals
import org.junit.Test

class DemoDataTest {
    @Test fun `tarifa estimada combina desbloqueo y minutos`() {
        val vehicle = DemoData.vehicles.first()
        assertEquals(3220, DemoData.estimatedFare(vehicle))
    }

    @Test fun `precios mostrados como pesos chilenos`() {
        assertEquals("\$3.220", DemoData.pesos(3220))
    }

    @Test fun `flota demostrativa tiene identificadores distintos`() {
        assertEquals(3, DemoData.vehicles.map { it.id }.distinct().size)
    }
}
