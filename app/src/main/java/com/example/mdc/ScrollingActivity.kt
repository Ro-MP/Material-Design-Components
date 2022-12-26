package com.example.mdc


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.webkit.URLUtil
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mdc.databinding.ActivityScrollingBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ScrollingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScrollingBinding
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var fab: ExtendedFloatingActionButton
    private lateinit var chips: ChipGroup
    private lateinit var buttonLoveIt: Button
    private lateinit var imageViewIconLoveIt: ImageView
    private lateinit var checkBoxIconFavorite: CheckBox
    private lateinit var cardView: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomAppBar = binding.bottomAppBar
        fab = binding.extendedFab
        chips = binding.layoutContentScrolling.chipGroup
        buttonLoveIt = binding.layoutContentScrolling.btnLoveCard
        imageViewIconLoveIt = binding.layoutContentScrolling.iconLoveIt
        checkBoxIconFavorite = binding.layoutContentScrolling.iconFavorite
        cardView = binding.layoutContentScrolling.cardview


        with(checkBoxIconFavorite){
            if (this.isChecked){
                this.buttonTintList = getColorStateList(R.color.orange)
            }
            else {
                this.buttonTintList = getColorStateList(R.color.pink)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        bottomAppBar.setNavigationOnClickListener {
            showSnackbar("Holis from Nav")

        }

        bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.menu_cookie -> {
                    showSnackbar("Holis from Cockie")
                    true
                }
                R.id.menu_cruelty_free -> {
                    showSnackbar("Holis from Cruelty Free")
                    true
                }
                R.id.menu_dashboard -> {
                    showSnackbar("Holis from Dashboard")
                    true
                }
                else -> false
            }
        }

        binding.layoutContentScrolling.btnBuy.setOnClickListener {
            Toast.makeText(this, "Bought", Toast.LENGTH_LONG).show()
        }

        var index = 0
        fab.setOnClickListener {
            Snackbar.make(binding.root, "Holis from FAB", Snackbar.LENGTH_SHORT)
                .setAction("Dont push"){
                    fab.setBackgroundColor(changeColorId(index))
                    fab.setTextColor(getColor(R.color.black))
                    fab.iconTint = getColorStateList(R.color.black)
                    fab.text = getString(R.string.fab_dont_do)
                    index++
                    if (index >= 5) index = 0
                }
                .setAnchorView(fab)
                .show()
        }

        chips.setOnCheckedStateChangeListener { _, checkedIds ->
            println(checkedIds)

            if (checkedIds.contains(binding.layoutContentScrolling.chipBacardi.id)){
                binding.layoutContentScrolling.tvBacardi.visibility = TextView.VISIBLE
            }else {
                binding.layoutContentScrolling.tvBacardi.visibility = TextView.GONE
            }
            if (checkedIds.contains(binding.layoutContentScrolling.chipCoco.id)){
                binding.layoutContentScrolling.tvCoco.visibility = TextView.VISIBLE
            }else {
                binding.layoutContentScrolling.tvCoco.visibility = TextView.GONE
            }
        }

        buttonLoveIt.setOnClickListener {

            if(imageViewIconLoveIt.visibility == ImageView.VISIBLE) {
                imageViewIconLoveIt.visibility = ImageView.INVISIBLE
                buttonLoveIt.text = getString(R.string.btn_love_it)
            } else {
                imageViewIconLoveIt.visibility = ImageView.VISIBLE
                buttonLoveIt.text = getString(R.string.btn_dont_love_it)

            }
        }

        checkBoxIconFavorite.setOnCheckedChangeListener { checkBox, isCheked ->
            if (isCheked){
                checkBox.buttonTintList = getColorStateList(R.color.orange)
            }
            else {
                checkBox.buttonTintList = getColorStateList(R.color.pink)
            }
        }

        cardView.setOnClickListener {
            showSnackbar("Holis from CardView")
        }


        with(binding.layoutContentScrolling){
            var wasPressed = false
            this.btnShowImage.setOnClickListener {
                if (wasPressed) {
                    Toast.makeText(this@ScrollingActivity, "image has already loaded", Toast.LENGTH_SHORT).show()
                } else{
                    loadImage()
                    wasPressed = true
                }
            }
        }

        binding.layoutContentScrolling.etUrl.editText?.setOnFocusChangeListener { _, focused ->
            var errorStr: String? = null
            val url = binding.layoutContentScrolling.etUrl.editText?.text.toString()
            Toast.makeText(this@ScrollingActivity, "holis", Toast.LENGTH_SHORT).show()

            binding.layoutContentScrolling.etUrl.error =
                if (!focused){
                    if (url.isEmpty()){
                        errorStr = "URL required"
                    } else if (URLUtil.isValidUrl(url)){
                        loadImage(url)
                    } else {
                        errorStr = "Invalid URL"
                    }
                    errorStr
                } else {
                    null
                }
        }
    }

    private fun changeColorId(index: Int): Int  {
        val colors = listOf(
            R.color.pink, R.color.orange, R.color.blue, R.color.purple_200, R.color.teal_200
        )
        return getColor(colors[index])
    }

    private fun showSnackbar(text: String){
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT)
            .setAnchorView(fab)
            .show()
    }

    private fun loadImage(url: String = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"){
        Glide.with(binding.root)
            .load(url)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .into(binding.layoutContentScrolling.imgCard)
    }

}


