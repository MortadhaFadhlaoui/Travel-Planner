<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});

Route::group(['prefix' => 'api'], function()
{
    Route::resource('index', 'AuthenticateController', ['only' => ['index']]);
    Route::post('register', 'AuthenticateController@register');
    Route::post('registeragent', 'AuthenticateController@registeragent');
    Route::post('login', 'AuthenticateController@login');
    Route::post('Getallagents', 'AuthenticateController@Getallagents');

    Route::group(['middleware' => ['jwt.auth']], function() {
      Route::post('update/{id}', 'AuthenticateController@update');
        Route::post('getphoto/{id}', 'AuthenticateController@getphoto');
        Route::get('logout', 'AuthenticateController@logout');
        Route::get('Deletemyagent', 'AuthenticateController@Deletemyagent');
        Route::post('save', 'SavedPlacesController@SavePlace');
        Route::post('addagence', 'AgenceController@SaveAgence');
        Route::post('modifagence', 'AgenceController@modifagence');
        Route::post('addagenceios', 'AgenceController@SaveAgenceios');
        Route::post('getagence', 'AgenceController@GetAgence');
        Route::post('getsaved', 'SavedPlacesController@GetSavedByUser');
        Route::post('Deletemysaved', 'SavedPlacesController@Deletemysaved');
        Route::post('Getmysaved', 'SavedPlacesController@Getmysaved');
        Route::post('SavePack', 'PackController@SavePack');
        Route::post('SavePays', 'PackController@SavePays');
        Route::post('SaveDay', 'PackController@SaveDay');
        Route::post('Deletemypack', 'PackController@Deletemypack');
         Route::post('Getallpacks', 'PackController@Getallpacks');
         Route::post('Getdayplans', 'PackController@Getdayplans');
         Route::post('Getdayplanscal', 'PackController@Getdayplanscal');
         Route::post('Getacceptedres', 'ReservationController@Getacceptedres');
         Route::get('Getonholdres', 'ReservationController@Getonholdres');
         Route::post('recherche', 'SavedPlacesController@RecherchePacks');
         Route::post('reserverpack/{idpack}', 'SavedPlacesController@InsertReservation');
          Route::get('acceptreservation', 'ReservationController@acceptreservation');
          Route::get('deletereservation', 'ReservationController@deletereservation');
          Route::post('getdayplanmorta/{idpack}', 'SavedPlacesController@Getdayplansmorta');
          Route::post('getreservation', 'SavedPlacesController@getReservation');
       

    });
});