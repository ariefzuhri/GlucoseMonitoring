<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('glucose_tbl', function (Blueprint $table) {
            $table->id('rec_id');
            $table->bigInteger('pt_id');
            $table->string('bg_date');
            $table->string('bg_time');
            $table->double('bg_level');
            $table->tinyInteger('calibration');
            $table->string('file_type');
            $table->dateTime('date_time');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('glucoses');
    }
};