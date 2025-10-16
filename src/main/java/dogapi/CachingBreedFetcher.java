package dogapi;

import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

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
    private HashMap<String, ArrayList<String>> cachedSubbreeds = new HashMap<>();
    public CachingBreedFetcher(BreedFetcher fetcher) {

    }

    @Override
    public List<String> getSubBreeds(String breed) {
        if (cachedSubbreeds.containsKey(breed)) {
            return cachedSubbreeds.get(breed);
        }
        ArrayList<String> subbreedList = new ArrayList<>();
        final Request request = new Request.Builder()
                .url("https://dog.ceo/api/breed/"+breed+"/list")
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE) == SUCCESS_CODE) {
                final JSONObject subbreeds = responseBody.getJSONObject("message");
                for (Object breedName : subbreeds.keySet()) {
                    subbreedList.add(breedName.toString());
                }
                cachedSubbreeds.put(breed, subbreedList);
                callsMade++;
            }
            else {
                throw new BreedFetcher.BreedNotFoundException("Subbreeds could not be found for " + breed);
            }
        }
        catch (BreedFetcher.BreedNotFoundException event) {
            throw new BreedFetcher.BreedNotFoundException(event);
        }
        return subbreedList;
    }

    public int getCallsMade() {
        return callsMade;
    }
}