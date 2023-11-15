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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentSummaryBinding
import com.example.cupcake.model.OrderViewModel

/**
 * [SummaryFragment] contains a summary of the order details with a button to share the order
 * via another app.
 */
class SummaryFragment : Fragment() {

    // Binding object instance corresponding to the fragment_summary.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentSummaryBinding? = null

    // 使用 activityViewModels() 取得共用的 view model -> OrderViewModel
    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentSummaryBinding.inflate(inflater, container, false)
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
            summaryFragment = this@SummaryFragment
        }
    }

    /* 送出訂單 */
    // 透過 implicit intent 將 order details 分享給另一個 app 來提交訂單。
    fun sendOrder() {
        // 從 view model 抓杯子蛋糕數量 (若 quantity 為 null，就設值為 0)
        val numberOfCupcakes = sharedViewModel.quantity.value ?: 0
        // email 收件人
        val TO = arrayOf(getString(R.string.cupcake_email))
        /*
        getQuantityString(R.plurals.cupcakes, 1, 1) 會傳回 1 cupcake 字串
        getQuantityString(R.plurals.cupcakes, 6, 6) 會傳回 6 cupcakes 字串
        getQuantityString(R.plurals.cupcakes, 0, 0) 會傳回 0 cupcakes 字串
         */

        // 新增 order summary 文字
        // 從 shared view model 取得訂單數量、口味、日期和價格，建立格式化的 order_details 字串
        val orderSummary = getString(
            R.string.order_details,
            resources.getQuantityString(R.plurals.cupcakes, numberOfCupcakes, numberOfCupcakes),
            sharedViewModel.flavor.value.toString(),
            sharedViewModel.date.value.toString(),
            sharedViewModel.price.value.toString()
        )

        // 將訂單分享至其他 app 的 implicit intent(email intent)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, TO)
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_cupcake_order))
            putExtra(Intent.EXTRA_TEXT, orderSummary)
        }

        // 檢查是否有 app 能處理此 intent 後，使用這項 intent 啟動(launching) activity
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            startActivity(intent)
        }
    }

    /* 取消訂單 */
    fun cancelOrder() {
        // 呼叫 sharedViewModel.resetOrder() 清除 view model
        sharedViewModel.resetOrder()
        // 從 summary fragment 返回 start fragment
        findNavController().navigate(R.id.action_summaryFragment_to_startFragment)
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