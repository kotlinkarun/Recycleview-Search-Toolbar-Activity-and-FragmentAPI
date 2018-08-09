package com.demo

import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {


    private  var list:MutableList<Model> = mutableListOf()
    private  var mAdapter:DataAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        initViews()
        loadJSON()
    }

    private fun initViews() {
        card_recycler_view!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        card_recycler_view!!.layoutManager = layoutManager
    }


    private fun loadJSON() {

        AndroidNetworking.get("http://192.168.43.121:8080/api/search")
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray?) {
                        for (i in 0 until response!!.length()) {
                            val obj = response.getJSONObject(i)
                            val model = Model(
                                    obj.getString("name"), obj.getString("avatar"))
                            list.add(model)
                        }
                        mAdapter = DataAdapter(list)
                        card_recycler_view.adapter = mAdapter

                    }

                    override fun onError(anError: ANError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val search = menu.findItem(R.id.search)
        val searchView = MenuItemCompat.getActionView(search) as SearchView
        search(searchView)
        return true
    }



    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                try {
                    mAdapter!!.filter.filter(newText)
                } catch (e: Exception) {
                }
                return true
            }
        })
    }
}
