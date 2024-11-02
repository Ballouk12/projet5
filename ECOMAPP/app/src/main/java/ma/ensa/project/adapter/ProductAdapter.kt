package ma.ensa.project.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ma.ensa.project.EditProductActivity
import ma.ensa.project.R
import ma.ensa.project.data.Product

class ProductAdapter(private val context: Context, private val itemList: MutableList<Product>) : RecyclerView.Adapter<ProductAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tvTitle)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
        val imageView: ImageView = itemView.findViewById(R.id.ivBestSeller)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.best_seller_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        Log.d("upddd", "onBindViewHolder: "+currentItem)
        holder.titleTextView.text = currentItem.title
        holder.price.text = currentItem.price.toString()
        val bitmap = decodeBase64ToBitmap(currentItem.image)
        if (bitmap != null) {
            holder.imageView.setImageBitmap(bitmap)
        } else {
            holder.imageView.setImageResource(R.drawable.bg_search_view)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EditProductActivity::class.java)
            intent.putExtra("id", currentItem.id.toString())
            intent.putExtra("title", currentItem.title)
            intent.putExtra("price", currentItem.price.toString()) // Convertir price en String si nécessaire
            intent.putExtra("image", currentItem.image)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun decodeBase64ToBitmap(encodedString: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun updateProducts(newProducts: List<Product>) {
        itemList.clear()
        itemList.addAll(newProducts)
        notifyDataSetChanged()
    }
}
