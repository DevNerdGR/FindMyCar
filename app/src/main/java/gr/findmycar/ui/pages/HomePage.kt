package gr.findmycar.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import gr.findmycar.R
import gr.findmycar.ViewModels.AuthState
import gr.findmycar.ViewModels.AuthViewModel
import gr.findmycar.ViewModels.ParkingLot
import gr.findmycar.ViewModels.ParkingLotViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun HomePage(navController: NavController, authViewModel: AuthViewModel, parkingLotViewModel: ParkingLotViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val parkingLot by parkingLotViewModel.parkingLot

    val homeParkingPopupState = remember { mutableStateOf(false) }
    parkingLotViewModel.getParkingLotRealTime()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    HomeParkingPopup(homeParkingPopupState, parkingLotViewModel)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(280.dp)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher),
                            contentDescription = "Icon"
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            //textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium,
                            text = stringResource(R.string.app_name)
                        )
                    }
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {
                            Text(stringResource(R.string.settingsTitle))
                        },
                        selected = false,
                        onClick = {
                            navController.navigate("settings")
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    )
                    NavigationDrawerItem(
                        label = {
                            Text(stringResource(R.string.logoutButton))
                        },
                        selected = false,
                        onClick = {
                            authViewModel.logout()
                            navController.navigate("login")
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Log out"
                            )
                        }
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        HorizontalDivider()
                        Text(stringResource(R.string.about) + "\n" + stringResource(R.string.version))
                    }
                }
            }
        }
    ) {

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        homeParkingPopupState.value = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "Car"
                    )
                }
                /*
                MultiFAB(
                    onClickHome = {
                        homeParkingPopupState.value = true
                    },
                    onClickNew = {

                    }
                )
                */
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(10.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp),
                    modifier = Modifier
                        .height(400.dp)
                        .width(300.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                    ) {
                        Text(
                            stringResource(R.string.cardTitle),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        //HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                        Text(
                            stringResource(R.string.locationLabelTemplate).format(parkingLot?.location),
                            style = MaterialTheme.typography.headlineLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        /*
                        Text(
                            stringResource(R.string.lotNumberTemplate).format(parkingLot?.lotNumber),
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        */
                        //HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                        Text(
                            stringResource(R.string.lastUpdatedTemplate).format(formatEpoch(parkingLot?.epochTimeStamp?: 0)),
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

fun formatEpoch(epochSeconds: Long): String {
    val instant = Instant.ofEpochSecond(epochSeconds)
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy h:mm a")
        .withZone(ZoneOffset.UTC)
    return formatter.format(instant)
}

@Composable
fun HomeParkingPopup(showState : MutableState<Boolean>, parkingLotViewModel: ParkingLotViewModel) {
    val selectedLevel = remember { mutableStateOf("1") }
    val selectedDeck = remember { mutableStateOf("A") }
    if (showState.value) {
        Dialog(
            onDismissRequest = {
                showState.value = false
            }
        ) {
            Card() {
                Column(
                    modifier = Modifier
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.homeCarpark),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            5.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        (1..3).forEach {
                            RoundButton(
                                selectedLevel,
                                it.toString()
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            5.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        (4..6).forEach {
                            RoundButton(
                                selectedLevel,
                                it.toString()
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .height(0.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            5.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        listOf("A", "B").forEach {
                            RoundButton(
                                selectedDeck,
                                it
                            )
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                    Button(
                        onClick = {
                            showState.value = false
                            parkingLotViewModel.updateParkingLot(
                                ParkingLot(
                                    location = "%s%s".format(selectedLevel.value, selectedDeck.value),
                                    lotNumber = 0
                                )
                            )
                        }
                    ) {
                        Text(stringResource(R.string.confirm))
                    }

                }
            }
        }
    }
}

@Composable
fun RoundButton(selectedOptionState : MutableState<String>, value : String) {
    Button(
        shape = CircleShape,
        onClick = {
            selectedOptionState.value = value
        },
        enabled = !selectedOptionState.value.equals(value) // Disable if it's already selected
    ) {
        Text(value)
    }
}

@Composable
fun MultiFAB(onClickHome: () -> Unit, onClickNew: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 360f else 0f, label = "FAB Rotation")

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedVisibility(visible = expanded) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SmallFAB(
                        icon = rememberVectorPainter(Icons.Default.Home),
                        onClick = onClickHome
                    )
                    SmallFAB(
                        icon = rememberVectorPainter(Icons.Default.Add),
                        onClick = onClickNew
                    )
                }
            }

            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.DirectionsCar,
                    contentDescription = "Expand",
                    modifier = Modifier.rotate(rotation)
                )
            }
        }
    }
}



@Composable
fun SmallFAB(icon: Painter, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.inversePrimary,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                icon,
                contentDescription = "Button"
            )
        }
    }
}