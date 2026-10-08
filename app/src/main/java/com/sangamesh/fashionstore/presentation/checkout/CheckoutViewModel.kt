package com.sangamesh.fashionstore.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangamesh.fashionstore.domain.model.Address
import com.sangamesh.fashionstore.domain.model.CartItem
import com.sangamesh.fashionstore.domain.model.CartTotals
import com.sangamesh.fashionstore.domain.model.Order
import com.sangamesh.fashionstore.domain.model.OrderItem
import com.sangamesh.fashionstore.domain.model.OrderStatus
import com.sangamesh.fashionstore.domain.model.PaymentMethod
import com.sangamesh.fashionstore.domain.model.PaymentStatus
import com.sangamesh.fashionstore.domain.repository.AddressRepository
import com.sangamesh.fashionstore.domain.repository.CartRepository
import com.sangamesh.fashionstore.domain.repository.OrderRepository
import com.sangamesh.fashionstore.domain.usecase.CalculateCartTotalsUseCase
import com.sangamesh.fashionstore.domain.usecase.CheckoutValidation
import com.sangamesh.fashionstore.domain.usecase.OrderNumberGenerator
import com.sangamesh.fashionstore.domain.usecase.ValidateCheckoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckoutUiState(
    val items: List<CartItem> = emptyList(),
    val totals: CartTotals = CartTotals(0, 0, 0, 0),
    val selectedAddress: Address? = null,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH_ON_DELIVERY,
    val validationError: String? = null,
)

sealed interface PaymentUiState {
    data object Idle : PaymentUiState
    data object Processing : PaymentUiState
    data class Success(val orderId: Long, val orderNumber: String) : PaymentUiState
    data object Failed : PaymentUiState
}

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val addressRepository: AddressRepository,
    private val orderRepository: OrderRepository,
    private val calculateTotals: CalculateCartTotalsUseCase,
    private val validateCheckout: ValidateCheckoutUseCase,
    private val orderNumberGenerator: OrderNumberGenerator,
) : ViewModel() {

    private val cartItems: StateFlow<List<CartItem>> = cartRepository.observeCart()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedAddress = MutableStateFlow<Address?>(null)
    private val _paymentMethod = MutableStateFlow(PaymentMethod.CASH_ON_DELIVERY)

    val state: StateFlow<CheckoutUiState> = kotlinx.coroutines.flow.combine(
        cartItems, _selectedAddress, _paymentMethod,
    ) { items, address, method ->
        CheckoutUiState(items = items, totals = calculateTotals(items), selectedAddress = address, paymentMethod = method)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CheckoutUiState())

    private val _paymentState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val paymentState: StateFlow<PaymentUiState> = _paymentState.asStateFlow()

    init {
        viewModelScope.launch {
            val defaultAddress = addressRepository.observeAddresses().first().firstOrNull { it.isDefault }
                ?: addressRepository.observeAddresses().first().firstOrNull()
            _selectedAddress.value = defaultAddress
        }
    }

    fun selectAddress(address: Address) { _selectedAddress.value = address }
    fun selectPaymentMethod(method: PaymentMethod) { _paymentMethod.value = method }

    fun validate(): CheckoutValidation = validateCheckout(state.value.items, state.value.selectedAddress)

    /** Simulated payment processing; ~15% chance of a simulated failure to demonstrate the retry path. */
    fun pay() {
        val s = state.value
        _paymentState.value = PaymentUiState.Processing
        viewModelScope.launch {
            delay(1200)
            val succeeded = s.paymentMethod == PaymentMethod.CASH_ON_DELIVERY || (0..99).random() >= 15
            if (!succeeded) {
                _paymentState.value = PaymentUiState.Failed
                return@launch
            }
            val address = s.selectedAddress ?: return@launch
            val order = Order(
                orderNumber = orderNumberGenerator(),
                items = s.items.map {
                    OrderItem(it.productId, it.productName, it.imageUrl, it.price, it.quantity, it.size, it.color)
                },
                subtotal = s.totals.subtotal, discount = s.totals.discount, deliveryFee = s.totals.deliveryFee,
                totalAmount = s.totals.total, address = address, paymentMethod = s.paymentMethod,
                paymentStatus = if (s.paymentMethod == PaymentMethod.CASH_ON_DELIVERY) PaymentStatus.PENDING else PaymentStatus.PAID,
                orderStatus = OrderStatus.PLACED,
                createdAt = System.currentTimeMillis(),
            )
            val orderId = orderRepository.placeOrder(order)
            cartRepository.clear()
            _paymentState.value = PaymentUiState.Success(orderId, order.orderNumber)
        }
    }

    fun retryPayment() { _paymentState.value = PaymentUiState.Idle }
}
