package it.polito.did.g2.shopdrop.ui.cst.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.StoreItem
import it.polito.did.g2.shopdrop.data.StoreItemCategory
import it.polito.did.g2.shopdrop.navigation.Screens
import it.polito.did.g2.shopdrop.ui.cst.common.StoreItemCard

@Composable
fun CategorySection(category: StoreItemCategory, itemList: List<StoreItem>, navController: NavController, onClick : (it: StoreItem) -> Unit, query: String? = null){

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom){
            Text(text = stringResource(id = category.getStringRef()).capitalize(),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp)
            )

            TextButton(
                onClick = { navController.navigate(Screens.CstCategoryScreen.route+"?categoryName=$category&query=$query") },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = stringResource(R.string.btn_see_more).capitalize(),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Row(modifier = Modifier
            .padding(horizontal = 16.dp)
            .horizontalScroll(rememberScrollState())){

            //Text("TEST")

            val upperLimit = if(itemList.size<5) itemList.size else 5
            //Text("upper limit is $upperLimit")

            for (i in 0 until upperLimit){
                StoreItemCard(itemList[i]) {
                    onClick(it)
                }
            }


            /*
                        for(i in 0 until upperLimit){
                            //ItemCard(itemList[i], onClick)
                            Card(modifier = Modifier
                                .size(width = 160.dp, height = 200.dp)
                                .padding(8.dp)
                                .clickable(onClick = onClick)) {
                                Column {
                                    Spacer(Modifier.height(160.dp))
                                    Text(text = itemList[i].name)
                                    Text(text = itemList[i].price.toString())
                                }
                            }
                        }

                         */
        }
    }
}