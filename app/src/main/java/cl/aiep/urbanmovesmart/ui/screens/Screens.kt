package cl.aiep.urbanmovesmart.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.aiep.urbanmovesmart.model.*
import cl.aiep.urbanmovesmart.ui.*
import cl.aiep.urbanmovesmart.ui.theme.*

private val horizontal = 18.dp

@Composable
fun MapScreen(onVehicle: (Vehicle) -> Unit) {
    var type by remember { mutableStateOf<VehicleType?>(null) }
    val shown = DemoData.vehicles.filter { type == null || it.type == type }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = horizontal),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Spacer(Modifier.height(4.dp))
        HeroCard("Muévete mejor por la ciudad", "Encuentra tu próximo viaje en segundos.")
        Row(verticalAlignment = Alignment.CenterVertically) {
            SectionLabel("Vehículos cercanos", "Centro de Santiago · ubicación ilustrativa", Modifier.weight(1f))
            DemoPill()
        }
        ConceptMap(onVehicle, Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected=type==null,onClick={type=null},label={Text("Todos")})
            FilterChip(selected=type==VehicleType.SCOOTER,onClick={type=VehicleType.SCOOTER},label={Text("Scooters")})
            FilterChip(selected=type==VehicleType.BIKE,onClick={type=VehicleType.BIKE},label={Text("Bicis")})
        }
        SectionLabel("Elige tu transporte", "Tarifas y disponibilidad ficticias")
        shown.forEach { VehicleCard(it) { onVehicle(it) } }
        Spacer(Modifier.height(15.dp))
    }
}

@Composable
fun VehicleScreen(vehicle: Vehicle, onBack: () -> Unit, onReserve: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=horizontal),
        verticalArrangement=Arrangement.spacedBy(16.dp)) {
        BackText(onBack)
        Surface(color=Mint,shape=RoundedCornerShape(26.dp),modifier=Modifier.fillMaxWidth().height(166.dp)) {
            Box(contentAlignment=Alignment.Center) {
                VehicleIcon(vehicle.type,Modifier.size(90.dp))
            }
        }
        Row(verticalAlignment=Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(vehicle.name,style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold)
                Text("${vehicle.type.title} · ${vehicle.id}",color=Muted)
            }
            DemoPill()
        }
        SoftCard {
            SectionLabel("Listo para moverte")
            LabelIcon(Icons.Default.Place, vehicle.address)
            HorizontalDivider()
            LabelIcon(Icons.Default.DirectionsWalk, "A ${vehicle.walkMinutes} min caminando")
            LabelIcon(Icons.Default.BatteryFull, "Batería ${vehicle.battery}% · dato simulado")
            if(vehicle.rangeKm>0) LabelIcon(Icons.Default.Route, "Autonomía estimada ${vehicle.rangeKm} km")
        }
        SoftCard {
            SectionLabel("Tarifa orientativa")
            PriceRow("Desbloqueo", DemoData.pesos(vehicle.unlockFee))
            PriceRow("Por minuto", DemoData.pesos(vehicle.minuteFee))
            HorizontalDivider()
            PriceRow("Ejemplo: 12 minutos",DemoData.pesos(DemoData.estimatedFare(vehicle)))
        }
        Text("El estado del vehículo, la distancia y la tarifa son ejemplos para evaluar el diseño.",
            style=MaterialTheme.typography.bodySmall,color=Muted)
        BigButton("Reservar vehículo",onReserve)
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun ReserveScreen(vehicle: Vehicle, onBack: () -> Unit, onConfirm: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=horizontal),
        verticalArrangement=Arrangement.spacedBy(16.dp)) {
        BackText(onBack)
        SectionLabel("Confirma tu reserva","Aseguraremos el vehículo en esta simulación.")
        HeroCard(vehicle.name,"${vehicle.id} · ${vehicle.address}")
        SoftCard {
            SectionLabel("Resumen")
            PriceRow("Caminar hasta el vehículo","${vehicle.walkMinutes} min")
            PriceRow("Batería","${vehicle.battery}%")
            PriceRow("Desbloqueo",DemoData.pesos(vehicle.unlockFee))
            PriceRow("Estimación 12 min",DemoData.pesos(DemoData.estimatedFare(vehicle)))
        }
        Surface(color=Mint,shape=RoundedCornerShape(18.dp)) {
            Text("Reserva de demostración. No se enviará ninguna solicitud ni se bloqueará un vehículo real.",
                Modifier.padding(16.dp),color=Ink,style=MaterialTheme.typography.bodySmall)
        }
        BigButton("Confirmar reserva simulada",onConfirm)
    }
}

