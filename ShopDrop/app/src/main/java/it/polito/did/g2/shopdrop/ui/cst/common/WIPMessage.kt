package it.polito.did.g2.shopdrop.ui.cst.common

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.polito.did.g2.shopdrop.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WIPMessage(snackbarHostState: SnackbarHostState) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackbarData: SnackbarData ->
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.msg_developing), modifier = Modifier.padding(horizontal = 8.dp))
                }
            }
        }
    )
}

fun performDevMsg(scope: CoroutineScope, snackbarHostState: SnackbarHostState, context: Context) {
    scope.launch{
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(
            message = "",
            duration = SnackbarDuration.Short)
    }
}