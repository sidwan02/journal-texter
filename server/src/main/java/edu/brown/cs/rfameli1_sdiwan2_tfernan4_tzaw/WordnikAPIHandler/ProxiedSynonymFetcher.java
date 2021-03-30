package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.WordnikAPIHandler;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ProxiedSynonymFetcher {
  private LoadingCache<String, Set<String>> synonymsCache;

  /** Constructor for ProxiedSynonymFetcher.
   * @param queryNeighborEdgesFunc is a function that queries adjacent PathEdges
   *                              starting from a certain node and returns them.
   */
  public ProxiedSynonymFetcher(Function<String, Set<String>>
                                queryNeighborEdgesFunc) {
    final int cacheSize = 100;
    synonymsCache = CacheBuilder.newBuilder()
        .maximumSize(cacheSize)
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build(new CacheLoader<>() {
          @Override
          public Set<String> load(String word) {
            WordnikAPIHandler wordnikConnection = new WordnikAPIHandler();
            return wordnikConnection.getSynonyms(word);
          }
        });
  }

  public Set<String> get(String word) {
    return synonymsCache.getUnchecked(word);
  }
}