@Composable
fun RideScreen(vehicle: Vehicle, onSupport: () -> Unit,onFinish: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=horizontal),
        verticalArrangement=Arrangement.spacedBy(18.dp)) {
        Spacer(Modifier.height(8.dp))
        HeroCard("Trayecto en curso", "Recorrido simulado · sin geolocalización")
        ConceptMap(onVehicle = {}, modifier=Modifier.fillMaxWidth())
        SoftCard {
            SectionLabel("Tu viaje",vehicle.name)
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceEvenly) {
                Metric("12 min","Duración")
                Metric("3,4 km","Distancia")
                Metric("${vehicle.battery}%","Batería")
            }
            HorizontalDivider()
            PriceRow("Total estimado",DemoData.pesos(DemoData.estimatedFare(vehicle)))
        }
        OutlinedButton(onClick=onSupport,modifier=Modifier.fillMaxWidth().height(48.dp)) {
            Icon(Icons.Default.HelpOutline, null)
            Spacer(Modifier.width(8.dp))
            Text("Solicitar ayuda")
        }
        BigButton("Finalizar trayecto simulado",onFinish)
    }
}

@Composable
fun PaymentScreen(vehicle: Vehicle, onPay: () -> Unit, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=horizontal),
        verticalArrangement=Arrangement.spacedBy(16.dp)) {
        BackText(onBack)
        SectionLabel("Resumen de pago", "Se mostrará una confirmación ficticia.")
        HeroCard("Viaje finalizado","${vehicle.name} · 12 min · 3,4 km")
        SoftCard {
            SectionLabel("Detalle de tarifa")
            PriceRow("Desbloqueo",DemoData.pesos(vehicle.unlockFee))
            PriceRow("Uso (12 minutos)",DemoData.pesos(vehicle.minuteFee*12))
            HorizontalDivider()
            PriceRow("Total estimado",DemoData.pesos(DemoData.estimatedFare(vehicle)))
        }
        SoftCard {
            Row(verticalAlignment=Alignment.CenterVertically) {
                Icon(Icons.Default.CreditCard,null,tint=Teal)
                Spacer(Modifier.width(10.dp))
                Column { Text("Tarjeta de ejemplo",fontWeight=FontWeight.Bold);Text("•••• 4242 · sin cobro",color=Muted,fontSize=12.sp) }
            }
        }
        Text("No se solicitan datos bancarios ni se procesa ningún pago real.",color=Muted,
            style=MaterialTheme.typography.bodySmall)
        BigButton("Confirmar pago simulado",onPay)
    }
}

@Composable
fun ReceiptScreen(vehicle: Vehicle, onHistory: () -> Unit, onMap: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal=horizontal),
        verticalArrangement=Arrangement.Center,horizontalAlignment=Alignment.CenterHorizontally) {
        Surface(color=Mint,shape=RoundedCornerShape(26.dp),modifier=Modifier.size(95.dp)) {
            Box(contentAlignment=Alignment.Center) {
                Icon(Icons.Default.CheckCircle,"Viaje finalizado",tint=Teal,modifier=Modifier.size(52.dp))
            }
        }
        Spacer(Modifier.height(20.dp))
        Text("¡Viaje completado!",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Así se vería tu comprobante al completar un viaje.",color=Muted,textAlign=TextAlign.Center)
        Spacer(Modifier.height(26.dp))
        SoftCard(Modifier.fillMaxWidth()) {
            PriceRow("Vehículo",vehicle.name)
            PriceRow("Tiempo","12 min")
            PriceRow("Total simulado",DemoData.pesos(DemoData.estimatedFare(vehicle)))
            PriceRow("Estado","Demostración")
        }
        Spacer(Modifier.height(20.dp))
        BigButton("Ver historial",onHistory)
        TextButton(onClick=onMap) { Text("Volver al mapa") }
    }
}

@Composable
fun HistoryScreen(vehicle: Vehicle?, onNewTrip: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=horizontal),
        verticalArrangement=Arrangement.spacedBy(18.dp)) {
        Spacer(Modifier.height(7.dp))
        SectionLabel("Mis viajes","Un recorrido claro de tu actividad")
        if(vehicle==null) {
            SoftCard {
                Icon(Icons.Default.History,null,tint=Teal,modifier=Modifier.size(36.dp))
                Text("Todavía no hay viajes",style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Bold)
                Text("Completa un trayecto de prueba para ver su comprobante aquí.",color=Muted)
            }
        } else {
            SoftCard {
                Row(verticalAlignment=Alignment.CenterVertically) {
                    VehicleIcon(vehicle.type)
                    Spacer(Modifier.width(12.dp))
                    Column { Text(vehicle.name,fontWeight=FontWeight.Bold);Text("Viaje de demostración",color=Muted,fontSize=12.sp) }
                }
                HorizontalDivider()
                PriceRow("Duración","12 minutos")
                PriceRow("Distancia","3,4 km")
                PriceRow("Monto",DemoData.pesos(DemoData.estimatedFare(vehicle)))
                LabelIcon(Icons.Default.CheckCircle,"Finalizado · pago simulado")
            }
        }
        BigButton("Explorar vehículos",onNewTrip)
    }
}

