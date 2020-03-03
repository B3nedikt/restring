[ ![Download](https://api.bintray.com/packages/b3nedikt/restring/restring/images/download.svg?version=4.0.0) ](https://bintray.com/b3nedikt/restring/restring/4.0.0/link)
[![Build Status](https://travis-ci.org/B3nedikt/restring.svg?branch=master)](https://travis-ci.org/B3nedikt/restring)
[![codecov](https://codecov.io/gh/B3nedikt/restring/branch/master/graph/badge.svg)](https://codecov.io/gh/B3nedikt/restring)

## Restring 4.0.0
An easy way to replace bundled Strings dynamically, or provide new translations for Android.

### 1. Add dependencies
```groovy
// Replace bundled strings dynamically
implementation 'dev.b3nedikt.restring:restring:4.0.0'

// Intercept view inflation
implementation 'io.github.inflationx:viewpump:2.0.3'

// Allows to update the text of views at runtime without recreating the activity
implementation 'dev.b3nedikt.reword:reword:1.0.0'
```

### 2. Initialize
Initialize Restring in your Application class:
```java
Restring.init(this,
        new RestringConfig.Builder()
                .stringsLoader(new SampleStringsLoader())
                .loadAsync(false) // If string loader should load strings asynchronously, default true
                .build()
        );

ViewPump.init(ViewPump.builder()
        .addInterceptor(RewordInterceptor.INSTANCE)
        .build()
        );
```

### 3. Inject into Context
if you have a BaseActivity you can add this there, otherwise you have to add it to all of your activities!
```java
 @Override
 protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(Restring.wrapContext(newBase)));
}

@Override
public Resources getResources() {
    return getBaseContext().getResources();
}
```

### 4. Provide new Strings
There're two ways to provide new Strings. You can use either way or both.

First way: You can implement Restring.StringsLoader like this:
```java
public class SampleStringsLoader implements Restring.StringsLoader {

    @NotNull
    @Override
    public List<Locale> getLocales() {
        return Arrays.asList(Locale.ENGLISH, Locale.US);
    }

    @NotNull
    @Override
    public Map<String, CharSequence> getStrings(@NotNull Locale locale) {
        final Map<String, CharSequence> map = new HashMap<>();
        // Load strings here...
        return map;
    }
}
```
and initialize Restring like this:
```java
Restring.init(context,
              new RestringConfig.Builder()
                  .stringsLoader(new MyStringsLoader())
                  .build()
        );
```

Second way:
Load your Strings in any way / any time / any place and just call this:
```java
// e.g. locale=Locale.EN newStrings=map of (key-value)s
Restring.setStrings(locale, newStrings);
```

### 5. Done!
Now all strings in your app will be overriden by new strings provided to Restring.

## Change Language of the app
Restring works with the current locale by default, however you can change your apps language like this:
```java
Restring.setLocale(Locale.FRENCH);

// The layout containing the views you want to localize
final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
Restring.reword(rootView);
```
Restring will start using strings of the new locale.

## Apply updated resources without restarting the app
After providing new strings or changing the app language you can either restart the app,
or reload the UI like this:
```java
// The layout containing the views you want to localize
final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
Reword.reword(rootView);
```

## Custom Repository
By default, Restring will hold strings in memory for caching and persist them to shared preferences after loading.
You can however change the repository for saving the strings, to e.g. only keep them in memory like this:
```java
Restring.init(this,
        new RestringConfig.Builder()
                .stringsLoader(new SampleStringsLoader())
                .stringRepository(new MemoryStringRepository())
                .build()
);
```
If needed you can also provide custom repositories if you want to e.g. save the strings in a database
instead of the SharedPreferences, or if you don´t want to use the StringsLoader mechanism.

## Plurals & String arrays
Restring also supports quantity strings (plurals) and string arrays.
Just provide them in the strings loader like this or return an empty map if you don´t need plurals or string arrays.
In kotlin these two methods already have a default implementation returning an empty map.
```java
public class SampleStringsLoader implements Restring.StringsLoader {

    ...

    @NotNull
    @Override
    public Map<String, Map<PluralKeyword, CharSequence>> getQuantityStrings(@NotNull Locale locale) {
        // Load quantity strings (plurals) here
        return map;
    }

    @NotNull
    @Override
    public Map<String, CharSequence[]> getStringArrays(@NotNull Locale locale) {
        // Load string arrays here
        return map;
    }
}
```

## Notes
For displaying a string, Restring tries to find it in dynamic strings, and will use bundled version as fallback.
In other words, only the new provided strings will be overriden and for the rest the bundled version will be used.

## License
This is a fork of a library originally developed by Hamid Gharehdaghi.
Also takes some inspiration from Philology by JcMinarro.
<pre>
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
</pre>
