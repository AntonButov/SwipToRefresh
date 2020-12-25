package pro.butovanton.swipetorefresh

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

import pro.butovanton.swipetorefresh.databinding.ActivityMainBinding
import pro.butovanton.swipetorefresh.repo.Repo

class MainActivity : AppCompatActivity(), Adapter.SelectInterface {

    private val model: MainViewModel by viewModels()

    private var isLoading = false

    lateinit var binding: ActivityMainBinding
    lateinit var adapterRecycler: Adapter

    private lateinit var disposableGetData: Disposable

    private lateinit var menu: Menu

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
                       getNextPage()
                }
               }
            })
        }

        findViewById<FloatingActionButton>(R.id.fabDelete).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        setContentView(binding.root)

        disposableGetData = model.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
          adapterRecycler.add(data)
                isLoading = false
                binding.swiperefresh.isRefreshing = false
            }
        getNextPage()

        model.setListnererrorServer(object: Repo.ErrorServer {
            override fun errorServer() {
        runOnUiThread() {
            if (adapterRecycler.itemCount == 1)
            showErrorSnack()
            else {
                runOnUiThread { adapterRecycler.add(DataRecycler.Error()) }
            }
            isLoading = false
        }
        }
        })

        binding.fabDelete.setOnClickListener {
        val killList = adapterRecycler.delete()
        model.delete(killList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable?) { }

                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable?) {
                        adapterRecycler.add(killList)
                        showErrorSnack()
                    }
                })
    }

    binding.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
        override fun onRefresh() {
            model.reload()
        }
    })
    }

    private fun getNextPage() {
         if (isLoading == false) {
            isLoading = true
            model.loadData()
        }
    }

    private fun showErrorSnack() {
        Snackbar.make(binding.root, "Ошибка сервера", Snackbar.LENGTH_INDEFINITE)
                .setAction("Повотроить", object : View.OnClickListener{
                    override fun onClick(v: View?) {
                        getNextPage()
                    }
                })
                .show()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        return true
    }

    override fun selectOff() {
        binding.fabDelete.visibility = View.INVISIBLE
    }

    override fun selectOnn() {
        binding.fabDelete.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableGetData.dispose()
    }
}