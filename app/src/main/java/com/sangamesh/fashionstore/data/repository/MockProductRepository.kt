package com.sangamesh.fashionstore.data.repository

import com.sangamesh.fashionstore.domain.model.AppError
import com.sangamesh.fashionstore.domain.model.AppResult
import com.sangamesh.fashionstore.domain.model.Category
import com.sangamesh.fashionstore.domain.model.Product
import com.sangamesh.fashionstore.domain.repository.ProductRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/** Local stand-in for the REST API. Swap for ProductRepositoryImpl (Retrofit) later. */
@Singleton
class MockProductRepository @Inject constructor() : ProductRepository {

    override suspend fun getProducts(): AppResult<List<Product>> {
        delay(NETWORK_DELAY_MS)
        return AppResult.Success(products)
    }

    override suspend fun getProduct(id: Long): AppResult<Product> {
        delay(NETWORK_DELAY_MS)
        return products.firstOrNull { it.id == id }
            ?.let { AppResult.Success(it) }
            ?: AppResult.Error(AppError.Http(404))
    }

    override suspend fun getCategories(): AppResult<List<Category>> {
        delay(NETWORK_DELAY_MS)
        return AppResult.Success(categories)
    }

    companion object {
        private const val NETWORK_DELAY_MS = 400L
        private val STD = listOf("S", "M", "L", "XL")
        private val SHOE = listOf("6", "7", "8", "9", "10")

        private fun p(
            id: Long, name: String, brand: String, category: String,
            price: Int, original: Int, sizes: List<String>, colors: List<String>,
            rating: Float, reviews: Int, stock: Int, description: String,
        ) = Product(
            id = id, name = name, description = description, price = price,
            originalPrice = original,
            discountPercentage = if (original > 0) (original - price) * 100 / original else 0,
            brand = brand, category = category,
            // Placeholder images: royalty-free, no brand assets
            images = List(3) { "https://picsum.photos/seed/fs$id-$it/600/800" },
            availableSizes = sizes, availableColors = colors,
            rating = rating, reviewCount = reviews, stock = stock,
        )

        val products: List<Product> = listOf(
            p(101, "Women's Casual Red Dress", "FashionHub", "Dresses", 1999, 2499, STD, listOf("Red", "Black"), 4.5f, 120, 25, "Comfortable cotton casual dress for everyday wear."),
            p(102, "Floral Summer Maxi Dress", "Bloom & Co", "Dresses", 2799, 3499, STD, listOf("Blue", "Green"), 4.7f, 210, 14, "Flowy maxi dress with a breathable floral print."),
            p(103, "Classic Black Midi Dress", "UrbanLane", "Dresses", 2299, 2299, STD, listOf("Black"), 4.3f, 64, 3, "A versatile midi dress that works from office to evening."),
            p(104, "Slim Fit Oxford Shirt", "Northwind", "Shirts", 1499, 1999, STD + "XXL", listOf("White", "Blue"), 4.4f, 310, 40, "Crisp cotton oxford shirt with a tailored fit."),
            p(105, "Linen Casual Shirt", "Northwind", "Shirts", 1799, 2199, STD, listOf("White", "Green"), 4.6f, 98, 18, "Lightweight linen shirt, ideal for warm weather."),
            p(106, "Checked Flannel Shirt", "Trailhead", "Shirts", 1299, 1899, STD + "XXL", listOf("Red", "Black"), 4.1f, 76, 0, "Soft brushed flannel with a relaxed fit."),
            p(107, "Essential Crew Neck T-Shirt", "BasicLab", "T-Shirts", 499, 699, STD + "XXL", listOf("Black", "White", "Blue"), 4.2f, 540, 120, "Everyday cotton tee, pre-shrunk and soft."),
            p(108, "Graphic Print Tee", "StreetKind", "T-Shirts", 799, 999, STD, listOf("Black", "White"), 4.0f, 155, 35, "Relaxed-fit tee with a bold graphic print."),
            p(109, "Striped Breton T-Shirt", "BasicLab", "T-Shirts", 649, 649, STD, listOf("Blue", "Red"), 4.5f, 88, 22, "Classic striped tee in breathable cotton."),
            p(110, "Straight Fit Blue Jeans", "DenimWorks", "Jeans", 2199, 2999, listOf("28", "30", "32", "34", "36"), listOf("Blue", "Black"), 4.6f, 430, 50, "Mid-rise straight jeans in stretch denim."),
            p(111, "High-Rise Skinny Jeans", "DenimWorks", "Jeans", 1999, 2599, listOf("26", "28", "30", "32"), listOf("Blue", "Black"), 4.4f, 260, 9, "Figure-hugging high-rise jeans with comfortable stretch."),
            p(112, "Everyday Canvas Sneakers", "StepUp", "Shoes", 1699, 2199, SHOE, listOf("White", "Black"), 4.5f, 380, 60, "Lightweight canvas sneakers with cushioned insoles."),
            p(113, "Leather Derby Shoes", "Northwind", "Shoes", 3499, 4499, SHOE, listOf("Black"), 4.3f, 72, 12, "Polished leather derbies for formal occasions."),
            p(114, "Woven Leather Belt", "Northwind", "Accessories", 599, 799, listOf("M", "L", "XL"), listOf("Black"), 4.2f, 110, 45, "Durable woven belt with a brushed metal buckle."),
            p(115, "Classic Tote Bag", "Bloom & Co", "Accessories", 1899, 2499, emptyList(), listOf("Black", "Red"), 4.8f, 190, 16, "Roomy everyday tote with an interior zip pocket."),
        )

        val categories: List<Category> = listOf(
            "Dresses", "Shirts", "T-Shirts", "Jeans", "Shoes", "Accessories",
        ).mapIndexed { index, name ->
            Category(
                id = index + 1L,
                name = name,
                imageUrl = "https://picsum.photos/seed/cat-$name/400/400",
                productCount = products.count { it.category == name },
            )
        }
    }
}
