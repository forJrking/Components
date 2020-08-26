### 组件-工具箱



#### ActivityManger

全局监听activity的生命周期，可以在任意位置直接获取activity或者杀死activity,支持class对象查找。另外提供APP前后台监听，不支持多进程

```kotlin
//初始化
ActivityManager.instances.initialize(this)
//栈顶、底 查看
ActivityManager.instances.topActivity()
ActivityManager.instances.bottomActivity()
//结束    
ActivityManager.instances.finishActivity(Activity::class.java)
ActivityManager.instances.finishActivity(Activity.this)
ActivityManager.instances.finishAllActivity()
ActivityManager.instances.appExit()
//查找指定
ActivityManager.instances.hasActivity(Activity::class.java)
            ...
```

#### Cxt

全局Application托管，任意位置获取Application、Resources、string等等

```kotlin
//初始化 也可以不用内部使用反射自动获取
Cxt.initialize(this.application)
//各种context相关操作
val app: Application = Cxt.application
val debug: Boolean = Cxt.debugEnable
val res: Resources = Cxt.res
val str: String = Cxt.getStr(R.string.app_name)
val color: Int = Cxt.getColor(R.color.white)
val cxt: Context = Cxt.get()
```

#### GoHandler

全局主线程调度器,支持Handler所有主动api

```kotlin
GoHandler.getInstance().runOnMainThread{
    //do something
}
```

#### EmptyUtils

object判空工具

```kotlin
EmptyUtils.isEmpty(object)
EmptyUtils.isNotEmpty(object)
```

#### DelayInitDispatcher

AppStart第三方SDK等初始化调度，用于加速冷启动

```kotlin
//初始化顺序调度器
DelayInitDispatcher()
    .addTask(object : DelayInitDispatcher.Task(doMain, "initSDK") {
        override fun run() {
        	//TODO
        }
    })
    .addTask(xxx)
    .start()
```

