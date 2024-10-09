<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Http\Controllers\Api\BaseController as BaseController;
use Illuminate\Support\Facades\Auth;
use App\Models\User;
use App\Http\Resources\User as UserResource;

class AuthController extends BaseController
{
    public function signin(Request $request)
    {
        // if (
        //     Auth::attempt(
        //         [
        //             'email' => $request->email,
        //             'password' => $request->password,
        //         ]
        //     )
        // ) {
        //     $authUser = Auth::user();
        //     return $this->sendResponse(new UserResource($authUser), 'User signed in');
        // } else {
        //     return $this->sendError('Unauthorised.', ['error' => 'Unauthorised']);
        // }

        $user = User::where('email', '=', $request->email)
            ->where('id', '=', $request->password)
            ->first();
        if (is_null($user)) {
            return $this->sendError('Unauthorised.');
        }
        return $this->sendResponse(new UserResource($user), 'User signed in.');
    }
}