/*
 *  Copyright 2024 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cnm.deepdive.cyphertext.service;


import edu.cnm.deepdive.cyphertext.model.entity.Game;
import edu.cnm.deepdive.cyphertext.model.entity.Guess;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CypherTextServiceProxy {

  @POST("games")
  Single<Game> startGame(
    @Body Game game,
    @Header("Authorization") String bearerToken);

  @GET("games/{id}")
  Single<Game> getGame(
      @Path("id") String id,
      @Header("Authorization") String bearerToken);

  @POST("games/{id}/guesses")
  Single<Game> submitGuess(
      @Path("id") String gameId,
      @Body Guess guess,
      @Header("Authorization") String bearerToken);

}
