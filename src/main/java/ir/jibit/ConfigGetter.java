package ir.jibit;

import ir.jibit.data_access.Resource;
import ir.jibit.presentation.Protocol;

/**
 * It gets config attributes from any sources.
 *
 * @author mrahimian
 */
public interface ConfigGetter {

    int getPort();

    Protocol getProtocol();

    Resource getResource();

    /**
     * @return time in millisecond
     */
    int batchInsertTimeThreshold();


    int batchInsertSize();

    /**
     * @return time in millisecond
     */
    int batchFetchTimeThreshold();

    int batchFetchSize();

    int responseConsumerThreadNumber();

    int fetchMessagesHandlerThreadNumber();

    int insertMessageHandlerThreadNumber();

    int dbConnectionNumber();

}
