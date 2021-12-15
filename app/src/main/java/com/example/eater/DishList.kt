package com.example.eater

import android.R.attr
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity.LEFT
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.view.GravityCompat
import android.R.attr.y

import android.R.attr.x

import android.widget.ImageView
import java.io.File
import java.io.FileInputStream
import android.R.attr.y

import android.R.attr.x
import android.content.Context
import android.content.ContextWrapper
import android.graphics.*
import androidx.core.graphics.drawable.toBitmap
import com.amulyakhare.textdrawable.TextDrawable
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable








class DishList : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish_list)

        //setting custom toolbar as default
        val my_toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.my_toolbar)
        setSupportActionBar(my_toolbar)
        //ends


        //drawer layout code starts here
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val nav_view = findViewById<NavigationView>(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        //setting custom toolbar image
        supportActionBar?.setHomeButtonEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeAsUpIndicator(R.drawable.eater_logo);//your icon here
        //ends


        //change email id in drawer
        changeEmailDrawer(nav_view)
        //ends

        //change photo in drawer
        changePhotoDrawer(nav_view)
        //ends

        //on item click listener
        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){

                R.id.profile -> {
                    val intent = Intent(this, Profile::class.java)
                                startActivity(intent)
                }

                R.id.myOrders -> {
//                    val intent = Intent(this, Profile::class.java)
//                    startActivity(intent)
                    Toast.makeText(applicationContext,
                        "my orders", Toast.LENGTH_SHORT).show()
                }

                R.id.otherUsers -> {
//                    val intent = Intent(this, Profile::class.java)
//                    startActivity(intent)
                    Toast.makeText(applicationContext,
                        "other users", Toast.LENGTH_SHORT).show()
                }

            }
            true

        }


        //drawer layout code ends here


        // here on recycler view code


        var dishes: DishListResponse
        var dishesAdapter = listOf<Dish>()

        val allDishes = findViewById<RecyclerView>(R.id.allDishes)

        val myApp = application as MyDishApplication
        val httpApiService = myApp.httpApiService

        CoroutineScope(Dispatchers.IO).launch {

            dishes = httpApiService.getAllDishes()     //http req here

            if(dishes.dishes != null){
                dishesAdapter = dishes.dishes!!
            }

            withContext(Dispatchers.Main){
                allDishes.adapter = MyDishAdapter(dishesAdapter)

                allDishes.layoutManager = LinearLayoutManager(applicationContext)
            }

        }

        //rc view code ends here

    }

    private fun changePhotoDrawer(nav_view: NavigationView) {
        val drawerEmailIdValue = "eater@gmail.com"
        val photoText = drawerEmailIdValue.substring(0,1).toUpperCase()

        val drawerPhoto = nav_view.getHeaderView(0).findViewById<ImageView>(R.id.drawerProfileImage)

        val f = File("/data/data/com.example.eater/app_imageDirectory", "profileImage.jpg")

        if(f.exists()){
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            drawerPhoto.setImageBitmap(b)
        }else{
            val drawableDrawerImage = TextDrawable.builder()
                .buildRound(photoText, Color.RED) // radius in px

            drawerPhoto.setImageDrawable(drawableDrawerImage)
        }


    }

    override fun onBackPressed() {
        finishAffinity()
        finish()
    }

//    private fun deleteImageFile(){
//        val f = File("/data/data/com.example.eater/app_imageDirectory", "profileImage.jpg")
//        val deleted = f.delete()
//        if(deleted){
//            //clear shared preference and then logout
//        }
//        else{
//            Toast.makeText(applicationContext, "Some error occurred", Toast.LENGTH_LONG).show()
//        }
//    }

    private fun changeEmailDrawer(nav_view: NavigationView) {
        val drawerEmailIdValue = "eater@gmail.com"
        val drawerEmailId = nav_view.getHeaderView(0).findViewById<TextView>(R.id.drawerEmailId)
        drawerEmailId.text = drawerEmailIdValue
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}