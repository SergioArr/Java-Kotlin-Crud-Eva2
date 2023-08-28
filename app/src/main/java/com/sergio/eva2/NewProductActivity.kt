package com.sergio.eva2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.sergio.eva2.db.DBHelper
import com.sergio.eva2.db.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewProduct : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PantallaNewProduct()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaNewProduct() {

    var nombreProducto by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }
    val contexto = LocalContext.current as ComponentActivity
    val alcanceCorrutina = rememberCoroutineScope()
    val addProductTitleResId = R.string.addProductTitle
    val addProductCaptionResId = R.string.addProductCaption
    val addButtonResId = R.string.addButton
    val returnButtonResId = R.string.returnButton
    val successMessageResId = R.string.successMessage
    val errorMessageResId = R.string.errorMessage
    val resp1 = stringResource(id = successMessageResId)
    val resp2 = stringResource(id = errorMessageResId)

    Column(
        modifier= Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = addProductTitleResId),
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            placeholder = {Text(stringResource(id = addProductCaptionResId))},
            value = nombreProducto,
            onValueChange = {nombreProducto = it},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier
            .height(20.dp)
            .fillMaxWidth())
        Button(onClick = {
            alcanceCorrutina.launch(Dispatchers.IO){
                val dao = DBHelper.getInstance(contexto).productoDao()
                val productName = nombreProducto.toString()
                if (productName.isNotEmpty()) {
                dao.insertProduct( Producto( 0, productName, false) )
                resultado = resp1

            } else {
                resultado = resp2
            }
                nombreProducto = ""
            }
        }) {
            Text(stringResource(id = addButtonResId))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(resultado)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { contexto.finish() }) {
            Text("<- " + stringResource(id = returnButtonResId))
        }
    }
}