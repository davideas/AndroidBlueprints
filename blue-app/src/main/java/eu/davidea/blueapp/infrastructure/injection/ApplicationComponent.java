/*
 * *
 *  * Copyright (C) 2017 Ryan Kay Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package eu.davidea.blueapp.infrastructure.injection;

import javax.inject.Singleton;

import dagger.Component;
import eu.davidea.blueapp.MyApplication;
import eu.davidea.blueapp.ui.MainActivity;

/**
 * Annotated as a Singleton since we don't want to have multiple instances of a Single Database.
 *
 * @author Davide
 * @since 17/09/2017
 */
@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        Builder apiModule(ApiModule module);

        Builder applicationModule(ApplicationModule module);

        ApplicationComponent build();
    }

    void inject(MyApplication application);

    void inject(MainActivity activity);

}