package ma.ensa.project.data

data class Product(val id: Int ,val title: String, val price: Float, val image: String) {
    override fun toString(): String {
        return "Product(id=$id, title='$title', price=$price, image='$image')"
    }
}