package com.anwera64.pagodividido.presentation.newexpenditure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anwera64.pagodividido.domain.repository.CompanionRepository
import com.anwera64.pagodividido.domain.repository.ExpenditureRepository

class NewExpenditureViewModelFactory(
    private val companionRepository: CompanionRepository,
    private val expenditureRepository: ExpenditureRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewExpenditureViewModel::class.java)) {
            return NewExpenditureViewModel(companionRepository, expenditureRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}