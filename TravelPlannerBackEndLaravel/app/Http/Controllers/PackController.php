<?php

namespace App\Http\Controllers;
use Illuminate\Http\Request;
use App\Pack;
use App\Pays;
use App\Dayplan;
use Illuminate\Support\Facades\DB;

class PackController extends Controller
{
      /**
     * API CreateSavedPlaces
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function SavePack(Request $request)
    {

        try {        
            $depart = $request->depart;
            $price = $request->price;
            $datedepart = $request->datedepart;
            $datea = $request->datea;
            $id = DB::table('packs')->insertGetId(
       		 array('nom_depart' => $depart,
               'date_debut' => $datedepart,
               'date_fin' => $datea,
               'prix' => $price
                ));

            return response()->json(['success' => true,'id' => $id]);
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
    public function SavePays(Request $request)
    {

        try {

            $nom = $request->nom;
            $id = $request->id;
            $etat = $request->etat;
            $saved = Pays::create(['nom_pays' => $nom,'pack_id'=>$id,'etat'=>$etat]);
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
    public function SaveDay(Request $request)
    {

        try {

            $title = $request->title;
            $description = $request->description;
            $date = $request->date;
            $heure = $request->heure;
			$id = $request->id;
			$x =$date.' '.$heure.':00';

			$timestamp = strtotime($x);
			$date1  =date("Y-m-d H:i:s", $timestamp);
			 


            $saved = Dayplan::create(['title' => $title,'pack_id'=>$id,'description' => $description,'date' => $date1]);
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
    public function Getallpacks(Request $request)
    {

        try {
         
          $users = DB::table('packs')->get();
          
         if (count($users)>0) {
          foreach ($users as $im) {
                    

                    $imagess[] =  DB::table('pays')->where('pack_id',$im->id)->get();;
                }
                return response()->json(['success' => true,'packs' => $users,'pays' => $imagess ]);
            }
           return response()->json(['success' => false]);
         
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }

     public function Getdayplans(Request $request)
    {

        try {
            $imagess = array();
            $imagesss = array();
            $id = $request->id;
         $res = DB::table('reservations')->where('user_id',$id)->get();
         foreach ($res as $im) {
            if($im->etat == 1){

                $imagess[] =  DB::table('packs')->where('id',$im->pack_id)->get();;


            }
         }
         foreach ($imagess as $im1) {
            

                $imagesss[] =  DB::table('dayplans')->where('pack_id',$im1[0]->id)->get();;


            
         }
        
          
         return response()->json(['success' => true,'dayplans' => $imagesss]);
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }
     public function Getdayplanscal(Request $request)
    {

        try {
         
         $saveds = DB::table('dayplans')->where('pack_id',21)->get();
          
         foreach ($saveds as $im) {
                    
                     $x=date('Y-m-d h:m:s', strtotime('-1 day', strtotime($im->date)));
                    $imagess[] = $x;
                    $imagesss[] = $im->title;
                }
          
          
         return response()->json(['success' => true,'dates' => $imagess ,'titles' => $imagesss]);
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
    public function Deletemypack(Request $request)
    {

        try {
            $saveds = DB::table('packs')->where('id',$request->id )->delete();
            return response()->json(['success' => true]);
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }
}




