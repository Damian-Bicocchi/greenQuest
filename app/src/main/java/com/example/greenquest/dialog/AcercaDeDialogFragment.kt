package com.example.greenquest.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.greenquest.databinding.DialogoAcercaDeBinding

class AcercaDeDialogFragment  : DialogFragment(){

    lateinit var binding : DialogoAcercaDeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogoAcercaDeBinding.inflate(inflater)

        binding.volver.setOnClickListener {
            dialog?.dismiss()
        }
        return  binding.root
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}