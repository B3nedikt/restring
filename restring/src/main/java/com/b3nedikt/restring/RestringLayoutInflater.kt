package com.b3nedikt.restring

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.xmlpull.v1.XmlPullParser
import java.lang.reflect.Field

/**
 * Restring custom layout inflater. it puts hook on view creation, and tries to apply some transformations
 * to the newly created views.
 *
 *
 * Transformations can consist of transforming the texts applied on XML layout resources, so that it checks if
 * the string attribute set as a string resource it transforms the text and apply it to the view again.
 */
internal class RestringLayoutInflater(original: LayoutInflater,
                                      newContext: Context,
                                      private val viewTransformerManager: ViewTransformerManager,
                                      cloned: Boolean) : LayoutInflater(original, newContext) {

    private var privateFactorySet = false
    private var mConstructorArgs: Field? = null

    init {
        if (!cloned) {
            initFactories()
        }
    }

    override fun cloneInContext(newContext: Context): LayoutInflater {
        return RestringLayoutInflater(this, newContext, viewTransformerManager, true)
    }

    private fun initFactories() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (factory2 != null) {
                factory2 = factory2
            }
        }
        if (factory != null) {
            factory = factory
        }
    }

    override fun setFactory(factory: Factory?) {
        if (factory !is WrapperFactory) {
            super.setFactory(WrapperFactory(factory!!))
        } else {
            super.setFactory(factory)
        }
    }

    override fun setFactory2(factory2: Factory2?) {
        if (factory2 !is WrapperFactory2) {
            super.setFactory2(WrapperFactory2(factory2!!))
        } else {
            super.setFactory2(factory2)
        }
    }

    private fun setPrivateFactoryInternal() {
        if (privateFactorySet) return
        if (context !is Factory2) {
            privateFactorySet = true
            return
        }

        val setPrivateFactoryMethod = ReflectionUtils
                .getMethod(LayoutInflater::class.java, "setPrivateFactory")

        if (setPrivateFactoryMethod != null) {
            val newFactory = PrivateWrapperFactory2(context as Factory2)
            ReflectionUtils.invokeMethod(
                    this,
                    setPrivateFactoryMethod,
                    newFactory)
        }
        privateFactorySet = true
    }

    override fun inflate(parser: XmlPullParser?, root: ViewGroup?, attachToRoot: Boolean): View {
        setPrivateFactoryInternal()
        return super.inflate(parser, root, attachToRoot)
    }

    @Throws(ClassNotFoundException::class)
    override fun onCreateView(name: String?, attrs: AttributeSet?): View {
        for (prefix in sClassPrefixList) {
            try {
                val view = createView(name, prefix, attrs)
                if (view != null) {
                    return applyChange(view, attrs!!)!!
                }
            } catch (e: ClassNotFoundException) {
                // In this case we want to let the base class take a crack
                // at it.
            }

        }

        return super.onCreateView(name, attrs)
    }

    private fun applyChange(view: View, attrs: AttributeSet): View? {
        return viewTransformerManager.transform(view, attrs)
    }

    private inner class WrapperFactory internal constructor(private val factory: Factory) : Factory {

        override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View {
            val view = factory.onCreateView(name, context, attrs)
            return applyChange(view, attrs!!)!!
        }
    }

    private inner class WrapperFactory2 internal constructor(private val factory2: Factory2) : Factory2 {

        override fun onCreateView(parent: View?, name: String?, context: Context?, attrs: AttributeSet?): View? {
            val view = factory2.onCreateView(parent, name, context, attrs)
            if(view != null && attrs != null){
                return applyChange(view, attrs)
            }
            return null
        }

        override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View {
            val view = factory2.onCreateView(name, context, attrs)
            return applyChange(view, attrs!!)!!
        }
    }

    private fun createCustomViewInternal(view: View?, name: String, viewContext: Context, attrs: AttributeSet): View? {
        // I by no means advise anyone to do this normally, but Google have locked down access to
        // the createView() method, so we never get a callback with attributes at the end of the
        // createViewFromTag chain (which would solve all this unnecessary rubbish).
        // We at the very least try to optimise this as much as possible.
        // We only call for customViews (As they are the ones that never go through onCreateView(...)).
        // We also maintain the Field reference and make it accessible which will make a pretty
        // significant difference to performance on Android 4.0+.

        // If CustomViewCreation is off skip this.
        if (view == null && name.indexOf('.') > -1) {
            if (mConstructorArgs == null)
                mConstructorArgs = ReflectionUtils.getField(LayoutInflater::class.java, "mConstructorArgs")

            @Suppress("UNCHECKED_CAST")
            val mConstructorArgsArr = ReflectionUtils.getValue(mConstructorArgs!!, this) as Array<Any>?
            val lastContext = mConstructorArgsArr!![0]
            // The LayoutInflater actually finds out the correct context to use. We just need to set
            // it on the mConstructor for the internal method.
            // Set the constructor ars up for the createView, not sure why we can't pass these in.
            mConstructorArgsArr[0] = viewContext
            ReflectionUtils.setValue(mConstructorArgs!!, this, mConstructorArgsArr)
            try {
                return createView(name, null, attrs)
            } catch (ignored: ClassNotFoundException) {
            } finally {
                mConstructorArgsArr[0] = lastContext
                ReflectionUtils.setValue(mConstructorArgs!!, this, mConstructorArgsArr)
            }
        }
        return view
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private inner class PrivateWrapperFactory2(private val factory2: Factory2) : Factory2 {

        override fun onCreateView(parent: View?, name: String?, context: Context?, attrs: AttributeSet?): View? {
            var view: View? = factory2.onCreateView(parent, name, context, attrs)
            view = createCustomViewInternal(view, name!!, context!!, attrs!!)
            if(view != null){
                return applyChange(view, attrs)!!
            }
            return null
        }

        override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View {
            var view: View? = factory2.onCreateView(name, context, attrs)
            view = createCustomViewInternal(view, name!!, context!!, attrs!!)
            return applyChange(view!!, attrs)!!
        }
    }

    companion object {

        private val sClassPrefixList = arrayOf("android.widget.", "android.webkit.", "android.app.")
    }

}
