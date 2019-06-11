package br.com.hussan.enjoeitest.ui.productdetails

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.hussan.enjoeitest.AppNavigator
import br.com.hussan.enjoeitest.R
import br.com.hussan.enjoeitest.databinding.ActivityProductBinding
import br.com.hussan.enjoeitest.domain.Product
import kotlinx.android.synthetic.main.activity_product.*


class ProductDetailsActivity : AppCompatActivity() {
    private val product: Product
        get() = intent.getSerializableExtra(AppNavigator.PRODUCT) as Product


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityProductBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_product
        )

        binding.product = product

        setupToolbar()
        txtOriginalPrice.paintFlags = txtOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.share -> {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_TEXT, product.title)
                startActivity(Intent.createChooser(i, getString(R.string.share_msg)))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
