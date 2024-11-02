package ma.ensa.project

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ma.ensa.project.data.Product
import ma.ensa.project.service.ProductService
import java.io.ByteArrayOutputStream

class CreateFragment : Fragment() {

    private lateinit var etTitle: EditText
    private lateinit var etPrice: EditText
    private lateinit var btnUploadImage: Button
    private lateinit var ivPreviewImage: ImageView
    private lateinit var btnSubmitProduct: Button

    private var selectedImageUri: Uri? = null
    private var encodedImage: String? = null

    private val IMAGE_PICK_CODE = 1000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create, container, false)

        etTitle = view.findViewById(R.id.etTitle)
        etPrice = view.findViewById(R.id.etPrice)
        btnUploadImage = view.findViewById(R.id.btnUploadImage)
        ivPreviewImage = view.findViewById(R.id.ivPreviewImage)
        btnSubmitProduct = view.findViewById(R.id.btnSubmitProduct)

        btnUploadImage.setOnClickListener {
            pickImageFromGallery()
        }

        btnSubmitProduct.setOnClickListener {
            createProduct()
        }

        return view
    }

    // Méthode pour choisir une image depuis la galerie
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    // Gérer le retour après sélection d'une image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            selectedImageUri = data?.data
            ivPreviewImage.setImageURI(selectedImageUri)

            // Encoder l'image en Base64
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImageUri)
            encodedImage = encodeImageToBase64(bitmap)
        }
    }

    // Méthode pour encoder une image en base64
    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    // Méthode pour créer un produit
    private fun createProduct() {
        val title = etTitle.text.toString()
        val price = etPrice.text.toString().toFloatOrNull()

        if (title.isEmpty() || price == null || encodedImage == null) {
            Toast.makeText(context, "Veuillez remplir tous les champs, y compris l'image", Toast.LENGTH_SHORT).show()
            return
        }

        // Créer un produit
        val product = Product(
            1,
            title = title,
            price = price,
            image = encodedImage ?: ""
        )

        // Appel à la méthode createProduct dans ProductService
        val productService = ProductService(requireContext())
        productService.createProduct(product)
        requireActivity().recreate()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_main, HomeScreenFragment()) // Replace with your fragment
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }
}
