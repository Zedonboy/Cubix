package com.redwasp.cubix.customViews

import android.content.Context
import android.os.Parcelable
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet

class CubixNestedView : NestedScrollView{
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, deffstyl : Int) : super(context, attributeSet, deffstyl)

    var state : Parcelable
    get() = onSaveInstanceState()
    set(value) = onRestoreInstanceState(value)
}