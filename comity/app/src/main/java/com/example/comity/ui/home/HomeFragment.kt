package com.example.comity.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.comity.R
import com.example.comity.SelectedStudyGroupsViewModel
import com.example.comity.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationView
import org.w3c.dom.Text


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var selectedStudyGroupsViewModel: SelectedStudyGroupsViewModel
    private lateinit var homeListView: ListView
    private lateinit var homeAdapter: ArrayAdapter<StudyGroup>
    private lateinit var filteredAdapter: ArrayAdapter<StudyGroup>
    private var seletedStudyGroups: MutableList<StudyGroup> = mutableListOf()
    private var filteredStudyGroups: MutableList<StudyGroup> = mutableListOf()
    private lateinit var addressTextView: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        selectedStudyGroupsViewModel = ViewModelProvider(requireActivity()).get(SelectedStudyGroupsViewModel::class.java)
        val selectedStudyGroups = selectedStudyGroupsViewModel.selectedStudyGroups.value?.toMutableList()
            ?: mutableListOf()
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Filter
        val buttonFilter = binding.buttonFilter
        buttonFilter.setOnClickListener {
            if (filteredStudyGroups.isEmpty()) { // First
                val headerNavView = requireActivity().findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)
                addressTextView = headerNavView.findViewById(R.id.appbarAddressTextView)
                val myAddress = addressTextView.text.toString()

                // Apply filter if the list is currently not filtered
                Log.d("address", myAddress)
                filterStudyGroupsByAddress(myAddress)
                buttonFilter.setBackgroundColor(Color.GRAY)
            } else { // Second
                // Revert back to the original adapter if the list is filtered
                homeListView.adapter = homeAdapter
                filteredStudyGroups.clear()
                buttonFilter.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.buttonColor
                    )
                )
            }
        }




        homeListView = binding.homeListView

        homeAdapter = object : ArrayAdapter<StudyGroup>(
            requireContext(),
            R.layout.box_item,
            homeViewModel.studyGroupList.value ?: emptyList()
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
                val groupLocationText = view.findViewById<TextView>(R.id.text_location)

                item?.let {
                    groupImage.setImageResource(it.imageResource)
                    groupNameText.text = it.groupName
                    leaderNameText.text = it.leaderName
                    currentNumText.text = it.currentNum.toString()
                    maxNumText.text = "(" + it.maxNum.toString() + ")"
                    groupDescriptionText.text = it.groupDescription
                    groupLocationText.text = it.groupLocation

                    // Update button text and background color based on joined state
                    if (it.joined) {
                        joinButton.text = "Cancel"
                        joinButton.setBackgroundColor(Color.GRAY)
                    } else {
                        joinButton.text = "Join"
                        joinButton.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primaryColor
                            )
                        )
                    }

                    joinButton.setOnClickListener {
                        val studyGroup = getItem(position)
                        studyGroup?.let {
                            if (it.joined) {
                                it.currentNum -= 1
                                it.joined = false
                                selectedStudyGroups.remove(it)
                                filteredStudyGroups.remove(it)
                                joinButton.text = "Join"
                                joinButton.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.primaryColor
                                    )
                                )
                            } else {
                                if (it.currentNum >= it.maxNum) { // full
                                    Toast.makeText(requireContext(), "Full", Toast.LENGTH_SHORT).show()
                                } else {
                                    it.currentNum += 1
                                    it.joined = true
                                    selectedStudyGroups.add(it)
                                    filteredStudyGroups.add(it)
                                    joinButton.text = "Cancel"
                                    joinButton.setBackgroundColor(Color.GRAY)
                                }
                            }

                            // Update the currentNum text
                            currentNumText.text = it.currentNum.toString()

                            selectedStudyGroupsViewModel.selectedStudyGroups.value = selectedStudyGroups
                        }
                    }

                }

                return view
            }
        }

        homeListView.adapter = homeAdapter

        homeListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                onStudyGroupSelected(position)
            }

        return root
    }

    private fun onStudyGroupSelected(position: Int) {
        // Handle study group selection here
    }

    private fun filterStudyGroupsByAddress(address: String) {
        filteredStudyGroups.clear()

        // Filter the study groups based on the address
        for (studyGroup in homeViewModel.studyGroupList.value ?: emptyList()) {
            if (studyGroup.groupLocation == address) {
                filteredStudyGroups.add(studyGroup)
            }
        }

        // Create a new adapter with the filtered study groups
        filteredAdapter = object : ArrayAdapter<StudyGroup>(
            requireContext(),
            R.layout.box_item,
            filteredStudyGroups
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
                val groupLocationText = view.findViewById<TextView>(R.id.text_location)

                item?.let {
                    groupImage.setImageResource(it.imageResource)
                    groupNameText.text = it.groupName
                    leaderNameText.text = it.leaderName
                    currentNumText.text = it.currentNum.toString()
                    maxNumText.text = "(" + it.maxNum.toString() + ")"
                    groupDescriptionText.text = it.groupDescription
                    groupLocationText.text = it.groupLocation

                    // Update button text and background color based on joined state
                    if (it.joined) {
                        joinButton.text = "Cancel"
                        joinButton.setBackgroundColor(Color.GRAY)
                    } else {
                        joinButton.text = "Join"
                        joinButton.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primaryColor
                            )
                        )
                    }

                    joinButton.setOnClickListener {
                        val studyGroup = getItem(position)
                        studyGroup?.let {
                            if (it.joined) {
                                it.currentNum -= 1
                                it.joined = false
                                joinButton.text = "Join"
                                joinButton.setBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.primaryColor
                                    )
                                )
                            } else {
                                if (it.currentNum >= it.maxNum) { // full
                                    Toast.makeText(requireContext(), "Full", Toast.LENGTH_SHORT).show()
                                } else {
                                    it.currentNum += 1
                                    it.joined = true
                                    joinButton.text = "Cancel"
                                    joinButton.setBackgroundColor(Color.GRAY)
                                }
                            }
                            currentNumText.text = it.currentNum.toString()
//                            selectedStudyGroupsViewModel.selectedStudyGroups.value = filteredStudyGroups
                        }
                    }
                }

                return view
            }
        }

        // Set the new adapter to the homeListView
        homeListView.adapter = filteredAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
