package com.example.ex11

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_main.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var context = this
        val databaseHandler: DBHandler = DBHandler(context = context)
        var nameList = databaseHandler.viewAllNames()
        Log.d("ONCREATE current namelist size", nameList.size.toString())
        listView.adapter = myCustomAdapter(context, nameList)

        deleteBtn.setOnClickListener {
            deleteRecord(nameList)
            listView.adapter = myCustomAdapter(context, nameList)
        }

    }

    class myCustomAdapter(context: Context, nameListParameter: List<String>): BaseAdapter(){
        //val databaseHandler: DBHandler = DBHandler(context = context)
        var nameList = nameListParameter

        override fun notifyDataSetChanged() {
            super.notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val rowMain: View

            if (convertView == null){
                val layoutInflator  = LayoutInflater.from(viewGroup!!.context)
                rowMain = layoutInflator.inflate(R.layout.row_main, viewGroup, false)
                Log.d("getview", "layoutInflator")
                val viewHolder = ViewHolder(rowMain.nameView, rowMain.idView)
                rowMain.tag = viewHolder
            } else {
                rowMain = convertView
            }

            val viewHolder = rowMain.tag as ViewHolder
            viewHolder.nameTextView.text = "Movie: " + nameList[position].substring(7)
            viewHolder.idTextView.text = nameList[position].substring(0, 7)

            val greyColor = Color.parseColor("#E0E0E0")
            val whiteColor = Color.parseColor("#FFFFFF")
            if (position%2 == 0){
                rowMain.setBackgroundColor(whiteColor)
            } else{
                rowMain.setBackgroundColor(greyColor)
            }


            return rowMain
        }

        override fun getItem(position: Int): Any {
            return nameList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return nameList.size
        }


        private class ViewHolder(val nameTextView: TextView, val idTextView: TextView)
    }


    fun saveRecord(view: View) {
        val source = "123456789"
        val random_id = (1..4).map { source.random() }.joinToString("1")
        val id = random_id

        val name = addField.text.toString()
        val databaseHandler: DBHandler = DBHandler(this)

        if (name.trim() != "") {
            val status = databaseHandler.addUser(Integer.parseInt(id), name)
            if (status > -1) {
                Toast.makeText(applicationContext, "Record Saved!", Toast.LENGTH_SHORT).show()
                addField.text.clear()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "name cannot be blank",
                Toast.LENGTH_SHORT
            ).show()
        }

        var context = this
        var newNameList = databaseHandler.viewAllNames()

        listView.adapter = myCustomAdapter(context, newNameList)
    }

    fun deleteRecord(nameList: List<String>) {

        val delete_me = Integer.parseInt(deleteField.text.toString())
        Log.d("DELETE caught from field", delete_me.toString())
        Log.d("DELETE current namelist size", nameList.size.toString())


        var context = this

        if (delete_me >= nameList.size) {
            return
        }

        val id_to_be_deleted = nameList[delete_me].substring(0,7)

        val databaseHandler: DBHandler = DBHandler(this)
        databaseHandler.deleteUser(Integer.parseInt(id_to_be_deleted))

        var newNameList = databaseHandler.viewAllNames()
        Log.d("DELETE new namelist size", newNameList.size.toString())

        listView.adapter = myCustomAdapter(context, newNameList)

    }


}
