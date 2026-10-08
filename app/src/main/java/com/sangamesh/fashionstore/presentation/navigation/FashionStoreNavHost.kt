package com.sangamesh.fashionstore.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sangamesh.fashionstore.presentation.address.AddEditAddressScreen
import com.sangamesh.fashionstore.presentation.address.AddressScreen
import com.sangamesh.fashionstore.presentation.auth.LoginScreen
import com.sangamesh.fashionstore.presentation.auth.RegisterScreen
import com.sangamesh.fashionstore.presentation.cart.CartScreen
import com.sangamesh.fashionstore.presentation.category.CategoriesScreen
import com.sangamesh.fashionstore.presentation.checkout.CheckoutScreen
import com.sangamesh.fashionstore.presentation.checkout.OrderSuccessScreen
import com.sangamesh.fashionstore.presentation.checkout.PaymentScreen
import com.sangamesh.fashionstore.presentation.home.HomeScreen
import com.sangamesh.fashionstore.presentation.orders.OrderDetailsScreen
import com.sangamesh.fashionstore.presentation.orders.OrdersScreen
import com.sangamesh.fashionstore.presentation.product.ProductDetailsScreen
import com.sangamesh.fashionstore.presentation.product.ProductListingScreen
import com.sangamesh.fashionstore.presentation.profile.ProfileScreen
import com.sangamesh.fashionstore.presentation.profile.SettingsScreen
import com.sangamesh.fashionstore.presentation.search.SearchScreen
import com.sangamesh.fashionstore.presentation.splash.SplashScreen
import com.sangamesh.fashionstore.presentation.wishlist.WishlistScreen

