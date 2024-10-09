<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Glucose extends Model
{
    use HasFactory;

    protected $fillable = [
        'rec_id',
        'pt_id',
        'bg_date',
        'bg_time',
        'bg_level',
        'calibration',
        'file_type',
        'date_time',
    ];

    public $table = 'glucose_tbl';
    protected $casts = [
        'bg_date' => 'datetime',
        'bg_time' => 'datetime',
    ];
}