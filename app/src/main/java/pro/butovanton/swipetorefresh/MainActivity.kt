package pro.butovanton.swipetorefresh

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import pro.butovanton.swipetorefresh.databinding.ActivityMainBinding
import pro.butovanton.swipetorefresh.repo.Repo
import pro.butovanton.swipetorefresh.server.Server

class MainActivity : AppCompatActivity() {

    val repo = Repo(Server())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val adapterRacycler = Adapter(inflater = layoutInflater)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.recyclerView.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = adapterRacycler
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        setContentView(binding.root)

        adapterRacycler.add(repo.getData().toMutableList())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

}