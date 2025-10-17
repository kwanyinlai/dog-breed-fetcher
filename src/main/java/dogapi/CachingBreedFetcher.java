package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private final OkHttpClient client = new OkHttpClient();
    private HashMap<String, ArrayList<String>> cachedSubbreeds = new HashMap<>();
    private final BreedFetcher breedFetcher;
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.breedFetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {

        if (cachedSubbreeds.containsKey(breed)) {
            return cachedSubbreeds.get(breed);
        }
        callsMade++;

        return breedFetcher.getSubBreeds(breed);
    }

    public int getCallsMade() {
        return callsMade;
    }
}