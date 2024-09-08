package ir.jibit;

import ir.jibit.data_access.Resource;
import ir.jibit.presentation.Protocol;

public class DefaultConfigGetter implements ConfigGetter {
    public int getPort() {
        return 80;
    }

    public Protocol getProtocol() {
        return Protocol.HTTP;
    }

    public Resource getResource() {
        return Resource.POSTGRESQL;
    }

    @Override
    public int batchInsertTimeThreshold() {
        return 50;
    }


    @Override
    public int batchInsertSize() {
        return 200;
    }

    @Override
    public int batchFetchTimeThreshold() {
        return 100;
    }

    @Override
    public int batchFetchSize() {
        return 120;
    }

    @Override
    public int responseConsumerThreadNumber() {
        return 2;
    }

    @Override
    public int fetchMessagesHandlerThreadNumber() {
        return 15;
    }

    @Override
    public int insertMessageHandlerThreadNumber() {
        return 4;
    }

    @Override
    public int dbConnectionNumber() {
        return 15;
    }


}
