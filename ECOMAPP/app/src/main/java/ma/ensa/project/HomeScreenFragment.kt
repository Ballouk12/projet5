package ma.ensa.project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ma.ensa.project.adapter.ProductAdapter
import ma.ensa.project.service.ProductService

class HomeScreenFragment : Fragment() {

    private lateinit var productService: ProductService
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_homme, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerVie)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        productService = ProductService(requireContext())
        productAdapter = ProductAdapter(requireContext(),mutableListOf())
        recyclerView.adapter = productAdapter

        loadProducts()

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                showDeleteConfirmationDialog(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return view
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Voulez-vous vraiment supprimer cet élément ?")
        builder.setPositiveButton("Oui") { dialog, _ ->
            deleteItem(position)
            dialog.dismiss()
        }
        builder.setNegativeButton("Non") { dialog, _ ->
            productAdapter.notifyItemChanged(position)
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun deleteItem(position: Int) {
        val productId = productService.productsList[position].id
        productService.deleteProduct(productId) { success ->
            if (success) {
                Log.d("del", "deleteItem: " + "l'element est bien suprime" + productId)
            } else {
                Log.d("del", "deleteItem: " + "erreur de supression de l'element d id" + productId)
            }
            loadProducts()
            productAdapter.notifyItemRemoved(position)
        }
    }

    private fun loadProducts() {
        productService.getProducts { products ->
            products?.let {
                productAdapter.updateProducts(it)
            }
        }
    }
}
