<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Tymon\JWTAuth\Exceptions\JWTException;
use Tymon\JWTAuth\Facades\JWTAuth;
use DB;

class ReservationController extends Controller
{
    /**
     * API CreateSavedPlaces
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function Getacceptedres(Request $request)
    {

        try { 
        	$users = DB::table('reservations')->where('etat','1')->get();
        	if (count($users)>0) {
           foreach ($users as $im) {
                    

                    $imagess[] =  DB::table('users')->where('id',$im->user_id)->get();;
                    $imagesss[] =  DB::table('packs')->where('id',$im->pack_id)->get();;
                }
                 return response()->json(['success' => true,'res' => $users ,'users' => $imagess,'packs' => $imagesss]);
          }
           return response()->json(['success' => false]);
          
        
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }
    /**
     * API CreateSavedPlaces
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function Getonholdres(Request $request)
    {

        try { 
        	$users = DB::table('reservations')->where('etat','en attente')->get();
        	if (count($users)>0) {
        	
           foreach ($users as $im) {
                    

                    $imagess[] =  DB::table('users')->where('id',$im->user_id)->get();;
                    $imagesss[] =  DB::table('packs')->where('id',$im->pack_id)->get();;
                }
          return response()->json(['success' => true,'res' => $users ,'users' => $imagess,'packs' => $imagesss]);
           }	
          return response()->json(['success' => false]);
         
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }
    /**
     * API CreateSavedPlaces
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function acceptreservation(Request $request)
    {

        try { 
        	 DB::table('reservations')
            ->where('id', $request->id)
            ->update([ 'etat' => '1' ]);
     
           
          
         return response()->json(['success' => true]);
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }
     /**
     * API CreateSavedPlaces
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function deletereservation(Request $request)
    {

        try { 
        	 DB::table('reservations')
            ->where('id', $request->id)
            ->update([ 'etat' => '0' ]);
     
           
          
         return response()->json(['success' => true]);
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }
}
