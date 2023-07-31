package com.svk.productbrowser.ui.productList

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.svk.productbrowser.databinding.FragmentProductsListBinding
import com.svk.productbrowser.domain.ProductListState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductsListFragment : Fragment() {
    private var _binding: FragmentProductsListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProductsViewModel>()
    @set:Inject
    var adapterProducts : ProductsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsListBinding.inflate(inflater, container, false)
        val view = binding.root
        val rvProducts = binding.rvProducts
        rvProducts.layoutManager = LinearLayoutManager(context)
        rvProducts.adapter = adapterProducts
        adapterProducts?.onItemClick = { product ->
            val action = ProductsListFragmentDirections
                .actionProductsListFragmentToProductDetailFragment()
            action.paramId = product.id
            findNavController().navigate(action)
        }
        setupObservers()
        searchProducts()
        return view
    }

    private fun setupObservers() {
        lifecycleScope.launch {
           viewModel.isSearching.collect{ isSearching->
               if(isSearching){
                   hideMessageUi()
                   binding.pbLoading.visibility = View.VISIBLE
               }else{
                   binding.pbLoading.visibility = View.GONE
               }
           }
       }

        lifecycleScope.launch {
            viewModel.productModels.collect{ result ->
                when(result){
                    is ProductListState.Loading ->{
                        adapterProducts?.clear()
                        showMessageUi("Searching..")
                    }
                    is ProductListState.Success ->{
                        hideMessageUi()
                        adapterProducts?.clearAndAddAll(result.products)
                    }
                    is ProductListState.Error ->{
                        adapterProducts?.clear()
                        showMessageUi(result.error)
                    }
                }
            }
        }
    }

    private fun showMessageUi(msg: String = "Loading") {
        binding.tvMsg.text = msg
        binding.tvMsg.visibility = View.VISIBLE
    }

    private fun hideMessageUi() {
        binding.tvMsg.visibility = View.GONE
    }

    private fun searchProducts() {
        showMessageUi("Browse Products")
        binding.edtSearch.doAfterTextChanged {editable: Editable? ->
            viewModel.onTextChange(editable.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}