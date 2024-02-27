package it.polito.did.g2.shopdrop.ui.crr.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.Order
import it.polito.did.g2.shopdrop.data.OrderStateName
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.cst.common.WIPMessage
import it.polito.did.g2.shopdrop.ui.cst.common.performDevMsg
import it.polito.did.g2.shopdrop.ui.theme.secondaryLight

private enum class CrrFilterChip{COLL, DELI}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CRRHomeScreen(navController : NavController, viewModel: MainViewModel){

    // SNACKBAR
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val listNavController = rememberNavController()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val chipLabels = mapOf(
        CrrFilterChip.COLL to R.string.chip_to_collect,
        CrrFilterChip.DELI to R.string.chip_to_deposit
    )

    Scaffold(
        floatingActionButton = {
           SmallFloatingActionButton(onClick = { performDevMsg(scope, snackbarHostState, context) }) {
               Icon(Icons.Filled.Sort,
                   contentDescription = null,
                   modifier = Modifier.scale(scaleX = -1f, scaleY = 1f)
               )
           }
        },
        snackbarHost = { WIPMessage(snackbarHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ){

                HomeHeader(
                    viewModel.currUser.value?.name?.substringBefore(" ")?:"ERR",
                    viewModel.currUser.value?.uid?:"ERR"
                ) { navController.navigate(Screens.CrrProfileScreen.route) }

                var activeTab by remember { mutableStateOf(CrrFilterChip.COLL) }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ){

                    enumValues<CrrFilterChip>().forEach {
                        FilterChip(
                            onClick = {activeTab = it; listNavController.navigate(it.toString())},
                            label = { Text(stringResource(id = chipLabels[it]!!).capitalize()) },
                            selected = activeTab==it,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }

                Column {
                    NavHost(navController = listNavController, startDestination = activeTab.name){
                        composable(CrrFilterChip.COLL.toString()) {
                            viewModel.ordersList.observeAsState().value
                                ?.filter { it.carrierID == viewModel.currUser.value?.uid && it.stateList?.map{s -> s.state}?.contains(OrderStateName.RECEIVED)==true && it.stateList?.map{s -> s.state}?.contains(OrderStateName.CARRIED)==false }
                                ?.forEach{
                                    val onMore = {id : String -> navController.navigate(Screens.CrrOrderDetail.route+"/$id") }
                                    val onScan = {id : String -> navController.navigate(Screens.CrrCollectionCamera.route+"/$id")}
                                    OrderCard(it, onMore, onScan)
                                }
                        }
                        composable(CrrFilterChip.DELI.toString()){
                            viewModel.ordersList.observeAsState().value
                                ?.filter { it.carrierID == viewModel.currUser.value?.uid && it.stateList?.map{s -> s.state}?.contains(OrderStateName.RECEIVED)==true && it.stateList?.map{s -> s.state}?.contains(OrderStateName.CARRIED)==true && it.stateList?.map{s -> s.state}?.contains(OrderStateName.AVAILABLE)==false}
                                ?.forEach{
                                    val onMore = {id : String -> navController.navigate(Screens.CrrOrderDetail.route+"/$id") }
                                    val onScan = {id : String -> navController.navigate(Screens.CrrDepositCameraLocker.route+"/$id")}
                                    OrderCard(it, onMore, onScan)
                                }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun HomeHeader(name: String, uid: String, toProfile: ()->Unit ) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .clip(
                MaterialTheme.shapes.large.copy(
                    topStart = ZeroCornerSize,
                    topEnd = ZeroCornerSize
                )
            )
            .background(Color(0xFF97F0AE))
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(Modifier.height(64.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.crr_profile_pic),
                        contentDescription = null,

                        modifier = Modifier
                            .size(84.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ){
                    Text(
                        text = stringResource(id = R.string.welcome_back).capitalize(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.headlineLarge,
                        //modifier = Modifier.weight(1f)
                    )
                }
            }

            IconButton(
                onClick = toProfile,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            }

        }

        Spacer(Modifier.height(32.dp))

        Text("#${uid}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                Icons.Filled.Circle,
                tint = secondaryLight,
                contentDescription = null,
                modifier = Modifier
                    .height(12.dp)
                    .padding(horizontal = 8.dp)
            )
            Text(
                stringResource(id = R.string.crr_state_active).capitalize(),
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun OrderCard(order: Order, onMore: (String)->Unit, onScan : (String)->Unit) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .clickable { onMore(order.id!!) },
        shape = MaterialTheme.shapes.medium,
        content = {
            ListItem(
                headlineContent = {
                    Column {
                        Text("${stringResource(id = R.string.order).capitalize()} # ${order.id}")
                    }
                },
                trailingContent = {
                    IconButton(
                        onClick = { onScan(order.id!!) } //TODO: aggiungere qui l'argomento alla navigazione dell'ID dell'ordine
                    ){
                        Icon(Icons.Filled.QrCodeScanner, contentDescription = null)
                    }
                }
            )
        }
    )
}

