package br.com.hussan.enjoeitest.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.hussan.enjoeitest.AppNavigator
import br.com.hussan.enjoeitest.R
import br.com.hussan.enjoeitest.domain.Product
import br.com.hussan.enjoeitest.extensions.add
import br.com.hussan.enjoeitest.extensions.hide
import br.com.hussan.enjoeitest.extensions.show
import br.com.hussan.enjoeitest.extensions.snack
import com.amitshekhar.DebugDB
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.lyt_loading.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductsActivity : AppCompatActivity() {

    private val viewModel: ProductsViewModel by viewModel()
    private val navigator: AppNavigator by inject { parametersOf(this@ProductsActivity) }
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        setupRecyclerViewFacts()
        getProducts()

        Log.d("h2", DebugDB.getAddressLog())

    }
    private fun getProducts() {
        viewModel.getProducts(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading(true) }
            .doOnComplete { showLoading(false) }
            .doOnError { showLoading(false) }
            .subscribe(::showProducts, ::showError)
            .add(compositeDisposable)
    }
    private fun showProducts(list: List<Product>) {
        Log.d("h2", list.toString())
        if (list.isNotEmpty()) {
            // TODO Set items
            showRecyclerViewFacts()
        } else
            showEmptyState()
    }

    private fun showEmptyState() {
        rvProducts.hide()
        lytEmptyState.show()
    }

    private fun showRecyclerViewFacts() {
        rvProducts.show()
        lytEmptyState.hide()
    }

    private fun showError(error: Throwable) {
        Log.d("h2", error.message)
        lytRoot.snack(R.string.error_message)
    }

    private fun showLoading(show: Boolean) {
        if (show)
            progressBar.show()
        else
            progressBar.hide()
    }
    private fun setupRecyclerViewFacts() {
        rvProducts.run {
            setHasFixedSize(true)
            // TODO Set Adapter
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}