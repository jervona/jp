package ja.interview.jpmccodingchallenge.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ja.interview.jpmccodingchallenge.data.SharedPref
import ja.interview.jpmccodingchallenge.data.SharedPrefImpl
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class InternalDataModule {
    @Provides
    @Singleton
    fun provideSharedPref(
        @ApplicationContext appContext: Context
    ): SharedPref = SharedPrefImpl(appContext)
}