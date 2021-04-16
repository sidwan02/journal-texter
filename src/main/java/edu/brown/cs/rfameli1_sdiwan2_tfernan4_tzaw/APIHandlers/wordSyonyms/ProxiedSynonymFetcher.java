package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers.wordSyonyms;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Class that caches synonyms and retrieves synonyms within cache.
 */
public class ProxiedSynonymFetcher {
  public static final ProxiedSynonymFetcher INSTANCE = new ProxiedSynonymFetcher();
  private LoadingCache<String, Set<String>> synonymsCache;

  public ProxiedSynonymFetcher() {
    final int cacheSize = 1000;
    synonymsCache = CacheBuilder.newBuilder()
        .maximumSize(cacheSize)
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build(new CacheLoader<>() {
          @Override
          public Set<String> load(String word) {
            // if the word is not in cache connect to the Wordnik API
            // and get synonyms
            WordnikAPIHandler wordnikConnection = new WordnikAPIHandler();
            return wordnikConnection.getSynonyms(word);
          }
        });
  }

  /**
   * Gets the synonyms of the word from the Wordnik API if not already in cache.
   * @param word a word whose synonym must be determined.
   * @return a Set of all synonyms of that word.
   */
  public Set<String> get(String word) {
    return synonymsCache.getUnchecked(word);
  }
}
