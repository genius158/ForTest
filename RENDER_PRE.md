# 界面绘制FAQ

## 单刀直入
布局
|                                |爷爷grandpa(FrameLayout)|                                 |
|-------------------------------:|:----------------------:|:--------------------------------|
| 父亲father(FrameLayout)         |                        |叔叔uncle(FragmentLayout)设置了背景| 
| 儿子child(TextView)             |                        |                                |   
|                                 |                       |                                | 

child:      
宽match_parent child 调用requestLayout
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child（儿子）  |         |         |         |              |         |
| parent（父亲） |         |         |         |              |         |
| grandpa（爷爷）|         |         |         |              |         |
| uncle（叔叔）  |         |         |         |              |         |

宽match_parent child 调用invalidate
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child（儿子）  |         |         |         |              |         |
| parent（父亲） |         |         |         |              |         |
| grandpa（爷爷）|         |         |         |              |         |
| uncle（叔叔）  |         |         |         |              |         |

宽wrap_content child 调用requestLayout
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child（儿子）  |         |         |         |              |         |
| parent（父亲） |         |         |         |              |         |
| grandpa（爷爷）|         |         |         |              |         |
| uncle（叔叔）  |         |         |         |              |         |

宽wrap_content child 调用invalidate
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child（儿子） |          |         |         |              |         |
| parent（父亲） |         |         |         |              |         |
| grandpa（爷爷）|         |         |         |              |         |
| uncle（叔叔）  |         |         |         |              |         |


不可见的child 调用requestLayout
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child（儿子）  |         |         |         |              |         |
| parent（父亲） |         |         |         |              |         |
| grandpa（爷爷）|         |         |         |              |         |
| uncle（叔叔）  |         |         |         |              |         |

不可见的child 调用invalidate
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child（儿子）  |         |         |         |              |         |
| parent（父亲） |         |         |         |              |         |
| grandpa（爷爷）|         |         |         |              |         |
| uncle（叔叔）  |         |         |         |              |         |

parent（父亲）:      
宽match_parent parent 调用requestLayout
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child(儿子)   |          |         |         |              |         |
| parent（父亲） |         |         |         |              |         |
| grandpa（爷爷）|         |         |         |              |         |
| uncle（叔叔）  |         |         |         |              |         |

宽match_parent parent 调用invalidate
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child(儿子)    |         |         |         |              |         |
| parent（父亲） |         |         |         |              |         |
| grandpa（爷爷）|         |         |         |              |         |
| uncle（叔叔）  |         |         |         |              |         |

宽wrap_content parent 调用requestLayout
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child(儿子)    |         |         |         |              |         |
| parent（父亲） |         |         |         |              |         |
| grandpa（爷爷）|         |         |         |              |         |
| uncle（叔叔）  |         |         |         |              |         |

宽wrap_content parent 调用invalidate
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child(儿子)    |         |         |         |              |         |
| parent（父亲） |         |         |         |              |         |
| grandpa（爷爷）|         |         |         |              |         |
| uncle（叔叔）  |         |         |         |              |         |

不可见的parent 调用requestLayout (当前情况下，child调用呢?)
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child(儿子)    |         |         |         |              |         |
| parent（父亲） |         |         |         |              |         |
| grandpa（爷爷）|         |         |         |              |         |
| uncle（叔叔）  |         |         |         |              |         |

不可见的parent 调用invalidate
|               |  onMeasure |  onLayout  |   draw  | dispatchDraw | onDraw  |
|:--------------|:--------|:--------|:--------|:-------------|:--------|
| child(儿子)    |         |         |         |              |         |
| parent（父亲） |         |         |         |              |         |
| grandpa（爷爷）|         |         |         |              |         |
| uncle（叔叔）  |         |         |         |              |         |

如果是match_parent textView setText（）     
如果是wrap_content textView setText（）     
如果是match_parent textView setText（）出现换行的情况     
只调用child的api 什么情况下uncle会触发onMeasure、 onLayout    

番外:怎么监听setVisibility(GONE)     

