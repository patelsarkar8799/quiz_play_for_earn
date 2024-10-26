package com.harsh.myapplication.Adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harsh.myapplication.Models.CategoryModel
import com.harsh.myapplication.databinding.ItemCategoryBinding

class CategoryAdapter(categoryList1: Context, var categoryList: ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){
    class CategoryViewHolder (var binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = categoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val dataList : CategoryModel = categoryList[position]
        dataList.categoryImage?.let { holder.binding.image.setImageResource(it) }
        holder.binding.category.text = dataList.categoryName
    }
}
