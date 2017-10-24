/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.davidea.blueapp.persistence.api.network;

import android.support.annotation.Nullable;

import java.io.IOException;

import retrofit2.Response;
import timber.log.Timber;

/**
 * Common class used by API responses.
 *
 * @param <T> The {@code Response} object
 */
public class ApiResponse<T> {

    @Nullable
    public final T body;
    @Nullable
    public final String errorMessage;
    public final int code;

    /**
     * Default constructor for Internal Server Error (500) {@code http} code, {@code null} body
     * and {@code exception} message.
     *
     * @param error the Exception
     */
    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        errorMessage = error.getMessage();
    }

    /**
     * Handles the error response properly.
     *
     * @param response the Response from Api
     */
    public ApiResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException exception) {
                    Timber.e(exception, "Error while parsing error response");
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            body = null;
            errorMessage = message;
        }
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

}