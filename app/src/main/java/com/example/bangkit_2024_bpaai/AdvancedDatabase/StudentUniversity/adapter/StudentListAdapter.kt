package com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database.Student
import com.example.bangkit_2024_bpaai.databinding.ItemStudentBinding

class StudentListAdapter :
    PagedListAdapter<Student, StudentListAdapter.WordViewHolder>(WordsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val student = getItem(position) as Student
        holder.bind(student)
    }

    class WordViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Student) {
            data.name.also { binding.tvItemName.text = it }
        }
    }

    class WordsComparator : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.name == newItem.name
        }
    }
}