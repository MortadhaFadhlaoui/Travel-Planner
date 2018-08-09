<?php

namespace App\Http\Controllers;

use App\Saved;
use App\User;
use App\Pays;
use App\Reservation;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Tymon\JWTAuth\Exceptions\JWTException;
use Tymon\JWTAuth\Facades\JWTAuth;
use DB;

class SavedPlacesController extends Controller
{
    /**
     * API CreateSavedPlaces
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function SavePlace(Request $request)
    {

        try {

            $title = $request->title;
            $name = $request->name;
            $lat = $request->lat;
            $categorie = $request->categorie;
            $log = $request->log;
            $currentUser = Auth::User()->getAuthIdentifier();

            $saveds = DB::table('saveds')->where('user_id',$currentUser)->where('name',$request->name)->get();
            if (!$saveds->isEmpty()) {
                return response()->json(['success' => false]);
            }
              $saved = Saved::create(['title' => $title, 'name' => $name,'categorie' => $categorie, 'lat' => $lat, 'log' => $log,'user_id'=>$currentUser]);
            return response()->json(['success' => true]);
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }

    /**
     * API a
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function GetSavedByUser(Request $request)
    {

        try {
            $currentUser = Auth::User()->getAuthIdentifier();
            $saveds = User::find($currentUser)->saveds;
            return response()->json(['success' => true,'saved' => $saveds]);
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }
     /**
     * API a
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function Getmysaved(Request $request)
    {

        try {
            $saveds = DB::table('saveds')->where('user_id',$request->id )->get();
            return response()->json(['success' => true,'saveds' => $saveds]);
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }

    /**
     * API a
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function Deletemysaved(Request $request)
    {

        try {
            $saveds = DB::table('saveds')->where('id',$request->id )->delete();
            return response()->json(['success' => true]);
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }

/**
     * API a
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function RecherchePacks(Request $request)
    {

        try {
            $currentUser = Auth::User()->getAuthIdentifier();
            $date0 = date('Y-m-d H:i:s', strtotime($request->datedepart . ' +2 day'));
            $date1 = date('Y-m-d H:i:s', strtotime($request->datedepart . ' -2 day'));
            $date2 = date('Y-m-d H:i:s', strtotime($request->datearrive . ' +2 day'));
            $date3 = date('Y-m-d H:i:s', strtotime($request->datearrive . ' -2 day'));
            $packs = DB::table('packs')->where('nom_depart',$request->nomdepart )->whereBetween('date_debut', array($request->datedepart,$date0))->orwhereBetween('date_debut', array($request->datedepart,$date1))->whereBetween('date_fin', array($request->datearrive,$date2))->orwhereBetween('date_fin', array($request->datearrive,$date3))->orderBy('prix', 'asc')->get();
            $string_array = explode(";",$request->nomarrive);
            $paa = array();
            for($i = 0; $i < sizeof($string_array); $i++)
            {
                foreach($packs as $pack) {
                    $pa = Pays::where("pack_id",$pack->id)->where('nom_pays', $string_array[$i])->where('etat','<=',sizeof($string_array))->first();
                    if ($pa !=  null){
                        $paa[] = ['pack' => $pack,'pays' => $pa];
                    }
                }            
            } 

            if ($paa ==  null) {
                return response()->json(['success' => false,'packs' => "no pack found"]);
            }                  
            return response()->json(['success' => true,'packs' => $paa]);
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }

    /**
     * API a
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function Getdayplansmorta(Request $request)
    {

        try {

            $saveds = DB::table('dayplans')->where('pack_id',$request->idpack)->get();

            return response()->json(['success' => true,'dayplans' => $saveds]);
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }
    public function getReservation(Request $request)
    {

        try {
        $currentUser = Auth::User()->getAuthIdentifier();
        $reservations = DB::table('reservations')->where('user_id',$currentUser)->get();
        //$reservations = DB::table('packs')->where('id',$reservations)->get();

         if ($reservations->isEmpty()) {
                return response()->json(['success' => false,'reservations' => "no reservations found"]);
            }   
            foreach ($reservations as $im) {
                    

                    $imagess[] =  DB::table('packs')->where('id',$im->pack_id)->get();;
                    
                }

            return response()->json(['success' => true,'reservations' => $reservations,'packs'=>$imagess]);
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }

    /**
     * API a
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function InsertReservation(Request $request)
    {

        try {

            $dt = new \DateTime();
            $date = $dt->format('Y-m-d H:i:s');
            $etat = "en attente";
            $idpack = $request->idpack;
            $currentUser = Auth::User()->getAuthIdentifier();
            $res = DB::table('reservations')->where('pack_id',$request->idpack )->where('user_id',$currentUser )->get();
            if ($res->isEmpty()) {
                $reservation = Reservation::create(['date' => $date, 'etat' => $etat, 'pack_id' => $idpack,'user_id'=>$currentUser]);
            return response()->json(['success' => true]);
            }else{
             return response()->json(['success' => false]);   
            }
            
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }
}
