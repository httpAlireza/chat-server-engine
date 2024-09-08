package ir.jibit.data_access.db;

import ir.jibit.presentation.request.Request;
import ir.jibit.presentation.response.Response;

public interface Cacheable<T> {

    void fetchDataFromDatabase(Request request);

    void fetchDataFromCache(Request request);

    void addDataToCache(T key, Response value);
}
