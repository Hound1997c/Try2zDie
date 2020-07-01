package com.example.try2zdie

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import android.util.Log
import java.lang.Double.parseDouble

class MainActivity : AppCompatActivity() {

    var items: MutableList<ArrayList<String>> = mutableListOf()
    var Url: String = "http://fcds.cs.put.poznan.pl/MyWeb/BL/"

    var projectId: Int = 0
    var projectCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun addProject(view: View) {
        Log.d("TAG", "Project\n")
        projectCode = textView.text.toString()

        /*var numeric = true

        try {
            val num = parseDouble(projectCode)
        } catch (e: NumberFormatException) {
            numeric = false
        }
        if(!numeric) return */

        AsyncDownload(this).execute("$Url$projectCode.xml")

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        databaseHandler.addPartsProject(items)
        items.clear()

    }

    inner class AsyncDownload : AsyncTask<String, String, Boolean> {

        var contextAdd: Context
        var itemsCount: Int = 0

        constructor(context: Context) : super() {
            contextAdd = context
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: String?): Boolean {
            var projectUrl = URL("$Url$projectCode.xml")
            Log.d("TAG", "Background\n")
            var started = false
            var tmp: ArrayList<String> = ArrayList()
            lateinit var instream: InputStream
            try {
                var urlconn = projectUrl.openConnection()
                urlconn.connect()
                instream = urlconn.getInputStream()
            } catch (e: Exception) {
                return false
            }
            var factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(InputStreamReader(instream))
            while (xpp.eventType != XmlPullParser.END_DOCUMENT) {
                if (xpp.eventType == XmlPullParser.START_TAG && xpp.name == "ITEM") {
                    started = true
                } else if (xpp.eventType == XmlPullParser.END_TAG && xpp.name == "ITEM" && started) {
                    Log.d("ITEM", tmp.toString())
                    items.add(tmp.clone() as ArrayList<String>)
                    tmp.clear()
                    started = false
                } else if (xpp.eventType == XmlPullParser.TEXT) {
                    if (started && !xpp.text.contains("\n"))
                        tmp.add(xpp.text)
                }
                xpp.next()
            }
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            Log.d("TAG", "POST\n")
            textView.text = items.size.toString()
        }
    }

    fun viewRecord(view: View) {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val emp: List<PartClass> = databaseHandler.viewEmployee()
        val empArrayId = Array<String>(emp.size) { "0" }
        val empArrayType = Array<String>(emp.size) { "null" }
        val empArrayQuantN = Array<String>(emp.size) { "null" }
        val empArrayQuantH = Array<String>(emp.size) { "null" }
        val empArrayColor = Array<String>(emp.size) { "null" }
        var index = 0
        for (e in emp) {
            empArrayType[index] = e.itemType
            empArrayId[index] = e.itemId
            empArrayQuantN[index] = e.quantityNeed.toString()
            empArrayQuantH[index] = e.quantityHave.toString()
            empArrayColor[index] = e.colorId.toString()
            index++
        }
        //creating custom ArrayAdapter
        val myListAdapter = MyListAdapter(this, empArrayType, empArrayId, empArrayQuantN,
                                            empArrayQuantH, empArrayColor)
        listView.adapter = myListAdapter
    }
}