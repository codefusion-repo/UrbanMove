package cl.aiep.urbanmovesmart.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.aiep.urbanmovesmart.model.DemoData
import cl.aiep.urbanmovesmart.model.Vehicle
import cl.aiep.urbanmovesmart.ui.screens.*
import cl.aiep.urbanmovesmart.ui.theme.*
import kotlinx.coroutines.launch

private enum class Screen { MAP, VEHICLE, RESERVE, RIDE, PAYMENT, RECEIPT, HISTORY, SUPPORT, PROFILE }

@Composable
fun UrbanMoveApp() {
    var screenName by rememberSaveable { mutableStateOf(Screen.MAP.name) }
    var selectedId by rememberSaveable { mutableStateOf(DemoData.vehicles.first().id) }
    var paidId by rememberSaveable { mutableStateOf("") }
    var demoUser by rememberSaveable { mutableStateOf("") }
    val selected = DemoData.vehicles.firstOrNull { it.id==selectedId } ?: DemoData.vehicles.first()
    val paidVehicle = DemoData.vehicles.firstOrNull { it.id==paidId }
    val screen = Screen.valueOf(screenName)
    fun go(route: Screen) { screenName = route.name }
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    BackHandler(enabled=screen != Screen.MAP) {
        go(when(screen){
            Screen.RESERVE -> Screen.VEHICLE
            Screen.PAYMENT -> Screen.RIDE
            Screen.VEHICLE -> Screen.MAP
            Screen.RIDE -> Screen.MAP
            else -> Screen.MAP
        })
    }
    Scaffold(
        containerColor=CanvasBg,
        topBar={
            Surface(color=CanvasBg) {
                Row(Modifier.fillMaxWidth().padding(horizontal=20.dp,vertical=13.dp),
                    verticalAlignment=Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("UrbanMove",color=Ink,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.ExtraBold)
                        Text("SMART MOBILITY · PROTOTIPO",color=Muted,fontSize=10.sp,letterSpacing=1.sp)
                    }
                    if(screen==Screen.RIDE) {
                        Surface(color=Lime,shape=RoundedCornerShape(10.dp)) {
                            Text("EN VIAJE",modifier=Modifier.padding(horizontal=10.dp,vertical=7.dp),
                                color=Ink,fontSize=11.sp,fontWeight=FontWeight.Bold)
                        }
                    } else {
                        IconButton(onClick={go(Screen.PROFILE)}) {
                            Icon(Icons.Default.AccountCircle,contentDescription="Abrir perfil",tint=Teal,modifier=Modifier.size(26.dp))
                        }
                    }
                }
            }
        },
        snackbarHost={ SnackbarHost(snackbar) },
        bottomBar={
            NavigationBar(containerColor=Color.White,tonalElevation=4.dp) {
                NavItem("Mapa",Icons.Default.Map,screen==Screen.MAP || screen==Screen.VEHICLE || screen==Screen.RESERVE,
                    onClick={go(Screen.MAP)})
                NavItem("Viajes",Icons.Default.History,screen==Screen.HISTORY || screen==Screen.PAYMENT || screen==Screen.RECEIPT || screen==Screen.RIDE,
                    onClick={go(Screen.HISTORY)})
                NavItem("Ayuda",Icons.Default.HelpOutline,screen==Screen.SUPPORT,onClick={go(Screen.SUPPORT)})
                NavItem("Perfil",Icons.Default.Person,screen==Screen.PROFILE,onClick={go(Screen.PROFILE)})
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when(screen) {
                Screen.MAP -> MapScreen(onVehicle={ selectedId=it.id;go(Screen.VEHICLE) })
                Screen.VEHICLE -> VehicleScreen(selected,onBack={go(Screen.MAP)},onReserve={go(Screen.RESERVE)})
                Screen.RESERVE -> ReserveScreen(selected,onBack={go(Screen.VEHICLE)},onConfirm={go(Screen.RIDE)})
                Screen.RIDE -> RideScreen(selected,onSupport={go(Screen.SUPPORT)},onFinish={go(Screen.PAYMENT)})
                Screen.PAYMENT -> PaymentScreen(selected,onBack={go(Screen.RIDE)},onPay={paidId=selected.id;go(Screen.RECEIPT)})
                Screen.RECEIPT -> ReceiptScreen(selected,onHistory={go(Screen.HISTORY)},onMap={go(Screen.MAP)})
                Screen.HISTORY -> HistoryScreen(paidVehicle,onNewTrip={go(Screen.MAP)})
                Screen.SUPPORT -> SupportScreen(onBack={go(Screen.MAP)},onReport={ reason ->
                    scope.launch { snackbar.showSnackbar("Reporte de demostración: $reason. No se envió a un servidor.") }
                })
                Screen.PROFILE -> ProfileScreen(demoUser.takeIf { it.isNotBlank() },
                    onLogin={demoUser=it;go(Screen.MAP)},onLogout={demoUser=""})
            }
        }
    }
}

@Composable
private fun RowScope.NavItem(title:String,icon: androidx.compose.ui.graphics.vector.ImageVector,
                                  selected:Boolean,onClick:()->Unit) {
    NavigationBarItem(selected=selected,onClick=onClick,
        icon={Icon(icon,contentDescription=null)},label={Text(title,fontSize=11.sp)},alwaysShowLabel=true,
        colors=NavigationBarItemDefaults.colors(selectedIconColor=Ink,selectedTextColor=Ink,
            indicatorColor=Mint,unselectedIconColor=Muted,unselectedTextColor=Muted))
}
