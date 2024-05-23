package com.teamdontbe.feature.homedetail.imagedetail

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import coil.load
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentImageDetailBinding
import com.teamdontbe.feature.util.KeyStorage

class ImageDialogFragment :
    BindingDialogFragment<FragmentImageDetailBinding>(R.layout.fragment_image_detail) {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initView() {
        initLoadImageUri()
        initCancelButtonClick()
    }

    private fun initLoadImageUri() {
        val imageUri = arguments?.getString(KeyStorage.KEY_NOTI_DATA).orEmpty()
        if (imageUri.isNotEmpty()) {
            binding.ivImageDetail.load(imageUri)
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 25.0f)
    }

    private fun initCancelButtonClick() {
        binding.ivPostingCancelImage.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}
