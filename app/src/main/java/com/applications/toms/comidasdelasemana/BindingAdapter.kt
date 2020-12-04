package com.applications.toms.comidasdelasemana

import android.util.Log
import android.view.View
import androidx.core.view.get
import androidx.databinding.BindingAdapter
import com.applications.toms.comidasdelasemana.screen.CalendarViewModel.EditingStatus
import com.google.android.material.card.MaterialCardView

@BindingAdapter("editingStatus")
fun bindStatus(cardView: MaterialCardView, status: EditingStatus?){
    Log.d("TMV BINDING", "bindStatus: ${status}")
    Log.d("TMV BINDING", "bindStatus: ${cardView.id}")
    Log.d("TMV BINDING", "bindStatus: ${cardView.verticalScrollbarPosition}")
    when(status){
        EditingStatus.EDITING -> {
            cardView.visibility = View.VISIBLE
        }
        EditingStatus.DONE -> {
            cardView.visibility = View.GONE
        }
        EditingStatus.ERROR -> {
            cardView.visibility = View.GONE
        }
        EditingStatus.INITIAL -> {
            cardView.visibility = View.GONE
        }
        else -> cardView.visibility = View.GONE
    }
}