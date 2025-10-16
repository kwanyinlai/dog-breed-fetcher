package dogapi;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String breed = "hound";
        BreedFetcher breedFetcher = new CachingBreedFetcher(new BreedFetcherForLocalTesting());
        int result;
        try{
            result = getNumberOfSubBreeds(breed, breedFetcher);
        }
        catch (BreedFetcher.BreedNotFoundException e) {
            result = 0;
        }

        System.out.println(breed + " has " + result + " sub breeds");

        breed = "cat";
        try{
            result = getNumberOfSubBreeds(breed, breedFetcher);
        }
        catch (BreedFetcher.BreedNotFoundException e) {
            result = 0;
        }
        System.out.println(breed + " has " + result + " sub breeds");
    }

    /**
     * Return the number of sub breeds that the given dog breed has according to the
     * provided fetcher.
     * @param breed the name of the dog breed
     * @param breedFetcher the breedFetcher to use
     * @return the number of sub breeds. Zero should be returned if there are no sub breeds
     * returned by the fetcher
     */
    public static int getNumberOfSubBreeds(String breed, BreedFetcher breedFetcher) {

        List<String> subbreeds = breedFetcher.getSubBreeds(breed);
        if (subbreeds.isEmpty()) {
            return 0;
        }
        return subbreeds.size();


    }
}