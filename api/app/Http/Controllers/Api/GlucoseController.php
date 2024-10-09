<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Api\BaseController as BaseController;
use App\Models\Glucose;
use App\Http\Resources\Glucose as GlucoseResource;

class GlucoseController extends BaseController
{
    public function index()
    {
        $glucoses = Glucose::
            where('bg_level', '>', 0)
            ->orderBy('pt_id', 'asc')
            ->orderBy('bg_date', 'asc')
            ->orderBy('bg_time', 'asc')
            ->get();
        return $this->sendResponse(GlucoseResource::collection($glucoses), 'Glucoses fetched.');
    }

    public function show($pt_id)
    {
        $glucoses = Glucose::
            where("pt_id", '=', $pt_id)
            ->where('bg_level', '>', 0)
            ->orderBy('bg_date', 'asc')
            ->orderBy('bg_time', 'asc')
            ->get();
        return $this->sendResponse(GlucoseResource::collection($glucoses), 'Glucoses fetched.');
    }
}