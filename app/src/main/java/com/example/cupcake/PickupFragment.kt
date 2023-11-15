/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentPickupBinding
import com.example.cupcake.model.OrderViewModel

/**
 * [PickupFragment] allows the user to choose a pickup date for the cupcake order.
 */
class PickupFragment : Fragment() {

    // Binding object instance corresponding to the fragment_pickup.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentPickupBinding? = null

    // 使用 activityViewModels() 取得共用的 view model -> OrderViewModel
    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentPickupBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            // 設定 viewModel 為共用 view model
            this.viewModel = sharedViewModel
            // 設定 binding 物件的 lifecycle owner
            lifecycleOwner = viewLifecycleOwner
            // 將 data variable flavorFragment bind 至 fragment instance
            // 使用 this 關鍵字來存取 fragment 中的 fragment instance(使用 @ 並明確指定 fragment class name)
            pickupFragment = this@PickupFragment
        }
    }

    /**
     * Navigate to the next screen to see the order summary.
     */
    fun goToNextScreen() {
        // 使用 findNavController() 方法取得 NavController，並呼叫此方法的 navigate()，
        // 然後傳入 action ID R.id.action_pickupFragment_to_summaryFragment，前往 summary fragment
        findNavController().navigate(R.id.action_pickupFragment_to_summaryFragment)
    }

    /* 取消訂單 */
    fun cancelOrder() {
        // 呼叫 sharedViewModel.resetOrder() 清除 view model
        sharedViewModel.resetOrder()
        // 從 pickup fragment 返回 start fragment
        findNavController().navigate(R.id.action_pickupFragment_to_startFragment)
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}