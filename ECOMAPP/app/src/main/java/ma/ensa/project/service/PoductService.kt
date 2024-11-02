package ma.ensa.project.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import ma.ensa.project.data.Product
import org.json.JSONArray
import org.json.JSONObject

class ProductService(private val context: Context) {

    private val queue: RequestQueue = Volley.newRequestQueue(context)
     val productsList : MutableList<Product> = mutableListOf()
    fun getProducts(callback: (List<Product>?) -> Unit) {
        val url = "http://10.0.2.2:8000/api/products"

        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            object : Response.Listener<JSONArray> {
                override fun onResponse(response: JSONArray) {
                    val products = parseProducts(response)
                    productsList.addAll(products)
                    callback(products)
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: com.android.volley.VolleyError) {
                    Log.e("ProductService", "Erreur lors de la récupération des produits: ${error.message}")
                    callback(null)
                }
            }
        )

        // Ajouter la requête à la file d'attente
        queue.add(request)
    }

    // Méthode pour analyser le JSON et convertir en liste de produits
    private fun parseProducts(jsonArray: JSONArray): List<Product> {
        val productList = mutableListOf<Product>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
            val product = Product(
                id = jsonObject.getInt("id") ,
                title = jsonObject.getString("title"),
                price = jsonObject.getDouble("price").toFloat(),
                image = jsonObject.getString("image")
            )
            Log.d("prod", "parseProducts: "+product)
            productList.add(product)
        }

        return productList
    }


    fun createProduct(product: Product) {
        val url = "http://10.0.2.2:8000/api/product/create"

        // Créer un produit sous forme de JSON
        val jsonObject = JSONObject()
        jsonObject.put("title", product.title)
        jsonObject.put("price", product.price)
        jsonObject.put("image", product.image)

        // Requête POST avec Volley
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response ->
                Toast.makeText(context, "Produit créé avec succès", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(context, "Erreur lors de la création du produit", Toast.LENGTH_SHORT).show()
            }
        )

        // Ajouter la requête à la file d'attente de Volley
        Volley.newRequestQueue(context).add(jsonObjectRequest)
    }

    fun deleteProduct(productId: Int, callback: (Boolean) -> Unit) {
        val url = "http://10.0.2.2:8000/api/products/delete/$productId"

        val request = JsonObjectRequest(
            Request.Method.DELETE,
            url,
            null,
            { _ ->
                callback(true)
            },
            { error ->
                Log.e("ProductService", "Erreur lors de la suppression du produit: ${error.message}")
                callback(false)
            }
        )

        queue.add(request)
    }

    fun updateProduct(product: Product) {
        val url = "http://10.0.2.2:8000/api/product/update/${product.id}" // Assurez-vous que l'URL correspond à votre API

        val jsonObject = JSONObject()
        jsonObject.put("title", product.title)
        jsonObject.put("price", product.price)
        jsonObject.put("image", product.image)


        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, url, jsonObject,
            { response ->
                Toast.makeText(context, "Produit mis à jour avec succès", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(context, "Erreur lors de la mise à jour du produit", Toast.LENGTH_SHORT).show()
                Log.e("UpdateProductError", error.toString())
            }
        )
        queue.add(jsonObjectRequest)
    }

}
