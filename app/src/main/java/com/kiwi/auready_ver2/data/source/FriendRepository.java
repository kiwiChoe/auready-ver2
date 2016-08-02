package com.kiwi.auready_ver2.data.source;

import android.support.annotation.NonNull;

import com.kiwi.auready_ver2.data.Friend;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by kiwi on 7/1/16.
 */
public class FriendRepository implements FriendDataSource {

//    private static List<Friend> FRIENDS = Lists.newArrayList(new Friend("email1", "name1"), new Friend("email2", "name2"));

    private static FriendRepository INSTANCE = null;

    private final FriendDataSource mFriendRemoteDataSource;
    private final FriendDataSource mFriendsLocalDataSource;

    private FriendRepository(@NonNull FriendDataSource friendRemoteDataSource,
                             @NonNull FriendDataSource friendLocalDataSource) {
        mFriendRemoteDataSource = checkNotNull(friendRemoteDataSource);
        mFriendsLocalDataSource = checkNotNull(friendLocalDataSource);
    }

    public static FriendRepository getInstance(FriendDataSource friendRemoteDataSource,
                                               FriendDataSource friendLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new FriendRepository(friendRemoteDataSource, friendLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    /*
    * Gets friend from local data source(sqlite) unless the table is new or empty.
    * In that case it uses the network data source. This is done to simplify the sample.
    * Note: {@link LoadFriendsCallback()#onDataNotAvailable()} is fired if both data sources fail to get the data.
    * */
    @Override
    public void getFriend(@NonNull String friendColumnId, @NonNull final GetFriendCallback callback) {
        checkNotNull(friendColumnId);
        checkNotNull(callback);

        // Load from server/persisted if needed.

        // Is the friend in the local data source? If not, query the network.
        mFriendsLocalDataSource.getFriend(friendColumnId, new GetFriendCallback() {
            @Override
            public void onFriendLoaded(Friend friend) {
                callback.onFriendLoaded(friend);
            }

            @Override
            public void onDataNotAvailable() {
                // Load from remoteDataSource
            }
        });
    }

    @Override
    public void saveFriend(@NonNull Friend friend) {
        checkNotNull(friend);
        mFriendRemoteDataSource.saveFriend(friend);
        mFriendsLocalDataSource.saveFriend(friend);
    }
}