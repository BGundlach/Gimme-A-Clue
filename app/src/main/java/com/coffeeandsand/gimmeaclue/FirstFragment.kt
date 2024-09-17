package com.coffeeandsand.gimmeaclue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.coffeeandsand.gimmeaclue.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private lateinit var act: MainActivity

    private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        act = activity as MainActivity
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            binding.textviewFirst.text = act.mainVM.orientation.value.toString()
        }
    }

    override fun onResume() {
        super.onResume()
        act.mainVM.startSensors()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        act.mainVM.stopSensors()
        _binding = null
    }
}