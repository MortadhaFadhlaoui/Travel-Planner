<?php

namespace App\Http\Controllers;

use App\User;
use Illuminate\Http\Request;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use Tymon\JWTAuth\Exceptions\JWTException;
use Tymon\JWTAuth\Facades\JWTAuth;
use DB;


class AuthenticateController extends Controller
{
    public function __construct()
    {
        // Apply the jwt.auth middleware to all methods in this controller
        // except for the authenticate method. We don't want to prevent
        // the user from retrieving their token if they don't already have it
        $this->middleware('jwt.auth', ['except' => ['register','login']]);
    }

    public function index()
    {
        $currentUser = JWTAuth::parseToken()->authenticate();
        return $currentUser;
    }

    /**
     * API Register
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function registeragent(Request $request)
    {
        $rules = [
            'username' => 'unique:users',
            'email' => 'unique:users',
        ];
        $input = $request->only('username', 'email');
        $validator = Validator::make($input, $rules);
        if($validator->fails()) {
            return response()->json(['success'=> false, 'error'=> $validator->messages()]);
        }
        $username = $request->username;
        $email = $request->email;
        $numtel = $request->numtel;
        $password = $request->password;
                $role = $request->role;
        $user = User::create(['username' => $username, 'email' => $email,'numtel' => $numtel,'role' => $role, 'password' => Hash::make($password)]);
        return $this->login($request);
    }
     /**
     * API Register
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function register(Request $request)
    {
        $rules = [
            'username' => 'unique:users',
            'email' => 'unique:users',
        ];
        $input = $request->only('username', 'email');
        $validator = Validator::make($input, $rules);
        if($validator->fails()) {
            return response()->json(['success'=> false, 'error'=> $validator->messages()]);
        }
        $username = $request->username;
        $email = $request->email;
        $numtel = $request->numtel;
        $password = $request->password;
                $role = $request->role;
        $user = User::create(['username' => $username, 'email' => $email,'numtel' => $numtel,'role' => $role, 'password' => Hash::make($password)]);
        return $this->login($request);
    }
    /**
     * API Login, on success return JWT Auth token
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function login(Request $request)
    {
        $credentials = [
            'username' => $request->username,
            'password' => $request->password,
        ];
        try {
            // attempt to verify the credentials and create a token for the user
            if (! $token = JWTAuth::attempt($credentials)) {
                return response()->json(['success' => false, 'error' => 'We cant find an account with this credentials.'], 401);
            }
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false, 'error' => 'Failed to login, please try again.'], 500);
        }
        // all good so return the token
        $currentUser = Auth::User();
        return response()->json(['success' => true, 'data'=> [ 'token' => $token ], 'user'=> $currentUser]);
    }
    /**
     * Log out
     * Invalidate the token, so user cannot use it anymore
     * They have to relogin to get a new token
     *
     * @return \Illuminate\Http\JsonResponse
     * @param Request $request
     */
    public function logout(Request $request) {
        $this->validate($request, ['token' => 'required']);
        try {
            JWTAuth::invalidate($request->input('token'));
            return response()->json(['success' => true]);
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false, 'error' => 'Failed to logout, please try again.'], 500);
        }
    }
 
     /**
     * API a
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function Getallagents(Request $request)
    {

        try {
            $saveds = DB::table('users')->where('role',"agent" )->get();
            return response()->json(['success' => true,'agents' => $saveds]);
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
    public function Deletemyagent(Request $request)
    {

        try {
            $saveds = DB::table('users')->where('id',$request->id )->delete();
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
    public function getphoto(Request $request, $id)
    {
        try{
            try{
                //Find the user object from model if it exists
                $user= User::findOrFail($id);

                if ($user->image != null){
                    $image=base64_encode(file_get_contents('C:\Users\AHMED Mohamed\TravelWebServices\TravelPlanner\Images\img'.$user->username.'.JPEG'));
                    return response()->json(['success' => true, 'photo'=> $image]);
                }
                return response()->json(['success' => false, 'photo'=> 'user didnt have photo']);
            }
            catch(ModelNotFoundException $err){
                return response()->json(['success' => false, 'error' => 'user not found']);
            }
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
    public function update(Request $request, $id)
    {
        try{
            try{
                //Find the user object from model if it exists
                $user= User::findOrFail($id);

                //$request contain your post data sent from your edit from
                //$user is an object which contains the column names of your table

                //Set user object attributes
                $user->nom = $request->nom;
                $user->prenom = $request->prenom;
                $user->email = $request->email;
                if ($request->image !=null ){
                    file_put_contents('C:\Users\AHMED Mohamed\TravelWebServices\TravelPlanner\Images\img'.$user->username.'.JPEG',base64_decode($request->get('file')));
                    $user->image = 'img'.$request->image;
                }
                //$user->image = $request['address'];
                if ($request->password !=null ){
                    $user->password = Hash::make($request->password);
                }
                if ($request->numtel !=null ){
                    $user->numtel = $request->numtel;
                }

                // Save/update user.
                // This will will update your the row in ur db.
                $user->save();

                return response()->json(['success' => true, 'user'=> $user]);
            }
            catch(ModelNotFoundException $err){
                return response()->json(['success' => false, 'error' => 'user not found']);
            }
        } catch (JWTException $e) {
            // something went wrong whilst attempting to encode the token
            return response()->json(['success' => false]);
        }
    }



}
