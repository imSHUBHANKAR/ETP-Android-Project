package com.example.easyfood

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.adapters.MealRecyclerAdapter
import com.example.easyfood.data.pojo.Meal
import com.example.easyfood.data.pojo.MealDetail
import com.example.easyfood.databinding.FragmentSearchBinding
import com.example.easyfood.mvvm.SearchMVVM
import com.example.easyfood.ui.HomeFragment
import com.example.easyfood.ui.MealDetailesActivity

class SearchFragment : Fragment() {
    private lateinit var myAdapter: MealRecyclerAdapter
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchMvvm: SearchMVVM
    private var mealId = ""
    private var mealStr = ""
    private var mealThub = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = MealRecyclerAdapter()
        searchMvvm = ViewModelProviders.of(this)[SearchMVVM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSearchClick()
        observeSearchLiveData()
        setOnMealCardClick()
    }

    private fun setOnMealCardClick() {
        binding.searchedMealCard.setOnClickListener {
            val intent = Intent(context, MealDetailesActivity::class.java)

            intent.putExtra(HomeFragment.MEAL_ID, mealId)
            intent.putExtra(HomeFragment.MEAL_STR, mealStr)
            intent.putExtra(HomeFragment.MEAL_THUMB, mealThub)

            startActivity(intent)


        }
    }

    private fun onSearchClick() {
        binding.icSearch.setOnClickListener {
            searchMvvm.searchMealDetail(binding.edSearch.text.toString())

        }
    }

    private fun observeSearchLiveData() {
        searchMvvm.observeSearchLiveData()
            .observe(viewLifecycleOwner, object : Observer<MealDetail> {
                override fun onChanged(t: MealDetail?) {
                    binding.apply {

                        mealId = t!!.idMeal
                        mealStr = t.strMeal
                        mealThub = t.strMealThumb

                        Glide.with(context!!.applicationContext)
                            .load(t!!.strMealThumb)
                            .into(imgSearchedMeal)

                        tvSearchedMeal.text = t.strMeal
                        searchedMealCard.visibility = View.VISIBLE


                    }
                }
            })
    }


}