<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Event extends Model
{
    use HasFactory;

    protected $fillable = [
        'ev_id',
        'pt_id',
        'ev_type',
        'ev_desc',
        'ev_date',
        'ev_time',
    ];

    protected $primaryKey = 'ev_id';
    protected $casts = [
        'ev_date' => 'datetime',
        'ev_time' => 'datetime',
    ];
}