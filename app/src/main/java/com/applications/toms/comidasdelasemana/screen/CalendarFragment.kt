package com.applications.toms.comidasdelasemana.screen

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.applications.toms.comidasdelasemana.R
import com.applications.toms.comidasdelasemana.database.MealsDatabase
import com.applications.toms.comidasdelasemana.databinding.FragmentCalendarBinding
import com.applications.toms.comidasdelasemana.hideKeyboard
import com.applications.toms.comidasdelasemana.showKeyboard
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var viewModel: CalendarViewModel
    private lateinit var binding: FragmentCalendarBinding

    override fun onPause() {
        this.hideKeyboard()
        super.onPause()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_calendar,container,false)

        //Menu
        setHasOptionsMenu(true)

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

        //Handle DeepLink
        if (!requireArguments().isEmpty) {
            val data =
                (arguments?.get("android-support-nav:controller:deepLinkIntent") as Intent).data
            viewModel.getDataFromDeepLink(data)
        }

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

        val manager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        viewModel.weeklyMeals.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        viewModel.dataDeepLink.observe(viewLifecycleOwner, Observer { it ->
            if(!Uri.EMPTY.equals(it)){
                showDialog()
            }
        })

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, "Comartiendo comida de la semana")
            var query:String = "day=${Calendar.getInstance().get(Calendar.DAY_OF_MONTH)}/${Calendar.getInstance().get(Calendar.MONTH)}/${Calendar.getInstance().get(Calendar.YEAR)}"
            viewModel.weeklyMeals.value!!.forEach { dailyMeal ->
                query +="&${dailyMeal.day}=${context?.getString(R.string.lunch)}:${dailyMeal.lunch.replace(" ","_")}" +
                        "|${context?.getString(R.string.diner)}:${dailyMeal.diner.replace(" ","_")}"
            }
            putExtra(Intent.EXTRA_TEXT, "https://com.applications.toms.comidasdelasemana/calendario?${query}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Comartiendo comida de la semana")
        startActivity(shareIntent)

        return super.onOptionsItemSelected(item)
    }

    // Method to show an alert dialog with yes, no and cancel button
    private fun showDialog(){
        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog
        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(requireContext())
        // Set a title for alert dialog
        builder.setTitle("Importar calendario de comidas")
        // Set a message for alert dialog
        builder.setMessage("Quiere importar el calendario de comidas?")
        // On click listener for dialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.startImportingDataFromDeepLink()
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    Toast.makeText(context,"No se importo", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // Set the alert dialog positive/yes button
        builder.setPositiveButton("YES",dialogClickListener)
        // Set the alert dialog negative/no button
        builder.setNegativeButton("NO",dialogClickListener)
        // Initialize the AlertDialog using builder object
        dialog = builder.create()
        // Finally, display the alert dialog
        dialog.show()
    }

    companion object{
        private const val TAG = "TMV-CalendarFragment"
    }

}