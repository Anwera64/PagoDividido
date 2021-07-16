package com.anwera64.pagodividido.presentation.trip.companionresult

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.anwera64.pagodividido.R
import com.anwera64.pagodividido.databinding.ListItemResultBinding
import com.anwera64.pagodividido.domain.models.ResultModel
import com.anwera64.pagodividido.utils.ViewUtils

class CompanionResultAdapter : RecyclerView.Adapter<CompanionResultAdapter.ViewHolder>() {

    var results: List<ResultModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DataBindingUtil.inflate<ListItemResultBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_result,
            parent,
            false
        ).let(this::ViewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(results[position])
    }

    override fun getItemCount(): Int = results.size

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.onUnbind()
    }

    inner class ViewHolder(private val binding: ListItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: ResultModel) = with(binding) {
            result = item
            tvPayed.text = root.context.getString(R.string.subtitle_total_expense, item.totalPayed)
            item.debts.forEach(::addTextView)
            btnExtend.setOnClickListener { onExtendClick(item) }
        }

        private fun ListItemResultBinding.onExtendClick(item: ResultModel) {
            grDebts.visibility = if (item.isExtended) View.GONE else View.VISIBLE
            item.isExtended = !item.isExtended
            val drawableId = if (item.isExtended) {
                R.drawable.ic_chevron_up
            } else {
                R.drawable.ic_chevron_down
            }
            btnExtend.setImageDrawable(ResourcesCompat.getDrawable(root.resources, drawableId, null))
        }

        private fun addTextView(debt: Map.Entry<String, Double>) = with(binding) {
            val layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT).apply {
                setMargins(ViewUtils.gerMarginInDP(16f, root.resources), 0, 0, 0)
            }
            TextView(root.context).apply {
                //key -> name, value -> amount
                text = context.getString(R.string.bullet_point_name_amount, debt.key, debt.value)
                this.layoutParams = layoutParams
            }.let(llDebts::addView)
        }

        fun onUnbind() = with(binding) {
            llDebts.removeAllViews()
        }
    }
}