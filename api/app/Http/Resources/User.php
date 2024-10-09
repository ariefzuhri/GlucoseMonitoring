<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class User extends JsonResource
{
    public function toArray(Request $request): array
    {
        return [
            'id' => $this->id,
            'pt_id' => $this->pt_id,
            'fullname' => $this->fullname,
            'name' => $this->name,
            'email' => $this->email,
            'photos' => $this->photos,
            'company' => $this->company,
            'address' => $this->address,
            'photo' => $this->photo,
            'status' => $this->status,
            'created_on' => $this->created_on,
            'is_admin' => $this->is_admin,
        ];
    }
}