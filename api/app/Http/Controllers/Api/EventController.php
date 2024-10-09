<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Http\Controllers\Api\BaseController as BaseController;
use Validator;
use App\Models\Event;
use App\Http\Resources\Event as EventResource;

class EventController extends BaseController
{

    public function index()
    {
        $events = Event::
            orderBy('pt_id', 'asc')
            ->orderBy('ev_date', 'asc')
            ->orderBy('ev_time', 'asc')
            ->get();
        return $this->sendResponse(EventResource::collection($events), 'Events fetched.');
    }

    public function store(Request $request)
    {
        $input = $request->all();
        $validator = Validator::make($input, [
            'pt_id' => 'required',
            'ev_type' => 'required',
            'ev_desc' => 'required',
            'ev_date' => 'required',
            'ev_time' => 'required',
        ]);
        if ($validator->fails()) {
            return $this->sendError($validator->errors());
        }
        $event = Event::create($input);
        return $this->sendResponse(new EventResource($event), 'Event created.');
    }

    public function show($pt_id)
    {
        $events = Event::
            where("pt_id", '=', $pt_id)
            ->orderBy('ev_date', 'asc')
            ->orderBy('ev_time', 'asc')
            ->get();
        return $this->sendResponse(EventResource::collection($events), 'Events fetched.');
    }

    public function update(Request $request, Event $event)
    {
        $input = $request->all();
        $validator = Validator::make($input, [
            'pt_id' => 'required',
            'ev_type' => 'required',
            'ev_desc' => 'required',
            'ev_date' => 'required',
            'ev_time' => 'required',
        ]);
        if ($validator->fails()) {
            return $this->sendError($validator->errors());
        }
        $event->pt_id = $input['pt_id'];
        $event->ev_type = $input['ev_type'];
        $event->ev_desc = $input['ev_desc'];
        $event->ev_date = $input['ev_date'];
        $event->ev_time = $input['ev_time'];
        $event->save();

        return $this->sendResponse(new EventResource($event), 'Event updated.');
    }

    public function destroy(Event $event)
    {
        $event->delete();
        return $this->sendResponse(new EventResource($event), 'Event deleted.');
    }
}