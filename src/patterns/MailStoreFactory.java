package patterns;


import oop.MailStore;
import store.FileMailStore;
import store.MemMailStore;
import store.RedisMailStore;

import java.awt.*;
import java.io.File;

public class MailStoreFactory {
    public static final int INMEMORYSTORE = 0;
    public static final int FILESTORE = 1;
    public static final int FILESTOREAES = 2;
    public static final int FILESTOREREVERSE = 3;
    public static final int REDISSTORE = 4;

    public static MailStore createMailStore(int storeType) {
        switch(storeType) {
            case INMEMORYSTORE:
                return new MemMailStore();
            case FILESTORE:
                return new FileMailStore();
            case FILESTOREAES:
                return new EncodeMailStoreDecorator(new FileMailStore(), new AESStrategy());
            case FILESTOREREVERSE:
                return new EncodeMailStoreDecorator(new FileMailStore(), new ReverseStrategy());
            case REDISSTORE:
                return RedisMailStore.getRedisInstance();
            default:
                return null;
        }
    }

    public static MailStore createMailStore(int storeType, String options) {
        switch(storeType) {
            case FILESTORE:
                return new FileMailStore(options);
            case FILESTOREAES:
                return new EncodeMailStoreDecorator(new FileMailStore(options), new AESStrategy());
            case FILESTOREREVERSE:
                return new EncodeMailStoreDecorator(new FileMailStore(options), new ReverseStrategy());
            default:
                return null;
        }
    }

}
