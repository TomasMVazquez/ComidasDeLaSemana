package com.applications.toms.comidasdelasemana.screen

import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applications.toms.comidasdelasemana.database.DailyMeals
import com.applications.toms.comidasdelasemana.databinding.DailyCardViewBinding

class RecyclerCalendarAdapter(val editClickListener: EditClickListener) :
    ListAdapter<DailyMeals, RecyclerCalendarAdapter.DailyViewHolder>(DailyMealDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        holder.bind(getItem(position)!!,editClickListener)
    }

    class DailyViewHolder private constructor(val binding: DailyCardViewBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: DailyMeals, editClickListener: EditClickListener) {
            binding.dailyMeal = item
            var meal = ""
            binding.lunchCardView.setOnClickListener {
                meal = "lunch"
                editClickListener.onClick("click",item,meal)
                binding.editMeal.text = binding.lunch.text
                binding.textInputFood.hint = "${binding.lunch.text} del ${item.day}"
                binding.editFood.requestFocus()
                binding.editCardView.visibility = View.VISIBLE
            }
            binding.dinerCardView.setOnClickListener {
                meal = "diner"
                editClickListener.onClick("click",item,meal)
                binding.editMeal.text = binding.diner.text
                binding.textInputFood.hint = "${binding.diner.text} del ${item.day}"
                binding.editFood.requestFocus()
                binding.editCardView.visibility = View.VISIBLE
            }
            binding.editFood.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    binding.editCardView.visibility = View.GONE
                    editClickListener.onEnter("enter",item, meal,binding.editFood.text.toString())
                    return@OnKeyListener true
                }
                false
            })
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): DailyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DailyCardViewBinding.inflate(layoutInflater,parent,false)
                return DailyViewHolder(binding)
            }
        }

    }

}

class EditClickListener(val clickListener: (clickType:String,day: String,meal:String,mealToChange:String) -> Unit){
    fun onClick(clickType:String,dailyMeal: DailyMeals,meal:String) = clickListener(clickType,dailyMeal.day,meal,"")
    fun onEnter(clickType:String,dailyMeal: DailyMeals,meal:String,mealToChange:String) = clickListener(clickType,dailyMeal.day,meal,mealToChange)
}

class DailyMealDiffCallback : DiffUtil.ItemCallback<DailyMeals>() {
    override fun areItemsTheSame(oldItem: DailyMeals, newItem: DailyMeals): Boolean {
        return oldItem.day == newItem.day
    }

    override fun areContentsTheSame(oldItem: DailyMeals, newItem: DailyMeals): Boolean {
        return oldItem.lunch == newItem.lunch && oldItem.diner == newItem.diner
    }
}