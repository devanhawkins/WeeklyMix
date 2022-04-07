package com.example.weeklymix

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.ColorsColumns
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.weeklymix.databinding.ActivityShuffleBinding
import java.util.*
import java.util.Calendar
import java.util.stream.Collectors.toList

class ShuffleActivity : AppCompatActivity() {

    private var dateList: List<String> = listOf("")
    private var eventList: List<String> = listOf("")

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityShuffleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityShuffleBinding.inflate(layoutInflater)
     setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_shuffle)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        //Format and print to textView
        var theList = intent.getStringExtra(EXTRA_MESSAGE)
        var rawList = theList.toString()
        //println(theList.toString())
        val regex = Regex("\\[|\\]")
        var noBrackList = regex.replace(rawList, "")


        //put info into two lists
        val pairList = noBrackList.split(",")

        //First element in each list is a dummy variable. Deleted after loop.


        for(i in 0..(pairList.size / 2) - 1) {
            eventList += pairList[i*2]
            dateList += pairList[i*2+1]
        }
        eventList = eventList.drop(1)
        dateList = dateList.drop(1)

        //Print the list to the page
        val tv = findViewById<TextView>(R.id.list).apply{
            //text = noBrackList
            text = eventList.toString() + "\n" + dateList.toString()
        }

        //Set onCLickLisner for Roll button
        val addEvent = findViewById<Button>(R.id.roll_event)
        addEvent.setOnClickListener{
            roll()
        }

    }

    //function for the Roll button.
    //For now, generates a random number between 1 and 20.
    fun roll() {

        eventList = eventList.shuffled()
        dateList = dateList.shuffled()

        //Update Textview
        val tv = findViewById<TextView>(R.id.list).apply{
            //text = noBrackList
            text = eventList.toString() + "\n" + dateList.toString()
        }

    }

    fun addCalendarEvent(view: View) {
        if(eventList.isEmpty() || dateList.isEmpty()){
            val toast = Toast.makeText(this, "Can't add event -- No info.", Toast.LENGTH_SHORT)
            toast.show()
        }else{
            val intent = Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI)
            intent.putExtra(CalendarContract.Events.TITLE, eventList[0])
            intent.putExtra(CalendarContract.Events.ALL_DAY, true)

            //Convert time into usable format
            //val begin: Calendar = Calendar.getInstance()
            //println(begin.timeInMillis)
            //begin.set(Calendar.DAY_OF_MONTH, 12)
            //begin.set(Calendar.MONTH, Calendar.NOVEMBER)
            //begin.set(Calendar.YEAR, 2022)

            intent.putExtra(CalendarContract.Events.DESCRIPTION,dateList[0])

            startActivity(intent)
            eventList = eventList.drop(1)
            dateList = dateList.drop(1)

            //Update Textview
            val tv = findViewById<TextView>(R.id.list).apply{
            text = eventList.toString() + "\n" + dateList.toString()
            }

//            if(intent.resolveActivity(getPackageManager()) != null){
//                startActivity(intent)
//
//                //Delete first elements once they are added to the calendar
//                eventList = eventList.drop(1)
//                dateList = dateList.drop(1)
//            } else {
//                val toast2 = Toast.makeText(this, "Can't add event -- Wrong info.", Toast.LENGTH_SHORT)
//                toast2.show()
//            }



        }
    }

    override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_shuffle)
    return navController.navigateUp(appBarConfiguration)
            || super.onSupportNavigateUp()
    }

}