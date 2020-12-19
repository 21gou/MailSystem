package oop;


import store.FileMailStore;
import store.MemMailStore;
import store.RedisMailStore;

public class MailStoreFactory {
    public static final int INMEMORYSTORE = 0;
    public static final int FILESTORE = 1;
    public static final int REDISSTORE = 2;

    public static MailStore getMailStoreFactory(int storeType) {
        switch(storeType) {
            case INMEMORYSTORE:
                return new MemMailStore();
            case FILESTORE:
                return new FileMailStore();
            case REDISSTORE:
                return new RedisMailStore();
            default:
                return null;
        }
    }

}
