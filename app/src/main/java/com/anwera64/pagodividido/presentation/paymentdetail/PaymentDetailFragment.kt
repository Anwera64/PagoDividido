package com.anwera64.pagodividido.presentation.paymentdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.BottomSheetFragmentDetailBinding
import com.anwera64.pagodividido.domain.models.ExpenditureDetail
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentDetailFragment : BottomSheetDialogFragment() {

    companion object {
        private const val EXPENDITURE_ID = "EXPENDITURE ID"
        private const val LAYOUT = R.layout.bottom_sheet_fragment_detail

        fun newInstance(id: String): PaymentDetailFragment = PaymentDetailFragment().apply {
            arguments = Bundle().apply {
                putString(EXPENDITURE_ID, id)
            }
        }
    }

    private val viewModel by currentScope.viewModel(this, PaymentDetailViewModel::class)
    private lateinit var binding: BottomSheetFragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            LAYOUT,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments
            ?.getString(EXPENDITURE_ID)
            ?.let(viewModel::getPaymentDetail)
            ?.observe(this, ::onDetails)
    }

    private fun onDetails(expenditureDetail: ExpenditureDetail?) {
        expenditureDetail ?: return
        //Just to try UI. TODO() update with final UI elements
        binding.tvTitle.text = expenditureDetail.detail
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme
}