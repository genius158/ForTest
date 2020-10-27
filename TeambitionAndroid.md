### Teambition Android 笔试题
#### 1. 实现一个 LRU 容器，丢弃近期最少访问的元素
Kotlin
```kotlin
val cache = LRUCache(2); /* 缓存容量 */ 
cache.put(1, 1);
cache.put(2, 2);
cache.get(1);       // 返回  1
cache.put(3, 3);    // 该操作会使得密钥 2 作废
cache.get(2);       // 返回 -1 (未找到)
cache.put(4, 4);    // 该操作会使得密钥 1 作废
cache.get(1);       // 返回 -1 (未找到)
cache.get(3);       // 返回  3
cache.get(4);       // 返回  4
```
实现
```kotlin
interface ILruCache<K, V> {
    fun get(key: K): V?
    fun put(
      key: K,
      value: V
    )
  }

  internal data class Entity<K, V>(
    val key: K,
    val value: V
  )

  open class LruCache1<K, V>(private val size: Int) : ILruCache<K, V> {

    /**
     * LinkedHashMap 构造方法中参数 accessOrder，为true
     * 则每次get能拿到数据，那么这个数据会被从新放到数据结构的末尾
     */
    private val cacheList = LinkedHashMap<K, V?>(0, 0.75f, true)

    @Synchronized
    override fun get(key: K): V? {
      return cacheList.get(key)
    }

    @Synchronized
    override fun put(
      key: K,
      value: V
    ) {
      val entity = cacheList.get(key)
      entity?.let { return }
      if (cacheList.size + 1 > size) cacheList.remove(cacheList.keys.first())
      cacheList.put(key, value)
    }
  }

  open class LruCache2<K, V>(private val size: Int) : ILruCache<K, V> {
    // ArrayDeque(ps kotlin下的ArrayDeque，使用其removeAt，比java下的remove(obj),少一次遍历查询)
    // 比arrayList 移除头部和尾部，新增头部尾部，在没超出大小时，不需要移位
    //
    //
    // 此处可换成 LinkedList<Entity<K, V?>?>
    // 换成LinkedList 查找用first代替indexOfFirst，removeAt改用remove(obj)
    private val cacheList = kotlin.collections.ArrayDeque<Entity<K, V?>?>()

    override fun get(key: K): V? {
      val appearIndex = cacheList.indexOfFirst { kv -> kv?.key == key }
      if (appearIndex == -1) return null

      val appear = cacheList.removeAt(appearIndex)!!
      return appear.value
    }

    @Synchronized
    override fun put(
      key: K,
      value: V
    ) {
      // 此处不用LruCache1 中的get获取，这样会多一次遍历
      val appearIndex = cacheList.indexOfFirst { kv -> kv?.key == key }
      if (appearIndex == -1) {
        if (cacheList.size + 1 > size) {
          cacheList.removeFirst()
        } else {
          cacheList.removeAt(appearIndex)
        }
      }
      cacheList.addLast(Entity(key, value))
    }
  }

  /**
   * 题目要求int 那么继承[LruCache2]，简单改造get返回,[LruCache1]同理
   */
  class LruCacheInt(size: Int) : LruCache2<Int, Int>(size) {
    override fun get(key: Int): Int {
      super.get(key)
          ?.let { return it }
      return -1
    }
  }
```

#### 2. 硬币问题
假设有面值 1、2、5 的三种硬币，数量无限。现在输入一个正整数 n，问所需硬币数量最少需要多少？只需要硬币总数，不必枚举每种硬币的个数。
进一步的问题，如果硬币的个数与面额发生变化改如何解？比如 1、3、8


零钱兑换
```kotlin
val map: MutableMap<Int, Int> = HashMap()
  fun coinChange(
    coins: IntArray,
    amount: Int
  ): Int {
    if (map.containsKey(amount)) return map[amount]!!

    if (amount == 0) return 0
    if (amount < 0) return -1
    var res: Int? = null
    for (coinValue in coins) {
      //当前面值下，余钱的最小凑整硬币数
      val subRes = coinChange(coins, amount - coinValue)
      if (subRes == -1) {
        continue
      }
      //取最小值
      val curRes = 1 + subRes
      res = if (res == null) curRes else Math.min(res, curRes)
    }
    res = res ?: -1
    map[amount] = res
    return res
  }
```
#### 3. 极大数相乘
给出两个字符串，每个字符串包含字符范围限制在「0-9」，即每个字符串可被认为是一个正整数，且不限制字符串的长度。如何计算这两个字符串相乘的结果，结果需要同样以字符串的形式返回。
```kotlin
 /**
   * 做乘法运算
   */
  fun multiply(
    num1: String,
    num2: String
  ): String? {
    if (num1 == "0" || num2 == "0") {
      return "0"
    }
    val res = IntArray(num1.length + num2.length)
    for (i in num1.length - 1 downTo 0) {
      val n1 = num1[i] - '0'
      for (j in num2.length - 1 downTo 0) {
        val n2 = num2[j] - '0'
        val sum = res[i + j + 1] + n1 * n2
        //当前位置数值
        res[i + j + 1] = sum % 10
        //进位
        res[i + j] += sum / 10
      }
    }
    val result = StringBuilder()
    for (i in res.indices) {
      if (i == 0 && res[i] == 0) continue
      result.append(res[i])
    }
    return result.toString()
  }
```
#### 4. 代码重构
请尝试重构下面这段程序，给出重构后的代码。
```java
private List<Task> sortByTime(List<Task> list) {
    Collections.sort(list, (lhs, rhs) -> {
		if (lhs.getPriority() > rhs.getPriority()) {
			return -1;
		} else if (lhs.getPriority() < rhs.getPriority()) {
			return 1;
		} else if (lhs.getDueDate() != null && rhs.getDueDate() != null) {
			return lhs.getDueDate().compareTo(rhs.getDueDate());
		} else if (lhs.getDueDate() != null) {
			return -1;
		} else if (rhs.getDueDate() != null) {
			return 1;
		} else {
			return -1 * lhs.getCreated().compareTo(rhs.getCreated());
		}
	});
	return list;
}

```

```java
private List<Task> sortByTime(List<Task> list) {
    Collections.sort(list, (lhs, rhs) -> {
      // 内置list的子类都可空 所以lhs, rhs可能为空，这里不清楚规则，不作比较
      if (lhs == null || rhs == null) return 0;

      if (lhs.getPriority() > rhs.getPriority()) return -1;
      if (lhs.getPriority() < rhs.getPriority()) return 1;

      if (lhs.getDueDate() != null && rhs.getDueDate() != null) {
        return lhs.getDueDate().compareTo(rhs.getDueDate());
      }
      if (rhs.getDueDate() == null) return -1;
      if (lhs.getDueDate() == null) return 1;

      // 原lhs.getCreated().compareTo(rhs.getCreated())
      // 调用了compareTo，则getCreated 也是对象
      // 可能还是需要判空,也不清楚规则,仍不做比较
      if (lhs.getCreated() == null || rhs.getCreated() == null) return 0;
      return -1 * lhs.getCreated().compareTo(rhs.getCreated());
  });
    return list;
}
```
