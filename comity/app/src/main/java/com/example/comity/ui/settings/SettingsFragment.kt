package com.example.comity.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.comity.LoginActivity
import com.example.comity.ProfileActivity
import com.example.comity.R
import com.example.comity.databinding.FragmentSettingsBinding
import com.firebase.ui.auth.AuthUI

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var settingsListView: ListView
    private lateinit var settingsAdapter: ArrayAdapter<SettingsItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        val textView: TextView = binding.textSettings
        settingsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        settingsListView = binding.settingsListView
        settingsAdapter = object : ArrayAdapter<SettingsItem>(
            requireContext(),
            R.layout.list_item_setting,
            getSettingsList()
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(
                    R.layout.list_item_setting,
                    parent,
                    false
                )

                val item = getItem(position)

                val iconSetting = view.findViewById<ImageView>(R.id.iconSetting)
                val textSetting = view.findViewById<TextView>(R.id.textSetting)

                item?.let {
                    iconSetting.setImageResource(it.iconResId)
                    textSetting.text = it.name
                }

                return view
            }
        }

        settingsListView.adapter = settingsAdapter

        settingsListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                onSettingsItemSelected(position)
            }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private data class SettingsItem(val name: String, val iconResId: Int)

    private fun getSettingsList(): List<SettingsItem> {
        return listOf(
            SettingsItem("Profile Settings", R.drawable.ic_settings_profile),
            SettingsItem("Notification Settings", R.drawable.ic_settings_notification),
            SettingsItem("Privacy Settings", R.drawable.ic_settings_privacy),
            SettingsItem("Language and Localization", R.drawable.ic_settings_language),
            SettingsItem("Theme and Display Settings", R.drawable.ic_settings_display),
            SettingsItem("Help and Support", R.drawable.ic_settings_help),
            SettingsItem("Logout", R.drawable.ic_settings_logout)
        )
    }

    private fun onSettingsItemSelected(position: Int) {
        when (position) {
            0 -> {
                val intent = Intent(requireContext(), ProfileActivity::class.java)
                startActivity(intent)
            }
            1 -> {
                Toast.makeText(requireContext(), "Notifications Settings Clicked", Toast.LENGTH_SHORT).show()
            }
            2 -> {
                Toast.makeText(requireContext(), "Privacy Settings Clicked", Toast.LENGTH_SHORT).show()
            }
            3 -> {
                Toast.makeText(requireContext(), "Language and Localization Clicked", Toast.LENGTH_SHORT).show()
            }
            4 -> {
                Toast.makeText(requireContext(), "Theme and Display Settings Clicked", Toast.LENGTH_SHORT).show()
            }
            5 -> {
                Toast.makeText(requireContext(), "Help and Support Clicked", Toast.LENGTH_SHORT).show()
            }
            6 ->{
                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener {
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
            }
        }
    }

}
