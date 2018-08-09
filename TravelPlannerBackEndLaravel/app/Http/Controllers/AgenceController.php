<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Agence;
use Illuminate\Support\Facades\DB;

class AgenceController extends Controller
{
      /**
     * API CreateSavedPlaces
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function SaveAgenceios(Request $request)
    {

        try {        
            $nom = $request->nom;
            $num_tel = $request->num_tel;
            $adresse = $request->adresse;
            $logo = $request->logo;
            $email = $request->email;
            file_put_contents('C:\Users\AHMED Mohamed\TravelWebServices\TravelPlanner\Images\img'.$nom.'.JPEG',base64_decode($request->file));
            $agence = Agence::create(['nom' => $nom, 'num_tel' => $num_tel,'adresse' => $adresse, 'logo' => 'img'.$nom.'.JPEG', 'email' => $email]);
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
    public function SaveAgence(Request $request)
    {

        try {        
            $nom = $request->nom;
            $num_tel = $request->num_tel;
            $adresse = $request->adresse;
            $logo = $request->logo;
            $email = $request->email;
            file_put_contents('C:\Users\AHMED Mohamed\TravelWebServices\TravelPlanner\Images\img'.$nom.'.JPEG',base64_decode($request->get('file')));
            $agence = Agence::create(['nom' => $nom, 'num_tel' => $num_tel,'adresse' => $adresse, 'logo' => 'img'.$nom.'.JPEG', 'email' => $email]);
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
    public function GetAgence(Request $request)
    {

        try {
          $image=null;
          $users = DB::table('agences')->get();
          
          
          if($users->count()>0){
           $imgname= $users->first()->logo;
           $image=base64_encode(file_get_contents('C:\Users\AHMED Mohamed\TravelWebServices\TravelPlanner\Images\\'.$imgname));
          }
          
          
         return response()->json(['success' => true,'agences' => $users,'photo' => $image]);
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
    public function modifagence(Request $request)
    {

        try {        
            $nom = $request->nom;
            $num_tel = $request->num_tel;
            $adresse = $request->adresse;
           
            $email = $request->email;
            $fax = $request->fax;
            file_put_contents('C:\Users\AHMED Mohamed\TravelWebServices\TravelPlanner\Images\img'.$nom.'.JPEG',base64_decode($request->get('file')));

            if($fax == ""){

             DB::table('agences')
            ->where('id', $request->id)
            ->update(['nom' => $nom, 'num_tel' => $num_tel, 'adresse' => $adresse , 'logo' => 'img'.$nom.'.JPEG', 'email' => $email ]);
            }else{
                DB::table('agences')
            ->where('id', $request->id)
            ->update(['nom' => $nom, 'num_tel' => $num_tel, 'adresse' => $adresse , 'logo' => 'img'.$nom.'.JPEG', 'email' => $email, 'num_fax' => $fax ]);
            }


            
            return response()->json(['success' => true]);
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }

}
