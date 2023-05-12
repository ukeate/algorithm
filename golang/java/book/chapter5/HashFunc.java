package chapter5;

/**
 * Created by outrun on 5/16/16.
 */
public class HashFunc {

    public static int hash(String key, int tableSize) {

        int hashVal = 0;

        for (int i = 0; i < key.length(); i++) {
            hashVal = 37 * hashVal + key.charAt(i);
        }

        hashVal %= tableSize;
        if (hashVal < 0) {
            hashVal += tableSize;
        }

        return hashVal;
    }
}
