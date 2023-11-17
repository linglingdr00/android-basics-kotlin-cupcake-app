package com.example.cupcake

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.cupcake.model.OrderViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ViewModelTests {
    /* 因 LiveData 物件不得呼叫主執行緒(main thread)，須建立測試規則(test rule) */
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun quantity_twelve_cupcakes() {

        // 在 method 中，建立 OrderViewModel instance
        val viewModel = OrderViewModel()

        // 確認 OrderViewModel 中的 quantity 物件在 setQuantity 呼叫時已更新
        // 測試 LiveData 物件的 value 時，須先使用 observeForever method 觀察(observed)物件，才能發出變更
        viewModel.quantity.observeForever {  }

        // 呼叫 setQuantity() method，將 12 傳入做為參數
        viewModel.setQuantity(12)

        // 指定以下斷言(assertion)，比較預期結果和實際結果
        assertEquals(12, viewModel.quantity.value)
    }

    @Test
    fun price_twelve_cupcakes() {
        // 建立 OrderViewModel instance
        var viewModel = OrderViewModel()

        // 確認 OrderViewModel 中的 price 物件在 setQuantity 呼叫時已更新
        // 測試 LiveData 物件的 value 時，須先使用 observeForever method 觀察(observed)物件，才能發出變更
        viewModel.price.observeForever {  }

        // 呼叫 setQuantity() 方法，將 12 傳入做為參數。
        viewModel.setQuantity(12)

        /* 查看 OrderViewModel 中的 PRICE_PER_CUPCAKE 時，可看見每個杯子蛋糕的售價為 $2.00 美元。
        還可以看到每次 ViewModel 初始化時都會呼叫 resetOrder()，在此方法中，預設日期為今天的日期，
        PRICE_FOR_SAME_DAY_PICKUP 為 $3.00 美元。因此，12 * 2 + 3 = 27。選擇 12 個杯子蛋糕後，
        我們預期 price 變數的 value 應為 $27.00 美元。*/

        // 假設 $27.00 美元的預期 value 等於 price LiveData 物件的 value。
        assertEquals("$27.00",viewModel.price.value)
    }
}