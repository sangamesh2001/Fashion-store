package com.sangamesh.fashionstore.presentation.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangamesh.fashionstore.domain.model.Address
import com.sangamesh.fashionstore.domain.repository.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository,
) : ViewModel() {

    val addresses: StateFlow<List<Address>> = addressRepository.observeAddresses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun save(address: Address, onSaved: () -> Unit) {
        viewModelScope.launch { addressRepository.upsert(address); onSaved() }
    }

    fun delete(addressId: Long) {
        viewModelScope.launch { addressRepository.delete(addressId) }
    }

    fun setDefault(addressId: Long) {
        viewModelScope.launch { addressRepository.setDefault(addressId) }
    }
}
