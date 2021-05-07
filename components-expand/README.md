### 扩展函数组件



#### Any

```kotlin
//TAG()
Log.d(TAG(), "onCreate: ")
//主线程判断
if(isMainThread()) TODO()
```

#### Boolean 

```kotlin
//三元运算
val result1 = isMainThread().then("a", "b")
val result2 = isMainThread().then({ TODO() }, { TODO() })
isMainThread().not().then { TODO() }
```

#### Bundle

```kotlin
//可以使用apply来替换下面是用
val bundle = Bundle().put(
    arrayOf(
        Pair("keyInt", 1),
        Pair("keyString", "abc"),
        Pair("keyBool", false)
    )
)
```

#### Context

```kotlin
val navigationBarHeight: Int = this.navigationBarHeight
val statusBarHeight: Int = this.statusBarHeight
val screenHeight: Int = this.screenHeight
val screenWidth: Int = this.screenWidth
val root = this.inflate(R.layout.activity_main)
/*** 使用Context初始化布局，专为RecyclerView提供*/
val view = this.inflateRv(R.layout.activity_main, parent)
```

####  Context -> SystemService

```kotlin
val activityManager: ActivityManager = this.activityManager
....涵盖所有常用
```

#### IO

```kotlin
FileInputStream(this.cacheDir).closeQuietly()
```

#### Number  <-> String

```kotlin
//精确到小数点后几位 默认2
val doExact = 21.6.doExact(2)
// 格式化成字符串去掉整数型的后面 .0 例如 21.0 = 21 多用于商品类展示
val noZero = 22.0.noZero()
// 金融类格式化  21,000,001,876.53
val formatNumber = 21000001876.528.formatNumber(addComma = true, modeFloor = false)
//转换int
"456.8".toIntWithNull(default = 0)
//转换float
"456.8".toFloatWithNull()
//转换double
"456.8".toDoubleWithNull()
"456.8".toBigDecimalWithNull()
```

#### dp/px/sp

```kotlin
20.dp
20.dp2px
20.px2dp
20f.sp
20f.sp2px
20f.px2sp
```

#### View

```kotlin
// 设置显示状态
root.show()
root.hide()
root.invisible()
// 获取显示状态
val visible = root.isVisible
val gone = root.isGone
val invisible = root.isInvisible
// 快照
val toBitmap = root.toBitmap()
// 扩大点击范围
btn.expandTouchArea(size = 40)
// 防止重复点击
btn.onFilterClick { 
    
}
//获取内部activity
val activity = btn.getActivity()
// 输入框输入监控  可以用于实时搜索功能
editView.textChange { 
    
}
// 输入法关闭
editView.hideKeyboard()
```

