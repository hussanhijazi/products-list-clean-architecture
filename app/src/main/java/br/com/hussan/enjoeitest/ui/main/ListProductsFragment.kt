package br.com.hussan.enjoeitest.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import br.com.hussan.enjoeitest.AppNavigator
import br.com.hussan.enjoeitest.R
import br.com.hussan.enjoeitest.domain.Product
import br.com.hussan.enjoeitest.extensions.add
import br.com.hussan.enjoeitest.extensions.hide
import br.com.hussan.enjoeitest.extensions.show
import br.com.hussan.enjoeitest.extensions.snack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.lyt_loading.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ListProductsFragment : Fragment() {

    private val viewModel: ProductsViewModel by viewModel()
    private val navigator: AppNavigator by inject { parametersOf(activity) }
    private val compositeDisposable = CompositeDisposable()
    private val productAdapter by lazy { ProductsAdapter(::goToDetails) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_products, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewProducts()
        setupSwipeRefresh()
        getProducts()
    }

    private fun setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            getProducts()
        }
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

    private fun showProducts(items: List<Product>) {
        if (items.isNotEmpty()) {
            productAdapter.setItems(items)
            showRecyclerViewFacts()
            if (swipeRefresh.isRefreshing)
                swipeRefresh.isRefreshing = false
        } else
            showEmptyState()
    }

    private fun showEmptyState() {
        rvProducts.hide()
    }

    private fun showRecyclerViewFacts() {
        rvProducts.show()
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

    private fun setupRecyclerViewProducts() {
        rvProducts.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 2)
            adapter = productAdapter
        }
    }

    private fun goToDetails(product: Product, view: View) {
        navigator.navigateToProductDetails(product, view)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }


}