<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class Event extends JsonResource
{
    public function toArray(Request $request): array
    {
        return [
            'ev_id' => $this->ev_id,
            'pt_id' => $this->pt_id,
            'ev_type' => $this->ev_type,
            'ev_desc' => $this->ev_desc,
            'ev_date' => $this->ev_date->format('Y-m-d'),
            'ev_time' => $this->ev_time->format('H:i'),
            'created_at' => $this->created_at->format('Y-m-d H:i:s'),
            'updated_at' => $this->updated_at->format('Y-m-d H:i:s'),
        ];
    }
}