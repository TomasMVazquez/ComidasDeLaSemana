package com.applications.toms.comidasdelasemana.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.applications.toms.comidasdelasemana.R
import com.applications.toms.comidasdelasemana.database.MealsDatabase
import com.applications.toms.comidasdelasemana.databinding.FragmentCalendarBinding
import com.applications.toms.comidasdelasemana.hideKeyboard
import com.applications.toms.comidasdelasemana.showKeyboard

class CalendarFragment : Fragment() {

    private lateinit var viewModel: CalendarViewModel
    private lateinit var binding: FragmentCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        this.hideKeyboard()
        super.onPause()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_calendar,container,false)

        //get a reference to the application context.
        val application = requireNotNull(this.activity).application
        // get a reference to the DAO of the database
        val dataSource = MealsDatabase.getInstance(application).listDatabaseDao
        //Create an instance of the viewModelFactory.
        val viewModelFactory = CalendarViewModelFactory(dataSource,application)
        // Get a reference to the ViewModel associated with this fragment.
        viewModel = ViewModelProvider(this,viewModelFactory).get(CalendarViewModel::class.java)

        // pass in the view model to the FragmentBinding.
        binding.calendarViewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        //Creamos y asignamos el adaptador al recycler
        val adapter = RecyclerCalendarAdapter(EditClickListener {
            clickType,day,meal,newFood ->
            if (clickType == "click") {
                this.showKeyboard()
            }else{
                viewModel.onStartEditing(day,meal,newFood)
                this.hideKeyboard()
            }
        })

        val manager = GridLayoutManager(activity,1)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        viewModel.weeklyMeals.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    companion object{
        private const val TAG = "TMV-CalendarFragment"
    }

}