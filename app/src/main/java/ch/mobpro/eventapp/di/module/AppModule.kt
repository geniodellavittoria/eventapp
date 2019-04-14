package ch.mobpro.eventapp.di.module

import ch.mobpro.eventapp.repository.SessionTokenRepository
import ch.mobpro.eventapp.service.*
import ch.mobpro.eventapp.service.RestClientModule.createService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RestClientModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideAuthService(): AuthService {
        return createService(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideEventService(): EventService {
        return createService(EventService::class.java)
    }

    @Singleton
    @Provides
    fun provideEventCategoryService(): EventCategoryService {
        return createService(EventCategoryService::class.java)
    }

    @Singleton
    @Provides
    fun provideEventInvitationService(): EventInvitationService {
        return createService(EventInvitationService::class.java)
    }

    @Singleton
    @Provides
    fun provideEventRegistrationService(): EventRegistrationService {
        return createService(EventRegistrationService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(): UserService {
        return createService(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideSessionTokenRepository(): SessionTokenRepository {
        return SessionTokenRepository(provideAuthService())
    }
}