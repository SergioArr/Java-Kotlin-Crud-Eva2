package com.sergio.eva2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.sergio.eva2.db.DBHelper
import com.sergio.eva2.db.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch(Dispatchers.IO) {
            val productoDao = DBHelper.getInstance(this@MainActivity).productoDao()
            val cantRegistros = productoDao.contar()
            if (cantRegistros < 1) {
                productoDao.insertProduct(Producto(0, "Huevos", false))
                productoDao.insertProduct(Producto(0, "Champiñones", false))
                productoDao.insertProduct(Producto(0, "Queso", false))
                productoDao.insertProduct(Producto(0, "Leche", false))
            }

        }
        setContent {
            ListaProductosUI()
        }

    }


}

@Composable
fun ListaProductosUI() {
    val addProductButtonResId = R.string.addProductButton
    val contexto = LocalContext.current
    val (productos, setProductos) = remember {
        mutableStateOf(emptyList<Producto>())
    }

    LaunchedEffect(productos) {
        withContext(Dispatchers.IO) {
            val dao = DBHelper.getInstance(contexto).productoDao()
            setProductos(dao.findAll())
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(productos) { producto ->
                ProductoItemUI(producto) {
                    setProductos(emptyList<Producto>())
                }
            }
        }
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            onClick = {
                val intent: Intent = Intent(contexto, NewProduct::class.java)
                contexto.startActivity(intent)
            }) {
            Text(stringResource(id = addProductButtonResId))
        }
    }

}

@Composable
fun ProductoItemUI(producto:Producto, onSave:() -> Unit = {} ){
    val contexto = LocalContext.current
    val alcanceCorrutina = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ){
        if(producto.adquirido) {
            Icon(
                Icons.Filled.Check,
                contentDescription = "Producto adquirido",
                modifier = Modifier.clickable{
                    alcanceCorrutina.launch(Dispatchers.IO){
                        val dao = DBHelper.getInstance(contexto).productoDao()
                        producto.adquirido = false
                        dao.updateProduct(producto)
                        onSave()
                    }
                },
                tint = androidx.compose.ui.graphics.Color.Green
            )

        } else {
            Icon(
                Icons.Filled.ShoppingCart,
                contentDescription = "Producto por adquirir",
                modifier = Modifier.clickable{
                    alcanceCorrutina.launch(Dispatchers.IO){
                        val dao = DBHelper.getInstance(contexto).productoDao()
                        producto.adquirido = true
                        dao.updateProduct(producto)
                        onSave()
                    }
                },
                tint = androidx.compose.ui.graphics.Color.Blue
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            producto.producto,
            modifier = Modifier.weight(2f)
        )
        Icon(
            Icons.Filled.Delete,
            contentDescription = "Eliminar producto",
            modifier = Modifier.clickable{
                alcanceCorrutina.launch(Dispatchers.IO){
                    val dao = DBHelper.getInstance(contexto).productoDao()
                    val rowsdelted = dao.deleteProduct(producto)
                    onSave()
                }
            },
            tint = androidx.compose.ui.graphics.Color.Red
        )


    }
}