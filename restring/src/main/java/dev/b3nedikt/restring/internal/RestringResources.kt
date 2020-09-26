@file:Suppress("DEPRECATION")

package dev.b3nedikt.restring.internal

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.*
import android.graphics.Movie
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.RequiresApi
import dev.b3nedikt.restring.StringRepository
import java.io.InputStream


/**
 * Wrapped [Resources] which will be provided by Restring.
 * Delegates all calls relevant for restring to the [ResourcesDelegate], every other call is
 * directed to the passed [baseResources].
 */
internal class RestringResources(
        private val baseResources: Resources,
        stringRepository: StringRepository,
        context: Context
) : Resources(baseResources.assets, baseResources.displayMetrics, baseResources.configuration) {

    private val resourcesDelegate by lazy {
        ResourcesDelegate(context, baseResources, stringRepository)
    }

    override fun getIdentifier(name: String, defType: String?, defPackage: String?): Int {
        return resourcesDelegate.getIdentifier(name, defType, defPackage)
    }

    override fun getString(id: Int): String {
        return resourcesDelegate.getString(id)
    }

    override fun getString(id: Int, vararg formatArgs: Any): String {
        return resourcesDelegate.getString(id, *formatArgs)
    }

    override fun getText(id: Int): CharSequence {
        return resourcesDelegate.getText(id)
    }

    override fun getText(id: Int, def: CharSequence): CharSequence {
        return resourcesDelegate.getText(id, def)
    }

    override fun getQuantityText(id: Int, quantity: Int): CharSequence {
        return resourcesDelegate.getQuantityText(id, quantity)
    }

    override fun getQuantityString(id: Int, quantity: Int): String {
        return resourcesDelegate.getQuantityString(id, quantity)
    }

    override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String {
        return resourcesDelegate.getQuantityString(id, quantity, *formatArgs)
    }

    override fun getStringArray(id: Int): Array<String> {
        return resourcesDelegate.getStringArray(id)
    }

    override fun getTextArray(id: Int): Array<CharSequence> {
        return resourcesDelegate.getTextArray(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getFont(id: Int): Typeface {
        return baseResources.getFont(id)
    }

    override fun getIntArray(id: Int): IntArray {
        return baseResources.getIntArray(id)
    }

    override fun obtainTypedArray(id: Int): TypedArray {
        return baseResources.obtainTypedArray(id)
    }

    override fun getDimension(id: Int): Float {
        return baseResources.getDimension(id)
    }

    override fun getDimensionPixelOffset(id: Int): Int {
        return baseResources.getDimensionPixelOffset(id)
    }

    override fun getDimensionPixelSize(id: Int): Int {
        return baseResources.getDimensionPixelSize(id)
    }

    override fun getFraction(id: Int, base: Int, pbase: Int): Float {
        return baseResources.getFraction(id, base, pbase)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun getDrawable(id: Int): Drawable {
        return baseResources.getDrawable(id)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getDrawable(id: Int, theme: Theme?): Drawable {
        return baseResources.getDrawable(id, theme)
    }

    override fun getDrawableForDensity(id: Int, density: Int): Drawable? {
        return baseResources.getDrawableForDensity(id, density)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getDrawableForDensity(id: Int, density: Int, theme: Theme?): Drawable? {
        return baseResources.getDrawableForDensity(id, density, theme)
    }

    override fun getMovie(id: Int): Movie {
        return baseResources.getMovie(id)
    }

    override fun getColor(id: Int): Int {
        return baseResources.getColor(id)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getColor(id: Int, theme: Theme?): Int {
        return baseResources.getColor(id, theme)
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun getColorStateList(id: Int): ColorStateList {
        return baseResources.getColorStateList(id)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getColorStateList(id: Int, theme: Theme?): ColorStateList {
        return baseResources.getColorStateList(id, theme)
    }

    override fun getBoolean(id: Int): Boolean {
        return baseResources.getBoolean(id)
    }

    override fun getInteger(id: Int): Int {
        return baseResources.getInteger(id)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun getFloat(id: Int): Float {
        return baseResources.getFloat(id)
    }

    override fun getLayout(id: Int): XmlResourceParser {
        return baseResources.getLayout(id)
    }

    override fun getAnimation(id: Int): XmlResourceParser {
        return baseResources.getAnimation(id)
    }

    override fun getXml(id: Int): XmlResourceParser {
        return baseResources.getXml(id)
    }

    override fun openRawResource(id: Int): InputStream {
        return baseResources.openRawResource(id)
    }

    override fun openRawResource(id: Int, value: TypedValue?): InputStream {
        return baseResources.openRawResource(id, value)
    }

    override fun openRawResourceFd(id: Int): AssetFileDescriptor {
        return baseResources.openRawResourceFd(id)
    }

    override fun getValue(id: Int, outValue: TypedValue?, resolveRefs: Boolean) {
        baseResources.getValue(id, outValue, resolveRefs)
    }

    override fun getValue(name: String?, outValue: TypedValue?, resolveRefs: Boolean) {
        baseResources.getValue(name, outValue, resolveRefs)
    }

    override fun getValueForDensity(id: Int, density: Int, outValue: TypedValue?, resolveRefs: Boolean) {
        baseResources.getValueForDensity(id, density, outValue, resolveRefs)
    }

    override fun obtainAttributes(set: AttributeSet?, attrs: IntArray?): TypedArray {
        return baseResources.obtainAttributes(set, attrs)
    }

    @Suppress("UNNECESSARY_SAFE_CALL")
    override fun updateConfiguration(config: Configuration?, metrics: DisplayMetrics?) {
        baseResources?.updateConfiguration(config, metrics)
    }

    override fun getDisplayMetrics(): DisplayMetrics {
        return baseResources.displayMetrics
    }

    override fun getConfiguration(): Configuration {
        return baseResources.configuration
    }

    override fun getResourceName(resid: Int): String {
        return baseResources.getResourceName(resid)
    }

    override fun getResourcePackageName(resid: Int): String {
        return baseResources.getResourcePackageName(resid)
    }

    override fun getResourceTypeName(resid: Int): String {
        return baseResources.getResourceTypeName(resid)
    }

    override fun getResourceEntryName(resid: Int): String {
        return baseResources.getResourceEntryName(resid)
    }

    override fun parseBundleExtras(parser: XmlResourceParser?, outBundle: Bundle?) {
        baseResources.parseBundleExtras(parser, outBundle)
    }

    override fun parseBundleExtra(tagName: String?, attrs: AttributeSet?, outBundle: Bundle?) {
        baseResources.parseBundleExtra(tagName, attrs, outBundle)
    }
}

