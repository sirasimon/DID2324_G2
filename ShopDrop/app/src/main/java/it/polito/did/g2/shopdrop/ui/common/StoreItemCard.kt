package it.polito.did.g2.shopdrop.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import it.polito.did.g2.shopdrop.R
import it.polito.did.g2.shopdrop.data.StoreItem

@Composable
fun StoreItemCard(item: StoreItem, onClick: (it: StoreItem) -> Unit){
    Card(
        Modifier
            .size(width = 160.dp, height = 230.dp)
            .padding(8.dp)
            .clickable(onClick = { onClick(item) })
    ){
        //Spacer(Modifier.height(160.dp))
        AsyncImage(
            model = item.thumbnail,
            contentDescription = stringResource(id = R.string.desc_item_pic_of)+item.name,
            modifier = Modifier
                .height(160.dp)
                .wrapContentSize(),
            //placeholder = painterResource(id = R.drawable.baseline_qr_code_scanner_24),
            //error = painterResource(id = R.drawable.outline_do_disturb_on_24)
        )
        Text(
            text = item.name.capitalize(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Text(
            text = item.price.toString()+" €",
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }
}