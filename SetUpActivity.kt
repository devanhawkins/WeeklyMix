package com.example.weeklymix

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.weeklymix.databinding.ActivitySetUpBinding
import java.util.*


class SetUpActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySetUpBinding

    //Set up variables: event list
    private val theList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySetUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

        }


        //Get hours from Home Activity, and display in text widget
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val listLength = message.toString().toInt()

        val textView = findViewById<TextView>(R.id.hours).apply {
            text = message + " hour(s) left."
        }
        val textView2 = findViewById<TextView>(R.id.schedule).apply {
            text = theList.toString()
        }


        //Set the onClickListener for the Add button
        val addEvent = findViewById<Button>(R.id.add_event)
        addEvent.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)

            //Set up the input field
            val input = EditText(this)
            input.hint = "Event Name"
            input.inputType = InputType.TYPE_CLASS_TEXT
            dialogBuilder.setView(input)


            //Set the message
            dialogBuilder.setMessage("Please add the event name.")
                .setCancelable(false)
                .setPositiveButton(R.string.add_text, DialogInterface.OnClickListener{
                    dialog, which ->
                    val mText = input.text.toString()
                    addEvent(mText)
                })
                .setNegativeButton(R.string.cancel_text, DialogInterface.OnClickListener{
                    dialog, id ->
                })


            //Create the dialog box
            val alert = dialogBuilder.create()
            alert.setTitle("Add an Event")

            //If list is full, not show dialog.
            if (theList.size < listLength){
                alert.show()
            }
        }

        //Set the onClickListener for the Remove button
        val remEvent = findViewById<Button>(R.id.del_event)
        remEvent.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)

            //Set the buttons
            dialogBuilder.setMessage("Do you want to remove the last event?")
                .setCancelable(true)
                .setPositiveButton(R.string.del_button, DialogInterface.OnClickListener{
                    dialog, id -> removeEvent()
                })
                .setNegativeButton(R.string.cancel_text, DialogInterface.OnClickListener{
                    dialog, id ->
                })

            //Create the dialog box
            val alert = dialogBuilder.create()
            alert.setTitle("Delete event")
            //If the list is empty, do nothing.
            if (theList.size != 0){
                alert.show()
            }
        }

    }

    //Reads info from the activity's widgets and appends it to the list
    fun addEvent(eventName: String) {

        //Add text to list (textView -> schedule)
        theList += eventName
        val textview = findViewById<TextView>(R.id.schedule)
            .apply {
                text = theList.toString()
            }
        //Minus one from count (TextView ->hours)
        val h = findViewById<TextView>(R.id.hours).apply {
            var temp = text.toString()

            //Replace all non-numerical characters with nothingness (delete them).
            val regex = Regex("[^0-9]")
            var onlyNumbs = regex.replace(temp, "")

            //Check if 0, if not minus one. If so, exit.
            if (onlyNumbs.toInt() == 0) {
                return
            } else {
                text = (onlyNumbs.toInt() - 1).toString() + " hour(s) left."
            }
        }

    }

    //Removes the last name-date pair from the list.
    fun removeEvent() {
        val maxMessage = intent.getStringExtra(EXTRA_MESSAGE)
        val maxInt = maxMessage?.toInt()

        val h = findViewById<TextView>(R.id.hours).apply {
            var temp = text.toString()
            //Replace all non-numerical characters with nothingness (delete them).
            val regex = Regex("[^0-9]")
            var onlyNumbs = regex.replace(temp, "")
            //Check if already at max. If not, minus one and delete last entry on theList.
            if (onlyNumbs.toInt() < maxInt!!) {
                text = (onlyNumbs.toInt() + 1).toString() + " hour(s) left."
            } else {
                //nothing
            }
        }

        val s = findViewById<TextView>(R.id.schedule).apply {
            if (theList.isNotEmpty()){
                theList.removeAt(theList.size - 1)
                text = theList.toString()
            }

        }
    }

    //Send event list to next activity
    fun toCalendar(view: View) {
        //Get the last from the schedule textView
        val tv = findViewById<TextView>(R.id.schedule)
        val m = tv.text.toString()

        //Put message 'm' in extra
        val intent = Intent(this, CalendarActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, m)
        }

        //Start next activity
        startActivity(intent)
    }


}