package com.anwera64.pagodividido.utils

import androidx.lifecycle.MutableLiveData
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <T> MutableLiveData<Set<T>>.modifyLiveDataSet(
    action: MutableSet<T>.() -> Unit
) {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }
    val oldSet: Set<T>? = value
    val newSet: MutableSet<T> = oldSet?.toMutableSet() ?: mutableSetOf()
    newSet.action()
    postValue(newSet)
}

@OptIn(ExperimentalContracts::class)
inline fun <T, K> MutableLiveData<Map<T, K>>.modifyLiveDataMap(
    action: MutableMap<T, K>.() -> Unit
) {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }
    val oldSet: Map<T, K>? = value
    val newSet: MutableMap<T, K> = oldSet?.toMutableMap() ?: mutableMapOf()
    newSet.action()
    postValue(newSet)
}