package ma.ensa.project

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import ma.ensa.project.R
import ma.ensa.project.data.Product
import ma.ensa.project.service.ProductService
import java.io.ByteArrayOutputStream
import java.io.InputStream

class EditProductActivity : AppCompatActivity() {

    private lateinit var productId: String
    private var price: Float = 0f
    private lateinit var title: String
    private lateinit var imageup: String
    private lateinit var titleEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var imageView: ImageView // Pour afficher l'image
    private var imageUri: Uri? = null
    private lateinit var productService: ProductService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        Log.d("upddd", "onCreate: Intent received with extras: ${intent.extras?.toString()}")
        Log.d("upddd", "onCreate: Checking intent extras: id=${intent.getStringExtra("id")}, title=${intent.getStringExtra("title")}, price=${intent.getStringExtra("price")}, image=${intent.getStringExtra("image")}")

        // Récupérer l'ID du produit à partir de l'intent
        productId = intent.getStringExtra("id") ?: return
        title = intent.getStringExtra("title") ?: return
        price = intent.getStringExtra("price")?.toFloatOrNull() ?: 0f
        imageup = intent.getStringExtra("image") ?: return


        titleEditText = findViewById(R.id.titleEditText)
        priceEditText = findViewById(R.id.priceEditText)
        imageView = findViewById(R.id.imageView)
        Log.d("upddd", "onCreate: "+" title "+title+" price "+price+" image "+imageup)
        titleEditText.setText(title)
        priceEditText.setText(price.toString())
        imageView.setImageBitmap(decodeBase64ToBitmap(imageup))
        productService = ProductService(this)

        // Ajouter un listener pour sélectionner une image
        findViewById<Button>(R.id.selectImageButton).setOnClickListener {
            selectImageFromGallery()
        }



        findViewById<Button>(R.id.updateButton).setOnClickListener {
            val updatedProduct = Product(
                id = productId.toInt(),
                title = titleEditText.text.toString(),
                price = priceEditText.text.toString().toFloat(),
                image = imageUri?.let { encodeImageToBase64(it) // On utilise imageUri ici
                } ?: imageup // Utilisez imageup si imageUri est null
            )
            productService.updateProduct(updatedProduct)
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }



    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*" // Sélectionner uniquement des images
        }
        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data ?: return
            imageView.setImageURI(imageUri) // Affichez l'image sélectionnée
        }
    }
    private fun decodeBase64ToBitmap(encodedString: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: IllegalArgumentException) {
            null
        }
    }


    private fun encodeImageToBase64(imageUri: Uri): String {
        val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream) // Compression en JPEG
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT) // Encodage en Base64
    }

    private fun updateProduct(product: Product) {
        // Votre méthode de mise à jour ici (similaire à celle montrée précédemment)
    }

    companion object {
        private const val IMAGE_PICK_REQUEST_CODE = 100 // Code de demande pour sélectionner l'image
    }
}
