package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// 每個杯子蛋糕的價格
private const val PRICE_PER_CUPCAKE = 2.00
// 當天取貨費
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel: ViewModel() {
    /* 杯子蛋糕數量 */
    // 內部可變動(mutable)
    private val _quantity = MutableLiveData<Int>()
    // 外部不可變動(immutable)
    val quantity: LiveData<Int> = _quantity

    /* 杯子蛋糕口味 */
    // 內部可變動(mutable)
    private val _flavor = MutableLiveData<String>()
    // 外部不可變動(immutable)
    val flavor: LiveData<String> = _flavor

    /* 取貨日期 */
    // 內部可變動(mutable)
    private val _date = MutableLiveData<String>()
    // 外部不可變動(immutable)
    val date: LiveData<String> = _date

    /* 價格 */
    // 內部可變動(mutable)
    private val _price = MutableLiveData<Double>()
    // 外部不可變動(immutable)
    // 使用 Transformations.map() 初始化新變數，並傳入 _price 和 lambda 函式
    val price: LiveData<String> = Transformations.map(_price) {
        // 使用 getCurrencyInstance() 方法，將 price 轉換成當地幣別格式(local currency format)
        NumberFormat.getCurrencyInstance().format(it)
    }

    /* 新增 val 屬性的 dateOptions，將 getPickupOptions() 初始化 */
    val dateOptions = getPickupOptions()

    /* 新增 init 區塊，呼叫 resetOrder() */
    init {
        resetOrder()
    }

    /* 設定杯子蛋糕數量 */
    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    /* 設定杯子蛋糕口味 */
    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    /* 設定取貨日期 */
    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    /* 檢查是否已設定訂單的口味 */
    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    /* 回傳(return) pickup dates list */
    private fun getPickupOptions(): List<String> {
        // 建立 val 變數 options，初始化為 mutableListOf<String>()
        val options = mutableListOf<String>()

        // 使用 SimpleDateFormat 傳遞 pattern string "E MMM d" 和 locale 來建立 formatter string
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())

        // 取得 Calendar instance 並 assign 給新的 val 變數 calender
        val calendar = Calendar.getInstance()

        // 建置當天日期(current date)加上後續三個日期(following three dates)的 date list
        // 由於這需要 4 個 date options，請重複此程式碼區塊 4 次
        repeat(4) {
            // 格式化日期(format a date)
            options.add(formatter.format(calendar.time))
            // 將日曆(calendar)增加 1 天
            calendar.add(Calendar.DATE, 1)
        }
        // return 更新後的 options
        return options
    }

    /* 設定初始值 */
    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    /* 更新 price */
    private fun updatePrice() {
        // price = quantity * 每個杯子蛋糕的價錢
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE

        // 檢查使用者是否選取了當天取貨
        if (_date.value == dateOptions[0]) {
            // 將目前價格加上當天取貨費
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        // 設 _price 的值為 calculatedPrice
        _price.value = calculatedPrice
    }

}