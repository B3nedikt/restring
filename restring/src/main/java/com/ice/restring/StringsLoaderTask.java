package com.ice.restring;

import android.os.AsyncTask;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Try to load all strings for different languages by a StringsLoader.
 * All string loads happen on background thread, and saving into repository happens on main thread.
 * <p>
 * FIRST it retrieves all supported languages,
 * THEN it retrieves all strings(key, value) for each language.
 */
class StringsLoaderTask extends AsyncTask<Void, Void, Map<String, Map<String, String>>> {

    private Restring.StringsLoader stringsLoader;
    private StringRepository stringRepository;

    StringsLoaderTask(Restring.StringsLoader stringsLoader,
                      StringRepository stringRepository) {
        this.stringsLoader = stringsLoader;
        this.stringRepository = stringRepository;
    }

    public void run() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected Map<String, Map<String, String>> doInBackground(Void... voids) {
        Map<String, Map<String, String>> langStrings = new LinkedHashMap<>();

        List<String> languages = stringsLoader.getLanguages();
        for (String lang : languages) {
            Map<String, String> keyValues = stringsLoader.getStrings(lang);
            if (keyValues != null && keyValues.size() > 0) {
                langStrings.put(lang, keyValues);
            }
        }

        return langStrings;
    }

    @Override
    protected void onPostExecute(Map<String, Map<String, String>> langStrings) {
        for (Map.Entry<String, Map<String, String>> langItem : langStrings.entrySet()) {
            stringRepository.setStrings(langItem.getKey(), langItem.getValue());
        }
    }
}