package com.example.eater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DishList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish_list)

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

    }
}