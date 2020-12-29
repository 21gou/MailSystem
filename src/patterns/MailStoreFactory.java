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
    public static final int FILESTOREAESREVERSE = 4;
    public static final int REDISSTORE = 5;

    /**
     * Returns new mail store based on the parameter
     * @param storeType
     * @return
     */
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
            case FILESTOREAESREVERSE:
                // Chain AES with reverse strategy in top of MailStore
                return new EncodeMailStoreDecorator(
                        new EncodeMailStoreDecorator(new FileMailStore(), new AESStrategy()),
                        new ReverseStrategy()
                );

            case REDISSTORE:
                return RedisMailStore.getRedisInstance();
            default:
                return null;
        }
    }

    /**
     * Returns new mail store based on the parameters
     * @param storeType
     * @param options
     * @return
     */
    public static MailStore createMailStore(int storeType, String options) {
        switch(storeType) {
            case FILESTORE:
                return new FileMailStore(options);
            case FILESTOREAES:
                return new EncodeMailStoreDecorator(new FileMailStore(options), new AESStrategy());
            case FILESTOREREVERSE:
                return new EncodeMailStoreDecorator(new FileMailStore(options), new ReverseStrategy());
            case FILESTOREAESREVERSE:
                // Chain AES with reverse strategy in top of MailStore
                return new EncodeMailStoreDecorator(
                        new EncodeMailStoreDecorator(new FileMailStore(options), new AESStrategy()),
                        new ReverseStrategy()
                );
            default:
                return null;
        }
    }

}
