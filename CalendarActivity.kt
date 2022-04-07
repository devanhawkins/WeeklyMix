package com.example.weeklymix

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import java.text.DateFormat
import java.util.*


class CalendarActivity : AppCompatActivity() {

    private val theDate: MutableList<String> = mutableListOf()
    private var theList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_calendar)

        //Get and set list from the intent extra message
        val events = intent.getStringExtra(EXTRA_MESSAGE)
        val tv = findViewById<TextView>(R.id.importList).apply {
            text = events.toString()
        }

        //Set of Calendar
        val calendar = Calendar.getInstance()
        val calDate = findViewById<CalendarView>(R.id.cal)
        calDate.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            calDate.date = calendar.timeInMillis

            //formatting
            val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)

            val datelist =  findViewById<TextView>(R.id.date_list).apply {
                theDate += dateFormatter.format(calendar.time)
                text = theDate.toString()
            }

        }

    }


    fun toShuffle(view: View){
        //Get the date list from the schedule textView
        val tv = findViewById<TextView>(R.id.date_list)
        val m = tv.text.toString()
        val tv_list: List<String> = m.split(",").toList()
        var ratio = false

        //Put the date-event pairs into theList list
        val events = intent.getStringExtra(EXTRA_MESSAGE).toString()
        val ev_list: List<String> = events.split(",").toList()

        for(i in 0..ev_list.size - 1) {

            //Safe call of tv_list
            if (!tv_list[i].isNullOrBlank()){
                //theList.add(ev_list[i] + "," + tv_list[i])
                theList += ev_list[i] + "," + tv_list[i]
            } else {
                //theList.add(ev_list[i] + "," + tv_list[0])
                theList += ev_list[i] + "," + tv_list[0]
            }
            //println(theList)
        }

        //Put message 'm' in extra
        val intent = Intent(this, ShuffleActivity::class.java).apply {
            val finalList = theList.toString()
            putExtra(EXTRA_MESSAGE, finalList)
            //println(theList)
            //println(finalList)


            //if the number of dates is too few compared to the number of events, don't proceed.
            //length!!.toInt() >= tv_list.size && length!!.toInt() < tv_list.size * 3
            if(ev_list.size == tv_list.size){
                ratio = true
            }
        }

        //Start next activity if num of dates corresponds to num of events
        if(ratio == true){

            startActivity(intent)
        }



    }
}