@Composable
fun SupportScreen(onBack: () -> Unit, onReport: (String) -> Unit) {
    var selected by remember { mutableStateOf("Vehículo dañado") }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=horizontal),
        verticalArrangement=Arrangement.spacedBy(17.dp)) {
        BackText(onBack)
        SectionLabel("Centro de ayuda", "Asistencia disponible en esta simulación")
        HeroCard("Estamos contigo","Accede rápidamente a respuestas y reporta incidencias.")
        SoftCard {
            SectionLabel("Preguntas frecuentes")
            Text("¿Cómo se inicia un viaje?",fontWeight=FontWeight.Bold)
            Text("Selecciona una unidad, revisa sus datos y confirma la reserva.",color=Muted)
            HorizontalDivider()
            Text("¿Qué ocurre si la batería es baja?",fontWeight=FontWeight.Bold)
            Text("La propuesta prioriza recomendaciones de unidades con autonomía suficiente.",color=Muted)
        }
        SoftCard {
            SectionLabel("Reportar un problema", "Selecciona una categoría de incidencia.")
            listOf("Vehículo dañado","Problema de bloqueo","Problema de cobro").forEach { reason ->
                Row(verticalAlignment=Alignment.CenterVertically,modifier=Modifier.fillMaxWidth()) {
                    RadioButton(selected=selected==reason,onClick={selected=reason})
                    Text(reason)
                }
            }
            BigButton("Enviar reporte simulado",{onReport(selected)})
        }
        Text("No se envían tickets a un equipo de soporte real.",color=Muted,style=MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun ProfileScreen(user: String?, onLogin: (String) -> Unit, onLogout: () -> Unit) {
    var input by remember { mutableStateOf("Usuario Demo") }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=horizontal),
        verticalArrangement=Arrangement.spacedBy(18.dp)) {
        Spacer(Modifier.height(7.dp))
        SectionLabel("Mi perfil","Controla tu experiencia de demostración")
        HeroCard(if(user!=null) "Hola, $user" else "Bienvenido a UrbanMove","Tu movilidad empieza aquí.")
        if(user==null) {
            SoftCard {
                SectionLabel("Acceder al prototipo")
                OutlinedTextField(input,{input=it},label={Text("Nombre de demostración")},singleLine=true,
                    modifier=Modifier.fillMaxWidth())
                BigButton("Continuar como usuario demo",{onLogin(input.trim())},enabled=input.isNotBlank())
                Text("Puedes explorar y reservar sin iniciar sesión. Este formulario no crea cuentas.",
                    color=Muted,style=MaterialTheme.typography.bodySmall)
            }
        } else {
            SoftCard {
                LabelIcon(Icons.Default.CheckCircle,"Sesión ficticia activa")
                PriceRow("Usuario",user)
                OutlinedButton(onClick=onLogout,modifier=Modifier.fillMaxWidth()) { Text("Salir del modo demo") }
            }
        }
        SoftCard {
            SectionLabel("Privacidad y seguridad")
            Text("El prototipo no solicita ubicación precisa, datos bancarios ni información personal real.",color=Muted)
            LabelIcon(Icons.Default.Info,"Experiencia sin cuentas ni servidores")
        }
    }
}

@Composable
fun BackText(onBack: () -> Unit) {
    TextButton(onClick=onBack,contentPadding=PaddingValues(0.dp)) {
        Icon(Icons.Default.ArrowBack, null,modifier=Modifier.size(18.dp))
        Spacer(Modifier.width(7.dp))
        Text("Volver")
    }
}

@Composable
private fun PriceRow(title:String,value:String) {
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,
        verticalAlignment=Alignment.CenterVertically) {
        Text(title,color=Muted,style=MaterialTheme.typography.bodyMedium,modifier=Modifier.weight(1f))
        Spacer(Modifier.width(12.dp))
        Text(value,color=Ink,fontWeight=FontWeight.Bold,style=MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun Metric(value:String,label:String) {
    Column(horizontalAlignment=Alignment.CenterHorizontally) {
        Text(value,color=Teal,fontWeight=FontWeight.Bold,style=MaterialTheme.typography.titleLarge)
        Text(label,color=Muted,fontSize=11.sp)
    }
}
