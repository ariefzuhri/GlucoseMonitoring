<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class Glucose extends JsonResource
{
    public function toArray(Request $request): array
    {
        return [
            'rec_id' => $this->rec_id,
            'pt_id' => $this->pt_id,
            'bg_date' => $this->bg_date->format('Y-m-d'),
            'bg_time' => $this->bg_time->format('H:i'),
            'bg_level' => $this->bg_level,
            'calibration' => $this->calibration,
            'file_type' => $this->file_type,
            'date_time' => $this->date_time,
        ];
    }
}