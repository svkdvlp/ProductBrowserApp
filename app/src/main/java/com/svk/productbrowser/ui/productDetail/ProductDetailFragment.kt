package com.svk.productbrowser.ui.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.svk.productbrowser.databinding.FragmentProductDetailBinding
import com.svk.productbrowser.ui.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * ProductDetailFragment : Ui for showing product detail
 */
@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProductsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        _binding!!.lifecycleOwner = this
        _binding!!.viewModel = viewModel
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}