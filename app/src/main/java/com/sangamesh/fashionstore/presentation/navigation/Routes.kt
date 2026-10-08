package com.sangamesh.fashionstore.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"

    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val CART = "cart"
    const val ORDERS = "orders"
    const val PROFILE = "profile"

    const val CATEGORY_ARG = "category"
    const val PRODUCT_LISTING = "products?$CATEGORY_ARG={$CATEGORY_ARG}"
    fun productListing(category: String? = null) = if (category != null) "products?$CATEGORY_ARG=$category" else "products?$CATEGORY_ARG="

    const val PRODUCT_ID_ARG = "id"
    const val PRODUCT_DETAILS = "product/{$PRODUCT_ID_ARG}"
    fun productDetails(id: Long) = "product/$id"

    const val SEARCH = "search"
    const val WISHLIST = "wishlist"

    const val ADDRESSES = "addresses"
    const val ADDRESSES_SELECT = "addresses?select=true"
    const val ADD_ADDRESS = "address/add"

    const val CHECKOUT = "checkout"
    const val PAYMENT = "payment"
    const val ORDER_SUCCESS_NUMBER_ARG = "orderNumber"
    const val ORDER_SUCCESS_ID_ARG = "orderId"
    const val ORDER_SUCCESS = "order-success/{$ORDER_SUCCESS_ID_ARG}/{$ORDER_SUCCESS_NUMBER_ARG}"
    fun orderSuccess(orderId: Long, orderNumber: String) = "order-success/$orderId/$orderNumber"

    const val ORDER_ID_ARG = "orderId"
    const val ORDER_DETAILS = "order/{$ORDER_ID_ARG}"
    fun orderDetails(orderId: Long) = "order/$orderId"

    const val SETTINGS = "settings"
}

enum class BottomDestination(val route: String, val label: String, val icon: ImageVector) {
    Home(Routes.HOME, "Home", Icons.Default.Home),
    Categories(Routes.CATEGORIES, "Categories", Icons.Default.Menu),
    Cart(Routes.CART, "Cart", Icons.Default.ShoppingCart),
    Orders(Routes.ORDERS, "Orders", Icons.AutoMirrored.Filled.List),
    Profile(Routes.PROFILE, "Profile", Icons.Default.Person),
}
