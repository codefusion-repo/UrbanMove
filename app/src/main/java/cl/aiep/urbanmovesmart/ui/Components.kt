package cl.aiep.urbanmovesmart.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.aiep.urbanmovesmart.model.DemoData
import cl.aiep.urbanmovesmart.model.Vehicle
import cl.aiep.urbanmovesmart.model.VehicleType
import cl.aiep.urbanmovesmart.ui.theme.*

@Composable
fun DemoPill(modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = Mint, shape = RoundedCornerShape(50)) {
        Text("ESCENARIO DEMO", color = Teal, fontWeight = FontWeight.Bold,
            fontSize = 10.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp))
    }
}

@Composable
fun SectionLabel(title: String, subtitle: String? = null, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Ink)
        if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Muted)
    }
}

@Composable
fun HeroCard(title: String, body: String, modifier: Modifier = Modifier) {
    Surface(modifier, color = Ink, shape = RoundedCornerShape(24.dp)) {
        Row(Modifier.padding(19.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Surface(color = Lime, shape = RoundedCornerShape(16.dp), modifier = Modifier.size(52.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Bolt, null, tint = Ink) }
            }
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(title, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                Text(body, style = MaterialTheme.typography.bodySmall, color = Color(0xFFD7E8E9))
            }
        }
    }
}

@Composable
fun VehicleIcon(type: VehicleType, modifier: Modifier = Modifier) {
    Box(modifier.size(56.dp).background(Mint, RoundedCornerShape(17.dp)), contentAlignment = Alignment.Center) {
        Icon(
            imageVector = when(type) {
                VehicleType.BIKE -> Icons.Default.DirectionsBike
                VehicleType.SCOOTER -> Icons.Default.ElectricScooter
                VehicleType.EBIKE -> Icons.Default.ElectricBike
            },
            contentDescription = type.title, tint = Teal, modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun VehicleCard(vehicle: Vehicle, onSelect: () -> Unit) {
    Card(onClick = onSelect, colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                VehicleIcon(vehicle.type)
                Column(Modifier.weight(1f)) {
                    Text(vehicle.name, fontWeight = FontWeight.Bold, color = Ink)
                    Text("${vehicle.type.title} · ${vehicle.id}", style = MaterialTheme.typography.bodySmall, color = Muted)
                }
                Icon(Icons.Default.ChevronRight, null, tint = Teal)
            }
            HorizontalDivider(color = Color(0xFFE4EFEF))
            Row(verticalAlignment = Alignment.CenterVertically) {
                LabelIcon(Icons.Default.BatteryFull, "${vehicle.battery}%")
                Spacer(Modifier.width(14.dp))
                LabelIcon(Icons.Default.DirectionsWalk, "${vehicle.walkMinutes} min a pie")
                Spacer(Modifier.weight(1f))
                Text("${DemoData.pesos(vehicle.minuteFee)}/min", color = Teal, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun LabelIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, null, tint = Teal, modifier = Modifier.size(16.dp))
        Text(text, fontSize = 12.sp, color = Ink)
    }
}

/** Plano urbano ilustrativo: no usa GPS ni cartografía real. */
@Composable
fun ConceptMap(onVehicle: (Vehicle) -> Unit, modifier: Modifier = Modifier) {
    Box(modifier.height(290.dp).background(Color(0xFFE7F2EE), RoundedCornerShape(25.dp)).border(
        1.dp, Color(0xFFD2E5E2), RoundedCornerShape(25.dp)).padding(6.dp)) {
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width; val h = size.height
            // Parcelas y corredores verdes a escala ilustrativa.
            drawRoundRect(color = Color(0xFFCEE8D9), topLeft = Offset(w*.08f, h*.10f),
                size = androidx.compose.ui.geometry.Size(w*.26f,h*.24f),
                cornerRadius=androidx.compose.ui.geometry.CornerRadius(20f))
            drawRoundRect(color = Color(0xFFC9E8D5), topLeft = Offset(w*.67f,h*.59f),
                size = androidx.compose.ui.geometry.Size(w*.24f,h*.28f),
                cornerRadius=androidx.compose.ui.geometry.CornerRadius(20f))
            val road=Color.White
            val paths = listOf(
                Pair(Offset(0f,h*.34f), Offset(w,h*.48f)),
                Pair(Offset(0f,h*.72f), Offset(w,h*.62f)),
                Pair(Offset(w*.27f,0f), Offset(w*.40f,h)),
                Pair(Offset(w*.78f,0f), Offset(w*.62f,h))
            )
            paths.forEach { (a,b) -> drawLine(road,a,b,strokeWidth=27f,cap=StrokeCap.Round) }
            paths.forEach { (a,b) -> drawLine(Color(0xFFB8D5D0),a,b,strokeWidth=2.5f,cap=StrokeCap.Round) }
            drawCircle(color = Teal, radius=11f, center=Offset(w*.47f,h*.50f))
            drawCircle(color=Color.White, radius=6f,center=Offset(w*.47f,h*.50f))
            drawCircle(color=Teal.copy(alpha=.16f),radius=36f,center=Offset(w*.47f,h*.50f),style=Stroke(width=4f))
        }
        MapPin(vehicle = DemoData.vehicles[0], x=34.dp,y=42.dp, onVehicle=onVehicle)
        MapPin(vehicle = DemoData.vehicles[1], x=208.dp,y=82.dp, onVehicle=onVehicle)
        MapPin(vehicle = DemoData.vehicles[2], x=102.dp,y=204.dp, onVehicle=onVehicle)
        Surface(modifier=Modifier.align(Alignment.BottomEnd).padding(9.dp), shape=RoundedCornerShape(50),
            color=Color.White.copy(alpha=.95f)) {
            Text("MAPA ILUSTRATIVO", modifier=Modifier.padding(horizontal=10.dp,vertical=6.dp),
                fontWeight=FontWeight.SemiBold,fontSize=9.sp,color=Muted)
        }
    }
}

@Composable
private fun MapPin(vehicle: Vehicle, x: androidx.compose.ui.unit.Dp, y: androidx.compose.ui.unit.Dp, onVehicle: (Vehicle) -> Unit) {
    Surface(
        modifier=Modifier.offset(x=x,y=y).size(48.dp).clickable { onVehicle(vehicle) },
        shape=CircleShape,color=Teal,shadowElevation=4.dp
    ) {
        Box(contentAlignment=Alignment.Center) {
            Icon(when(vehicle.type) {
                VehicleType.BIKE -> Icons.Default.DirectionsBike
                VehicleType.SCOOTER -> Icons.Default.ElectricScooter
                VehicleType.EBIKE -> Icons.Default.ElectricBike
            },contentDescription="Ver ${vehicle.name}",tint=Color.White,modifier=Modifier.size(24.dp))
        }
    }
}

@Composable
fun BigButton(label:String, onClick:()->Unit, modifier: Modifier = Modifier, enabled:Boolean=true) {
    Button(onClick=onClick,enabled=enabled,modifier=modifier.fillMaxWidth().height(52.dp),
        shape=RoundedCornerShape(16.dp)) { Text(label,fontWeight=FontWeight.Bold) }
}

@Composable
fun SoftCard(modifier:Modifier=Modifier, content: @Composable ColumnScope.()->Unit) {
    Surface(modifier, color=Color.White,shape=RoundedCornerShape(23.dp)) {
        Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp),content=content)
    }
}
