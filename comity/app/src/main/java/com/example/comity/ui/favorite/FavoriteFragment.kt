package com.example.comity.ui.favorite

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.comity.R
import com.example.comity.SelectedStudyGroupsViewModel
import com.example.comity.databinding.FragmentFavoriteBinding
import com.example.comity.ui.home.StudyGroup
import org.w3c.dom.Text

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var selectedStudyGroupsViewModel: SelectedStudyGroupsViewModel
    private lateinit var favoriteListView: ListView
    private lateinit var favoriteAdapter: ArrayAdapter<StudyGroup>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        favoriteViewModel = ViewModelProvider(requireActivity()).get(FavoriteViewModel::class.java)
        selectedStudyGroupsViewModel = ViewModelProvider(requireActivity()).get(
            SelectedStudyGroupsViewModel::class.java
        )

        val textView: TextView = binding.textFavorite
        favoriteViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        favoriteListView = binding.favoriteListView

        favoriteAdapter = object : ArrayAdapter<StudyGroup>(
            requireContext(),
            R.layout.box_item,
            selectedStudyGroupsViewModel.selectedStudyGroups.value?.toList() ?: emptyList()
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(
                    R.layout.box_item,
                    parent,
                    false
                )

                val item = getItem(position)

                // item elements
                val groupImage = view.findViewById<ImageView>(R.id.image_group)
                val groupNameText = view.findViewById<TextView>(R.id.text_group_name)
                val leaderNameText = view.findViewById<TextView>(R.id.text_leader_name)
                val currentNumText = view.findViewById<TextView>(R.id.text_current_number)
                val maxNumText = view.findViewById<TextView>(R.id.text_max_number)
                val joinButton = view.findViewById<Button>(R.id.button_join)
                val groupDescriptionText = view.findViewById<TextView>(R.id.text_description)
                val groupLocation = view.findViewById<TextView>(R.id.text_location)

                item?.let {
                    groupImage.setImageResource(it.imageResource)
                    groupNameText.text = it.groupName
                    leaderNameText.text = it.leaderName
                    currentNumText.text = it.currentNum.toString()
                    maxNumText.text = "(" + it.maxNum.toString() + ")"
                    groupDescriptionText.text = it.groupDescription
                    groupLocation.text = it.groupLocation
                }

                joinButton.visibility = View.GONE

                return view
            }
        }

        favoriteListView.adapter = favoriteAdapter

        favoriteListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                onStudyGroupSelected(position)
            }

        return root
    }

    private fun onStudyGroupSelected(position: Int) {
        // Handle study group selection if needed
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
