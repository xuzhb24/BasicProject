package com.android.widget.AlignTextView;

import java.lang.reflect.Array;

/**
 * Created by xuzhb on 2021/4/13
 */
public class ArrayUtil {

    private static final int CACHE_SIZE = 73;
    private static Object[] mCache = new Object[CACHE_SIZE];

    private ArrayUtil() {
    }

    /**
     * Returns an empty array of the specified type.  The intent is that
     * it will return the same empty array every time to avoid reallocation,
     * although this is not guaranteed.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] emptyArray(Class<T> kind) {
        if (kind == Object.class) {
            return (T[]) new Object[0];
        }
        int bucket = (kind.hashCode() & 0x7FFFFFFF) % CACHE_SIZE;
        Object cache = mCache[bucket];
        if (cache == null || cache.getClass().getComponentType() != kind) {
            cache = Array.newInstance(kind, 0);
            mCache[bucket] = cache;
        }
        return (T[]) cache;
    }

}
