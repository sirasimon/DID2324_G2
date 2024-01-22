package it.polito.did.g2.shopdrop.ui.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.MainViewModel
import it.polito.did.g2.shopdrop.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartSummary(navController: NavController, viewModel: MainViewModel) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { CS_TopBar(navController, viewModel, scrollBehavior) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = { CS_Button(navController, viewModel)},
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(Modifier.fillMaxWidth()) {
                Text("[Qui la lista dell'ordine]")
                Text("[Qui la mappa del locker che sarebbe cliccabile]")
                Text("[Qui il metodo di pagamento]")
                // TOTAL PRICE AREA

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                    ) {}

                    PriceItemList(
                        stringResource(R.string.price_subtotal).capitalize(),
                        String.format("%.2f €", viewModel.subtot.value)
                    )  //TODO
                    PriceItemList(
                        stringResource(R.string.price_shipment).capitalize(),
                        String.format("%.2f €", viewModel.shipmentFee)
                    )
                    PriceItemList(
                        stringResource(R.string.price_service).capitalize(),
                        String.format("%.2f €", viewModel.serviceFee)
                    )
                    PriceListTotal(
                        stringResource(R.string.price_total).uppercase(),
                        String.format(
                            "%.2f €",
                            (viewModel.subtot.value
                                ?: 0f) + viewModel.shipmentFee + viewModel.serviceFee
                        )
                    )

                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CS_TopBar(
    navController: NavController,
    viewModel: MainViewModel,
    scrollBehavior: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.title_order_summary).capitalize(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun CS_Button(navController: NavController, viewModel: MainViewModel) {
    ExtendedFloatingActionButton(
        onClick = { navController.navigate("OrderConfirmed") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
            Text(text="${viewModel.subtot.value} €")
            Text(stringResource(id = R.string.btn_order_now).capitalize())
        }
    }
}