package com.b3nedikt.restring

import android.util.AttributeSet
import android.view.View
import io.github.inflationx.viewpump.InflateResult
import io.github.inflationx.viewpump.Interceptor

object RestringInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): InflateResult =
            chain.proceed(chain.request()).let { inflateResult ->
                inflateResult.toBuilder()
                        .view(rewordView(inflateResult.view, inflateResult.attrs))
                        .build()
            }

    private fun rewordView(view: View?, attributeSet: AttributeSet?): View? = when {
        view != null && attributeSet != null -> view.reword(attributeSet)
        else -> view
    }
}

private fun View.reword(attrs: AttributeSet): View = Restring.viewTransformerManager.transform(this, attrs)