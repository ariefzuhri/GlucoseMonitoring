<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;

class User extends Authenticatable
{
    use HasFactory;

    protected $fillable = [
        'id',
        'pt_id',
        'fullname',
        'name',
        'email',
        'password',
        'phone',
        'company',
        'address',
        'photo',
        'status',
        'created_on',
        'is_admin',
    ];

    protected $hidden = [
        'password',
    ];
}