@Composable
fun FashionStoreNavHost(cartCount: Int = 0) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = BottomDestination.entries.any { it.route == currentRoute }

    Scaffold(
        bottomBar = { if (showBottomBar) BottomBar(navController, currentRoute, cartCount) },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = if (showBottomBar) Modifier.padding(padding) else Modifier,
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(
                    onNavigateHome = { navController.navigate(Routes.HOME) { popUpTo(Routes.SPLASH) { inclusive = true } } },
                    onNavigateLogin = { navController.navigate(Routes.LOGIN) { popUpTo(Routes.SPLASH) { inclusive = true } } },
                )
            }
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = { navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } } },
                    onNavigateRegister = { navController.navigate(Routes.REGISTER) },
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    onRegisterSuccess = { navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } } },
                    onNavigateLogin = { navController.popBackStack() },
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    onProductClick = { navController.navigate(Routes.productDetails(it)) },
                    onCategoryClick = { navController.navigate(Routes.productListing(it)) },
                    onSearchClick = { navController.navigate(Routes.SEARCH) },
                    onProfileClick = { navController.navigate(Routes.PROFILE) },
                    onCartClick = { navController.navigate(Routes.CART) },
                )
            }
            composable(Routes.CATEGORIES) {
                CategoriesScreen(onCategoryClick = { navController.navigate(Routes.productListing(it)) })
            }
            composable(Routes.CART) {
                CartScreen(
                    onCheckout = { navController.navigate(Routes.CHECKOUT) },
                    onBrowse = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } },
                )
            }
            composable(Routes.ORDERS) {
                OrdersScreen(onOrderClick = { navController.navigate(Routes.orderDetails(it)) })
            }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    onNavigateOrders = { navController.navigate(Routes.ORDERS) },
                    onNavigateWishlist = { navController.navigate(Routes.WISHLIST) },
                    onNavigateAddresses = { navController.navigate(Routes.ADDRESSES) },
                    onNavigateSettings = { navController.navigate(Routes.SETTINGS) },
                    onLoggedOut = { navController.navigate(Routes.LOGIN) { popUpTo(0) } },
                )
            }

            composable(
                Routes.PRODUCT_LISTING,
                arguments = listOf(navArgument(Routes.CATEGORY_ARG) { type = NavType.StringType; nullable = true; defaultValue = null }),
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString(Routes.CATEGORY_ARG)
                ProductListingScreen(
                    title = category ?: "All Products",
                    onBack = { navController.popBackStack() },
                    onProductClick = { navController.navigate(Routes.productDetails(it)) },
                )
            }
            composable(
                Routes.PRODUCT_DETAILS,
                arguments = listOf(navArgument(Routes.PRODUCT_ID_ARG) { type = NavType.LongType }),
            ) {
                ProductDetailsScreen(
                    onBack = { navController.popBackStack() },
                    onGoToCart = { navController.navigate(Routes.CART) },
                )
            }

            composable(Routes.SEARCH) {
                SearchScreen(
                    onBack = { navController.popBackStack() },
                    onProductClick = { navController.navigate(Routes.productDetails(it)) },
                )
            }
            composable(Routes.WISHLIST) {
                WishlistScreen(onProductClick = { navController.navigate(Routes.productDetails(it)) })
            }

            composable(Routes.ADDRESSES) {
                AddressScreen(
                    onBack = { navController.popBackStack() },
                    onAddNew = { navController.navigate(Routes.ADD_ADDRESS) },
                    onEdit = { navController.navigate(Routes.ADD_ADDRESS) },
                )
            }
            composable(Routes.ADDRESSES_SELECT) {
                val checkoutEntry = remember(it) { navController.getBackStackEntry(Routes.CHECKOUT) }
                val checkoutViewModel: com.sangamesh.fashionstore.presentation.checkout.CheckoutViewModel =
                    androidx.hilt.navigation.compose.hiltViewModel(checkoutEntry)
                AddressScreen(
                    onBack = { navController.popBackStack() },
                    onAddNew = { navController.navigate(Routes.ADD_ADDRESS) },
                    onEdit = {},
                    selectionMode = true,
                    onSelect = { address -> checkoutViewModel.selectAddress(address); navController.popBackStack() },
                )
            }
            composable(Routes.ADD_ADDRESS) {
                AddEditAddressScreen(onBack = { navController.popBackStack() }, onSaved = { navController.popBackStack() })
            }

            composable(Routes.CHECKOUT) { backStackEntry ->
                CheckoutScreen(
                    onBack = { navController.popBackStack() },
                    onChangeAddress = { navController.navigate(Routes.ADDRESSES_SELECT) },
                    onProceedToPayment = { navController.navigate(Routes.PAYMENT) },
                    viewModel = androidx.hilt.navigation.compose.hiltViewModel(backStackEntry),
                )
            }
            composable(Routes.PAYMENT) {
                val checkoutEntry = remember(it) { navController.getBackStackEntry(Routes.CHECKOUT) }
                PaymentScreen(
                    onSuccess = { orderId, orderNumber ->
                        navController.navigate(Routes.orderSuccess(orderId, orderNumber)) {
                            popUpTo(Routes.CART) { inclusive = true }
                        }
                    },
                    onCancel = { navController.popBackStack() },
                    viewModel = androidx.hilt.navigation.compose.hiltViewModel(checkoutEntry),
                )
            }
            composable(
                Routes.ORDER_SUCCESS,
                arguments = listOf(
                    navArgument(Routes.ORDER_SUCCESS_ID_ARG) { type = NavType.LongType },
                    navArgument(Routes.ORDER_SUCCESS_NUMBER_ARG) { type = NavType.StringType },
                ),
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getLong(Routes.ORDER_SUCCESS_ID_ARG) ?: 0L
                val orderNumber = backStackEntry.arguments?.getString(Routes.ORDER_SUCCESS_NUMBER_ARG).orEmpty()
                OrderSuccessScreen(
                    orderNumber = orderNumber,
                    onViewOrder = {
                        navController.navigate(Routes.orderDetails(orderId)) { popUpTo(Routes.HOME) }
                    },
                    onContinueShopping = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } },
                )
            }
            composable(
                Routes.ORDER_DETAILS,
                arguments = listOf(navArgument(Routes.ORDER_ID_ARG) { type = NavType.LongType }),
            ) {
                OrderDetailsScreen(onBack = { navController.popBackStack() })
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
private fun BottomBar(navController: NavHostController, currentRoute: String?, cartCount: Int) {
    NavigationBar {
        BottomDestination.entries.forEach { dest ->
            NavigationBarItem(
                selected = currentRoute == dest.route,
                onClick = {
                    navController.navigate(dest.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    if (dest == BottomDestination.Cart && cartCount > 0) {
                        BadgedBox(badge = { Badge { Text("$cartCount") } }) { Icon(dest.icon, dest.label) }
                    } else {
                        Icon(dest.icon, contentDescription = dest.label)
                    }
                },
                label = { Text(dest.label) },
            )
        }
    }
}
