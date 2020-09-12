[![Download](https://api.bintray.com/packages/b3nedikt/restring/restring/images/download.svg?version=5.0.0)](https://bintray.com/b3nedikt/restring/restring/5.0.0/link)
[![Build Status](https://travis-ci.org/B3nedikt/restring.svg?branch=master)](https://travis-ci.org/B3nedikt/restring)
[![codecov](https://codecov.io/gh/B3nedikt/restring/branch/master/graph/badge.svg)](https://codecov.io/gh/B3nedikt/restring)
[![Documentation](https://img.shields.io/badge/docs-documentation-green.svg)](https://b3nedikt.github.io/restring/)

## Restring 5.0.0

An easy way to replace bundled Strings dynamically, or provide new translations for Android. Also supports plurals & string arrays.

### 1. Add dependencies

```groovy
// Replace bundled strings dynamically
implementation 'dev.b3nedikt.restring:restring:5.0.0'

// Intercept view inflation
implementation 'dev.b3nedikt.viewpump:viewpump:3.0.0'

// Allows to update the text of views at runtime without recreating the activity
implementation 'dev.b3nedikt.reword:reword:2.0.0'
```

### 2. Initialize

Initialize Restring in your Application class:

```kotlin
Restring.init(this)

ViewPump.init(ViewPump.builder()
        .addInterceptor(RewordInterceptor)
        .build()
```

### 3. Inject into Context

if you have a BaseActivity you can add this there, otherwise you have to add it to all of your activities!

```kotlin
abstract class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(Restring.wrapContext(newBase)))
    }

    override fun getResources(): Resources {
        return Restring.wrapContext(baseContext).resources
    }
}
```

If you use fragments add the following to your BaseFragment:

```kotlin
abstract class BaseFragment : Fragment() {

    override fun onResume() {
        super.onResume()

        ViewPump.setOverwriteContext(Restring.wrapContext(requireContext()))
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        val wrappedContext = ViewPumpContextWrapper.wrap(Restring.wrapContext(requireContext()))
        return wrappedContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}
```

Also should you use the application context somewhere to retrieve strings
and inject it with a DI tool like koin or dagger, I would recommend wrapping it in your
application class with Restring.wrap(...) when providing it to your DI tool.

### 4. Provide new Strings

Now load your strings like this:

```kotlin
Restring.putStrings(Locale.FRENCH, frenchStringsMap)
```

Now all strings in your app will be overriden by new strings provided to Restring.

## Change Language of the app

Restring works with the current locale by default, however you can change your apps language like this:

```kotlin
Restring.locale = Locale.FRENCH
```

Restring will start using strings of the new locale.

## Apply updated resources without restarting the app

After providing new strings or changing the app language you can either restart the app,
or reload the UI like this:

```kotlin
// The layout containing the views you want to localize
val rootView = window.decorView.findViewById<ContentFrameLayout>(android.R.id.content)
Reword.reword(rootView)
```

If you have changed the texts of views in code, you need to update these
texts manually of course, as this call will only update those string resources which
you set in your xml layout files.

## Custom Repository

By default, Restring will hold strings in memory for caching and persist them to shared preferences after every write operation. You can however change the repository behavior by providing a custom implementation of the StringRepository interface to Restring like this:

```java
Restring.stringRepository = newRepository
```

Keep in mind that this repository is immutable, so you need to either:

- Implement the MutableStringRepository, which will allow you to use the normal mutation methods of the Restring facade like Restring.putStrings(...).

- Provide your own mutation methods in your derived class.

## App Locale integration

This library was build for easy integration with my library [AppLocale](https://github.com/B3nedikt/AppLocale). AppLocale simplifies managing the Locales supported by your app.

To integrate it you need to first create a custom LocaleProvider:

```kotlin
object AppLocaleLocaleProvider : LocaleProvider {

    override val isInitial
        get() = AppLocale.isInitial

    override var currentLocale
        get() = AppLocale.desiredLocale
        set(value) {
            AppLocale.desiredLocale = value
        }
}
```

Then you can install this custom LocaleProvider when initializing Restring in your application class:

```kotlin
Restring.init(this)
Restring.localeProvider = AppLocaleLocaleProvider
```

If you already have some mechanism in your app to manage its Locales, you can easily integrate it with a custom LocaleProvider as well.

## Notes

For displaying a string, Restring tries to find it in dynamic strings, and will use bundled version as fallback.
In other words, only the new provided strings will be overriden and for the rest the bundled version will be used.
If you want you can also provide Restring with string resources which are not in your apps string.xmls. Restring
will generated an id for these at runtime.

## License

This is a fork of a library originally developed by Hamid Gharehdaghi.
Also takes some inspiration from Philology by JcMinarro.

```
Copyright 2018-present Restring Contributors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
