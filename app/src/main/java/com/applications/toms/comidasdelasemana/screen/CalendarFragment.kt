package com.applications.toms.comidasdelasemana.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.applications.toms.comidasdelasemana.R
import com.applications.toms.comidasdelasemana.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private lateinit var viewModel: CalendarViewModel
    private lateinit var binding: FragmentCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_calendar,container,false)

        // Add ViewModel
        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        // pass in the view model to the FragmentBinding.
        binding.calendarViewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this


        /*viewModel.weklyMeals.observe(viewLifecycleOwner, Observer { data ->
            Log.d(TAG, "onCreateView: " + data.toString())

        })*/

        return binding.root
    }

    companion object{
        private const val TAG = "TMV-CalendarFragment"
    }
}