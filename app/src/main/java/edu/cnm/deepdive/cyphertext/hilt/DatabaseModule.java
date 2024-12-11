package edu.cnm.deepdive.cyphertext.hilt;

import android.content.Context;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.cyphertext.model.dao.UserDao;
import edu.cnm.deepdive.cyphertext.service.LocalDatabase;
import javax.inject.Singleton;

/**
 * Uses Dagger {@link Provides @Provides}-annotated methods to satisfy dependencies on concrete
 * implementations of {@link LocalDatabase} and {@link UserDao}.
 */
@InstallIn(SingletonComponent.class)
@Module
public final class DatabaseModule {

  DatabaseModule() {
    // Package-private constructor to avoid automatic HTML documentation generation.
  }

  @Provides
  @Singleton
  LocalDatabase provideLocalDatabase(@ApplicationContext Context context) {
    return Room
        .databaseBuilder(context, LocalDatabase.class, LocalDatabase.NAME)
        .addCallback(new LocalDatabase.Callback())
        .build();
  }

  @Provides
  UserDao provideUserDao(LocalDatabase database) {
    return database.getUserDao();
  }

  // TODO Add additional methods so satisfy dependencies on other DAO interface implementations.

}
