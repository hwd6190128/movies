package com.example.movies.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> :
    Fragment() {

    @LayoutRes
    abstract fun getLayoutRes(): Int
    abstract fun getViewModelClass(): Class<VM>
    abstract fun getViewModelStoreOwner(): ViewModelStoreOwner

    open lateinit var binding: DB
    lateinit var viewModel: VM
    lateinit var navController: NavController

    open fun initObserve() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(getViewModelStoreOwner()).get(getViewModelClass())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)
        initObserve()
    }
}