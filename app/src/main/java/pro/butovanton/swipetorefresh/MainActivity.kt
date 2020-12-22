package pro.butovanton.swipetorefresh

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.View
import androidx.annotation.MainThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import pro.butovanton.swipetorefresh.databinding.ActivityMainBinding
import pro.butovanton.swipetorefresh.repo.Repo
import pro.butovanton.swipetorefresh.server.Server
import java.util.*

class MainActivity : AppCompatActivity(), Adapter.SelectInterface {

    private val repo = Repo(Server())
    private var isLoading = false

    lateinit var binding: ActivityMainBinding
    lateinit var adapterRecycler: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        adapterRecycler = Adapter(inflater = layoutInflater, this)
        binding.recyclerView.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = adapterRecycler
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
               super.onScrolled(recyclerView, dx, dy)
               val last = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                   if (last == recyclerView.adapter!!.itemCount - 1 && dy != 0 ) {
                       getData()
                }
               }
            })
        }

        findViewById<FloatingActionButton>(R.id.fabDelete).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        setContentView(binding.root)

    getData()

    binding.fabDelete.setOnClickListener {
        adapterRecycler.delete()
    }

    binding.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
        override fun onRefresh() {
            getData()
        }
    })
    }

    private fun getData() {
        if (isLoading == false) {
            Log.d("DEBUG", "getData")
            isLoading = true
            binding.swiperefresh.isRefreshing = true
            repo.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    if (isDataError(data) && adapterRecycler.itemCount == 1) {
                        showErrorSnack()
                    }
                    adapterRecycler.add(data)
                    isLoading = false
                    binding.swiperefresh.isRefreshing = false
                }
        }
    }

    private fun isDataError(data: List<DataRecycler>) = data.size == 1 && (data[0] is DataRecycler.Error)

    private fun showErrorSnack() {
        Snackbar.make(binding.root, "Ошибка сервера", Snackbar.LENGTH_SHORT)
                .setAction("Повотроить", object : View.OnClickListener{
                    override fun onClick(v: View?) {
                        getData()
                    }
                })
                .show()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun selectOff() {
        binding.fabDelete.visibility = View.INVISIBLE
    }

    override fun selectOnn() {
        binding.fabDelete.visibility = View.VISIBLE
    }

}