package ru.msnetworks.warehouse_helper

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.msnetworks.warehouse_helper.database.DatabaseBuilder
import ru.msnetworks.warehouse_helper.ui.Navigation
import ru.msnetworks.warehouse_helper.ui.theme.WarehousehelperTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WarehousehelperTheme {
                val mainViewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(
                        DatabaseBuilder.getInstance(this).shipmentDao(),
                        application
                    )
                )
                Navigation(mainViewModel)
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        val image = data?.extras?.get("data") as Bitmap
//        filesDir
//        if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                val contents = data?.getStringExtra("SCAN_RESULT")
//            }
//            if (resultCode == RESULT_CANCELED) {
//                //handle cancel
//            }
//        }
//    }

    private fun getQRCode() {
        try {
            val intent = Intent("com.google.zxing.client.android.SCAN")
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE") // "PRODUCT_MODE for bar codes
            startActivityForResult(intent, 0)
        } catch (e: Exception) {
            val marketUri: Uri = Uri.parse("market://details?id=com.google.zxing.client.android")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            startActivity(marketIntent)
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WarehousehelperTheme {
        Greeting("Машенька")
    }
}