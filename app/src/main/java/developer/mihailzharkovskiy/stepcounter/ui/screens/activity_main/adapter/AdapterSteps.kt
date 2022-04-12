package developer.mihailzharkovskiy.stepcounter.ui.screens.activity_main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import developer.mihailzharkovskiy.stepcounter.R
import developer.mihailzharkovskiy.stepcounter.databinding.AdapterStepsBinding
import developer.mihailzharkovskiy.stepcounter.ui.screens.activity_main.model.AdapterStepsUiModel

class AdapterSteps() : RecyclerView.Adapter<AdapterSteps.ViewHolder>() {

    fun submitList(data: List<AdapterStepsUiModel>) {
        differ.submitList(data)
    }

    private val differCallback = object : DiffUtil.ItemCallback<AdapterStepsUiModel>() {
        override fun areItemsTheSame(
            oldItem: AdapterStepsUiModel,
            newItem: AdapterStepsUiModel,
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: AdapterStepsUiModel,
            newItem: AdapterStepsUiModel,
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder(
        private val binding: AdapterStepsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: AdapterStepsUiModel) = with(binding) {
            tvShagi.text = itemView.context.getString(R.string.recycler_view_steps, data.steps)
            tvKalorii.text = itemView.context.getString(R.string.recycler_view_kkal, data.kkal)
            tvRastoyanie.text = itemView.context.getString(R.string.recycler_view_km, data.km)
            tvDate.text = data.date
            progressBar.progress = data.progress
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterStepsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}


