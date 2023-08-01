package com.svk.productbrowser.ui.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.svk.productbrowser.databinding.FragmentProductDetailBinding
import com.svk.productbrowser.ui.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private var paramId: Int? = null
    private val args : ProductDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<ProductsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paramId = args.paramId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        _binding!!.lifecycleOwner = this
        _binding!!.viewModel = viewModel
        fetchProductDetail()
        return view
    }

    private fun fetchProductDetail() {
        viewModel.viewModelScope.launch {
            viewModel.getProductDetail(paramId!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}